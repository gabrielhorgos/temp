#!/usr/bin/python

import os, sys
from optparse import OptionParser
import logging.config
from subprocess import Popen
import shutil

# absolute path of local directory containing this script
local_abs_path = os.path.dirname(os.path.abspath(__file__))
workspace_path = os.path.abspath(os.path.join(local_abs_path, '..', '..'))

# initialize logging
logging.config.fileConfig(os.path.join(local_abs_path, 'log.conf'))
_LOG = logging.getLogger()
# shortcuts
lgi, lgw, lge, lgex, dbg = _LOG.info, _LOG.warn, _LOG.error, _LOG.exception, _LOG.debug

# globals
version = "Configure Payara 4.1.2 instance for running the iam services (LOCAL and CI environments) [3.0]"
options = None

# GCDM_HOME
if os.getenv('GCDM_HOME'):
	GCDM_HOME = os.getenv('GCDM_HOME')
	lgi('GCDM_HOME = %s' % (GCDM_HOME));
else:
	GCDM_HOME = os.path.join('c:\work\gcdm')
	lgi('Variable GCDM_HOME is not set, using default value %s' % (GCDM_HOME));

# exit codes
class ExitCode:
    OK = 0
    ERROR = 1

# illegal argument exception
class InvalidCmdArguments(Exception): pass
# service error exception
class ProcessError(Exception): pass

def run_command(cmdlist):
    dbg("running: " + " ".join(cmdlist))
    if options.simulate: return

    p = Popen(cmdlist)
    p.wait()
    if p.returncode != 0:
        raise ProcessError("process failed with code %i" % p.returncode)

# asadmin commands running without admin user
def asadmin0(command, *params):
    run_command([os.path.join(options.appserver, 'bin', 'asadmin.bat'), command] + list(params))

# asdmin commands with admin user required
def asadmin1(command, *params):
    asadmin0(command, '--user', 'admin', '--passwordfile', os.path.join(local_abs_path, 'passwordfile'), *params)

# asdmin commands with admin user and admin port required
def asadmin2(command, *params):
    asadmin1(command, '--port', str(options.portbase + 48), *params)

def main():
    try:
        print(version)

        # define command line options
        parser = OptionParser(usage="%prog [options]", version="%prog - " + version)
        parser.add_option("--simulate", help="do not call asadmin", action="store_true", default=False)
        parser.add_option("--portbase", help="see asadmin --portbase option", type="int", default=6000)
        parser.add_option("--domain", help="Glassfish domain name", default='gcdm-login')
        parser.add_option("--appserver", help="Glassfish root path", default= os.path.join(GCDM_HOME, 'bin/payara41/glassfish'))
        parser.add_option("--deploy", help="do deployment only", action="store_true", default=False)
        parser.add_option("--restart", help="restart the domain", action="store_true", default=False)
        parser.add_option("--stop", help="stop the domain", action="store_true", default=False)
        parser.add_option("--start", help="start the domain", action="store_true", default=False)
        parser.add_option("--delete", help="delete the domain", action="store_true", default=False)
        parser.add_option("--create", help="create the domain", action="store_true", default=False)
        parser.add_option("--nodebug", help="do not use --debug when starting domain", action="store_true", default=False)
        parser.add_option("--mqpath", help="Web Sphere MQ root path")

        # parse command line
        global options
        (options, args) = parser.parse_args()
        if args: raise InvalidCmdArguments("unknown arguments: %s" % args)
        if not (options.deploy or options.restart or options.stop or options.start or options.delete or options.create):
            parser.error("one of options is required: --deploy --restart --stop --start --delete --create")
        if options.nodebug:
            options.debug = '--debug=false'
        else:
            options.debug = '--debug=true'
        dbg("command line parsed: " + str(options))

        if options.stop:
            asadmin0('stop-domain', options.domain)

        if options.start:
            asadmin0('start-domain', options.debug, options.domain)

        if options.delete or options.create:
            try:
                asadmin0('stop-domain', options.domain)
            except ProcessError:
                pass
            try:
                asadmin0('delete-domain', options.domain)
            except ProcessError:
                glassfish_domain = os.path.abspath(os.path.join(options.appserver, 'domains', options.domain))
                lgi('removing directory %s' % glassfish_domain)
                os.system("rmdir %s /s /q" % glassfish_domain)

        if options.create:
            # create domain with the given portbase
            asadmin1('create-domain', '--portbase', str(options.portbase), options.domain)
            
			# start domain to manage settings
            asadmin0('start-domain', options.domain)
            
            # set timeouts
            asadmin2('set', 'server-config.transaction-service.timeout-in-seconds=60')

            # set configuration path as in the local environment
            asadmin2('create-jvm-options', '-XX\:MaxPermSize=512m')
            asadmin2('create-jvm-options', '-Duser.language=en')
            asadmin2('create-jvm-options', '-Duser.country=US')
            asadmin2('create-jvm-options', '-Dfile.encoding=UTF-8')
			
            domain_root = os.path.abspath(os.path.join(options.appserver, 'domains', options.domain))
            domain_lib = os.path.abspath(os.path.join(domain_root, 'lib'))
			
			# copy libs from os-build-gcdm-login\src\main\config\lib to payara41\glassfish\lib
            appagent_lib_path = os.path.join(workspace_path, 'os-build-gcdm-login', 'src', 'main', 'config', 'lib')
            c2b_sec_common_file = os.path.abspath(os.path.join(appagent_lib_path, '*.jar'))
            os.system ("copy %s %s" % (c2b_sec_common_file, domain_lib))
            lgi('copied %s to %s' % (c2b_sec_common_file, domain_lib))

            # copy app-agent.properties from os-build-deployment-scripts\config-map\DEV to project-synced
            project_synced_path = os.path.join(workspace_path, 'os-build-gcdm-login', 'configs', 'config-map', 'PMT1_DEV', 'app-agent')
            project_synced_dst_dir = os.path.abspath(os.path.join(domain_root, 'project-synced'))
            shutil.copytree(project_synced_path, project_synced_dst_dir)
            lgi('copied project-synced from %s to %s' % (project_synced_path, project_synced_dst_dir))

            system_properties_path = 'com.bmw.mastersolutions.gf.domain.dir=' + domain_root.replace('\\', '/')
            system_properties_path = system_properties_path.replace(':/', '\\:/', 1)
            asadmin2('create-system-properties', system_properties_path)

			# create c2b App Agent
            asadmin2('create-message-security-provider', '--target', 'server', '--classname', 'com.bmw.iam.auth.glassfish.Sam', '--layer', 'HttpServlet', '--providertype', 'server',  'C2bSec')  
            
        if options.restart or options.create:
            asadmin0('stop-domain', options.domain)
            asadmin0('start-domain', options.debug, options.domain)

        if options.deploy:
        
            # deploy war artifact
            app_name = os.path.join(os.path.dirname(local_abs_path), 'gcdm-login-war' , 'target', 'gcdm-login-war*' + '.war')
            lgi("deploying " + app_name)
            asadmin2('deploy', '--force=true', app_name)

        lgi("success")

        return ExitCode.OK

    except SystemExit:
        # ignore parser thrown exception
        pass

    except Exception, e:
        lgex(str(e))
        return ExitCode.ERROR

if __name__ == '__main__':
    sys.exit(main())

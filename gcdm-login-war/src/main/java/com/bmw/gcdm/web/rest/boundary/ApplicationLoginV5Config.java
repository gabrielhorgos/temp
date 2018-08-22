package com.bmw.gcdm.web.rest.boundary;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.bmw.gcdm.api.login.v5.boundary.AuthenticateApiResource;

/**
 * JAX RS application class for the Login V5 Api.
 *
 * @author qxk9919
 */
@ApplicationPath("/api/v5/")
public class ApplicationLoginV5Config extends Application {

	/**
	 * The default constructor.
	 */
	public ApplicationLoginV5Config() {
	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> resources = new HashSet<>();

		// register your services here
		resources.add(AuthenticateApiResource.class);

		return resources;
	}

}

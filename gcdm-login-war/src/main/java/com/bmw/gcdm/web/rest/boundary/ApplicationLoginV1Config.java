package com.bmw.gcdm.web.rest.boundary;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.bmw.gcdm.api.login.v1.boundary.AuthenticateApiResource;

/**
 * JAX RS application class for the Login V1 Api.
 *
 * @author qxk9919
 */
@ApplicationPath("/api/v1")
public class ApplicationLoginV1Config extends Application {

	/**
	 * The default constructor.
	 */
	public ApplicationLoginV1Config() {
	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> resources = new HashSet<>();

		// register your services here
		resources.add(AuthenticateApiResource.class);

		return resources;
	}

}

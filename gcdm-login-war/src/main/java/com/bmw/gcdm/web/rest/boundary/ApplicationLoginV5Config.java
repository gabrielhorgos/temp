package com.bmw.gcdm.web.rest.boundary;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.bmw.gcdm.api.login.v5.boundary.AuthenticateApiResource;
import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * JAX RS application class for the Login V5 Api.
 *
 * @author qxk9919
 */

@OpenAPIDefinition(
		info = @Info(
				title = "GCDM-Login",
				description = "GCDM Authenticate API V5"),
		tags = {
				@Tag(name = "Authenticate")
		}
)
@SecurityScheme(name = "authorization", type = SecuritySchemeType.DEFAULT)
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

		//register JAX-RS openapi resources
		resources.add(OpenApiResource.class);
		resources.add(AcceptHeaderOpenApiResource.class);

		return resources;
	}

}

package com.bmw.gcdm.api.login.v1.boundary;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.bmw.gcdm.api.login.v1.entity.Authentication;
import com.bmw.gcdm.api.login.v1.entity.Session;
import com.bmw.gcdm.bm.login.v1.boundary.AuthenticationBF;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;

/**
 * This class represents a BF (business facade aka boundary) for the BC
 * (business component) according to BMW CA 4.
 *
 * @author qxk9919
 */
@Path("/{clientId}/{clientVariantId}")
@Api(value = "GCDM Authenticate API V1", tags = "Authenticate", authorizations = @Authorization(value = "basic"))
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticateApiResource {

	@Inject
	private AuthenticationBF authenticationBF;

	@Context
	private HttpServletRequest httpRequest;

	/**
	 * The default constructor.
	 */
	public AuthenticateApiResource() {
		super();
	}

	/**
	 * Constructor for testing purposes.
	 *
	 * @param weatherBF BF to access business logic.
	 */
	public AuthenticateApiResource(final AuthenticationBF authenticationBF, final HttpServletRequest httpRequest) {
		this.authenticationBF = authenticationBF;
		this.httpRequest = httpRequest;
	}

	/**
	 * todo
	 *
	 * @param
	 * @param
	 * @return 
	 */
	@POST
	@Path("/authenticate")

	/*	@ApiOperation(value = "The login resource", notes = "Returns Gcdm Token", response = Session.class,
			tags = { "Login", })
			@ApiResponses(value = { @ApiResponse(code = 200, message = "",
			response = Session.class), @ApiResponse(code = 401, message = "Unauthorized.", response = ApiClientErrorResponse.class),
			@ApiResponse(code = 404, message = "Resource not available."),
			@ApiResponse(code = 422, message = "Unprocessable Entity.",
			response = ApiBusinessErrorResponse.class),
			@ApiResponse(code = 500, message = "Internal server error.", response = ApiServerErrorResponse.class), })*/

	public Response authenticate(@ApiParam(value = "clientId",
			required = true) @PathParam("clientId") @NotNull @Size(min = 1) final String clientId,
			@ApiParam(value = "clientVariantId",
					required = true) @PathParam("clientVariantId") @NotNull @Size(min = 1) final String clientVariantId,
			@ApiParam(value = "An optional header parameter used to indicate if the login device is new or not ",
					required = false, defaultValue = "true") @HeaderParam("BMW-New-Device") final boolean newDevice,
			final Authentication authentication) {
		Session session = new Session();
		session.setGcdmAuthenticationToken("halloFlami");
		// return result status and data
		return Response.ok().entity(session).build();
	}

}

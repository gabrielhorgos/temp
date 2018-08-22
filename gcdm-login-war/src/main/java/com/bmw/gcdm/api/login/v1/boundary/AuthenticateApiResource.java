package com.bmw.gcdm.api.login.v1.boundary;

import com.bmw.gcdm.api.login.v1.entity.Authentication;
import com.bmw.gcdm.api.login.v1.entity.Session;
import com.bmw.gcdm.bm.login.v1.boundary.AuthenticationBF;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * This class represents a BF (business facade aka boundary) for the BC
 * (business component) according to BMW CA 4.
 *
 * @author qxk9919
 */
@Path("/{clientId}/{clientVariantId}")
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
    @Operation(summary = "Authenticate user by login id",
            tags = {"Login"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "GCDM Session object for authenticated user",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = com.bmw.gcdm.api.login.v5.entity.Session.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not available."),
                    @ApiResponse(responseCode = "400", description = "User not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error.")})
    public Response authenticate(@Parameter(description = "clientId",
            required = true) @PathParam("clientId") @NotNull @Size(min = 1) final String clientId,
                                 @Parameter(description = "clientVariantId",
                                         required = true) @PathParam("clientVariantId") @NotNull @Size(min = 1) final String clientVariantId,
                                 @Parameter(description = "An optional header parameter used to indicate if the login device is new or not ",
                                         required = false) @HeaderParam("BMW-New-Device") final boolean newDevice,
                                 final Authentication authentication) {
        Session session = new Session();
        session.setGcdmAuthenticationToken("halloFlami");
        // return result status and data
        return Response.ok().entity(session).build();
    }

}

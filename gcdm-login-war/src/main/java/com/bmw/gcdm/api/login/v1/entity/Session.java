package com.bmw.gcdm.api.login.v1.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * This class represents a BE (business entity) for the BC (business component)
 * according to BMW CA 4.
 *
 * @author qxk9919
 */

@Schema(name = "Session", description = "A gcdm session object is returned for the authenticated user. It contains the Gcdm Token.")
public class Session {

    private String gcdmAuthenticationToken;

    /**
     * The default constructor.
     */
    public Session() {
        super();
    }

    /**
     * The Gcdm Token is an JWT...
     *
     * @return gcdmAuthenticationToken
     */
    @Schema(name = "The Gcdm Token", example = "\"ey....\"", required = true)
    @JsonProperty(value = "gcdmAuthenticationToken", required = true)
    public String getGcdmAuthenticationToken() {
        return gcdmAuthenticationToken;
    }

    /**
     * Set the gcdmAuthenticationToken
     *
     * @param gcdmAuthenticationToken
     */
    public void setGcdmAuthenticationToken(final String gcdmAuthenticationToken) {
        this.gcdmAuthenticationToken = gcdmAuthenticationToken;
    }

}

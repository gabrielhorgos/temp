package com.bmw.gcdm.api.login.v5.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * This class represents a BE (business entity) for the BC (business component)
 * according to BMW CA 4.
 *
 * @author qxk9919
 */
@Schema(name = "Authentication", description = "An authentication object that contains the user credentials.")
public class Authentication {

    private String loginId;

    /**
     * The default constructor.
     */
    public Authentication() {
        super();
    }

    /**
     * LoginId: one of mail, mobile, alias or gcid
     *
     * @return loginId
     */
    @Schema(name = "The loginId", example = "\"mail@mail.de\"", required = true)
    @JsonProperty(value = "loginId", required = true)
    public String getLoginId() {
        return loginId;
    }

    /**
     * Set the loginId
     *
     * @param mail, mobile, alias or gcid
     */
    public void setLoginId(final String loginId) {
        this.loginId = loginId;
    }

}

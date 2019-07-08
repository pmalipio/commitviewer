/*
 * Copyright (c) 2019.  Pedro Alípio, All Rights Reserved.
 *
 * This material is provided "as is", with absolutely no warranty expressed
 * or implied. Any use is at your own risk.
 *
 * Permission to use or copy this software for any purpose is hereby granted
 * without fee. Permission to modify the code and to distribute modified
 * code is also granted without any restrictions.
 */
package com.pmalipio.commitclient.data;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "verified",
    "reason",
    "signature",
    "payload"
})
public class Verification {

    @JsonProperty("verified")
    private Boolean verified;
    @JsonProperty("reason")
    private String reason;
    @JsonProperty("signature")
    private Object signature;
    @JsonProperty("payload")
    private Object payload;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("verified")
    public Boolean getVerified() {
        return verified;
    }

    @JsonProperty("verified")
    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    @JsonProperty("reason")
    public String getReason() {
        return reason;
    }

    @JsonProperty("reason")
    public void setReason(String reason) {
        this.reason = reason;
    }

    @JsonProperty("signature")
    public Object getSignature() {
        return signature;
    }

    @JsonProperty("signature")
    public void setSignature(Object signature) {
        this.signature = signature;
    }

    @JsonProperty("payload")
    public Object getPayload() {
        return payload;
    }

    @JsonProperty("payload")
    public void setPayload(Object payload) {
        this.payload = payload;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

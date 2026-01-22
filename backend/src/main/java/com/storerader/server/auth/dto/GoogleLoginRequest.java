package com.storerader.server.auth.dto;

public class GoogleLoginRequest {
    private String idToken;

    protected GoogleLoginRequest() {
    }
    public String getIdToken() { return idToken; }
}

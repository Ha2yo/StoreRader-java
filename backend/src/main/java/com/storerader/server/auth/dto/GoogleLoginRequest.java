package com.storerader.server.auth.dto;

public class GoogleLoginRequest {
    private String idToken;
    private String client_id;

    protected GoogleLoginRequest() {
    }
    public String getIdToken() { return idToken; }
    public String getClientId() { return client_id; }
}

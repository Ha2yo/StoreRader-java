package com.storerader.server.auth.dto;

public class GoogleLoginRequest {
    private String idToken;

    public String getIdToken() { return idToken; }
    public void setIdToken(String idToken) { this.idToken = idToken; }
}

package com.storerader.server.auth.dto;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

public class GoogleClaims {
    private String iss;
    private String sub;
    private String aud;
    private String email;
    private Boolean email_verified;
    private Long exp;
    private Long iat;
    private String name;
    private String picture;

    public static GoogleClaims from(GoogleIdToken.Payload payload) {
        GoogleClaims claims = new GoogleClaims();
        claims.iss = payload.getIssuer();
        claims.sub = payload.getSubject();
        claims.aud = payload.getAudience().toString();
        claims.email = payload.getEmail();
        claims.email_verified = payload.getEmailVerified();
        claims.exp = payload.getExpirationTimeSeconds();
        claims.iat = payload.getIssuedAtTimeSeconds();
        claims.name = (String) payload.get("name");
        claims.picture = (String) payload.get("picture");
        return claims;
    }

    public String getIss() { return iss; }
    public String getSub() { return sub; }
    public String getAud() { return aud; }
    public String getEmail() { return email; }
    public Boolean getEmailVerified() { return email_verified; }
    public Long getExp() { return exp; }
    public Long getIat() { return iat; }
    public String getName() { return name; }
    public String getPicture() { return picture; }
}

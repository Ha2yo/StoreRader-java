package com.storerader.server.auth.dto;

public class JwtClaims {
    private String sub;
    private String email;
    private Long exp;

    public JwtClaims(String sub, String email, Long exp) {
        this.sub = sub;
        this.email = email;
        this.exp = exp;
    }

    public String getSub() { return sub; }
    public String email() { return email; }
    public Long exp() { return exp; }
}

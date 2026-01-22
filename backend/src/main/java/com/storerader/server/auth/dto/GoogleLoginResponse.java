package com.storerader.server.auth.dto;

public class GoogleLoginResponse {
    private final String jwt;
    private final UserResponse user;
    private final GoogleClaims claims;

    public GoogleLoginResponse(String jwt, UserResponse user, GoogleClaims claims) {
        this.jwt = jwt;
        this.user = user;
        this.claims = claims;
    }

    public String getJwt() { return jwt; }
    public UserResponse getUser() { return user; }
    public GoogleClaims getClaims() { return claims; }

    public static class UserResponse {
        private final long id;
        private final String name;
        private final String email;
        private final String picture;

        public UserResponse(long id, String name, String email, String picture) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.picture = picture;
        }

        public long getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getPicture() { return picture; }
    }

}


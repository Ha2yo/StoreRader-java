package com.storerader.server.auth.dto;

public class GoogleLoginResponse {
    private final String accessToken;
    private final String refreshToken;
    private final UserResponse user;
    private final GoogleClaims claims;

    public GoogleLoginResponse(String accessToken, String refreshToken, UserResponse user, GoogleClaims claims) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.user = user;
        this.claims = claims;
    }

    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public UserResponse getUser() { return user; }
    public GoogleClaims getClaims() { return claims; }

    public static class UserResponse {
        private final long id;
        private final String name;
        private final String email;
        private final String picture;

        public UserResponse(
                long id,
                String name, String email, String picture) {
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


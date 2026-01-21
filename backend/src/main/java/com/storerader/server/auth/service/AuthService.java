package com.storerader.server.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.storerader.server.auth.dto.GoogleClaims;
import com.storerader.server.auth.dto.GoogleLoginRequest;
import com.storerader.server.auth.dto.GoogleLoginResponse;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService {


    // 함수 1
    public GoogleLoginResponse authGoogle(
            GoogleLoginRequest req
    ) {
        GoogleClaims claims = verifyGoogleIdToken(req.getIdToken(), req.getClientId());

        GoogleLoginResponse.UserResponse userResponse = new GoogleLoginResponse.UserResponse(
                1,
                claims.getName(),
                claims.getEmail(),
                claims.getPicture()
        );

        // 임시 JWT
        String dummyJwt = "server-jwt";

        return new GoogleLoginResponse(dummyJwt, userResponse, claims);
    }


    // 함수 2
    public GoogleClaims verifyGoogleIdToken(
            String idToken,
            String clientId
    ) {
        // 1. verifier 초기화
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                new GsonFactory()
        )
                .setAudience(Collections.singletonList(clientId))
                .build();

        try {
            // 2. 토큰 검증
            GoogleIdToken token = verifier.verify(idToken);

            if (token != null) {
                GoogleIdToken.Payload payload = token.getPayload();

                // 3. 데이터를 DTO에 매핑

                return GoogleClaims.from(token.getPayload());
            } else {
                throw new IllegalArgumentException("유효하지 않은 ID Token입니다.");
            }
        } catch (Exception e) {
            throw new RuntimeException("구글 토큰 검증 실패", e);
        }
    }



}

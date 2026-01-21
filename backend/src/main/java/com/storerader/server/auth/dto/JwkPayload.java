package com.storerader.server.auth.dto;

import java.util.List;

public class JwkPayload {
    private List<JwkKey> keys;

    public List<JwkKey> getKeys() { return keys; }
    public void setKeys(List<JwkKey> keys) { this.keys = keys; }

    public static class JwkKey {
        private String kid;
        private String n;
        private String e;

        public String getKid() { return kid; }
        public String getN() { return n; }
        public String getE() { return e; }

        public void setKid(String kid) { this.kid = kid; }
        public void setN(String n) { this.n = n; }
        public void setE(String e) { this.e = e; }
    }
}

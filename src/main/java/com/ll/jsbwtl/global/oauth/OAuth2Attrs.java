package com.ll.jsbwtl.global.oauth;

import java.util.Map;

public final class OAuth2Attrs {
    private OAuth2Attrs() {}

    public static class Profile {
        public final String providerId; // 필수
        public final String email;      // null 가능
        public final String name;       // null 가능
        public Profile(String providerId, String email, String name) {
            this.providerId = providerId; this.email = email; this.name = name;
        }
    }

    @SuppressWarnings("unchecked")
    public static Profile extract(String provider, Map<String,Object> attrs) {
        switch (provider) {
            case "google":
                return new Profile((String) attrs.get("sub"),
                        (String) attrs.get("email"),
                        (String) attrs.get("name"));
//            case "github":
//                return new Profile(String.valueOf(attrs.get("id")),
//                        (String) attrs.get("email"),
//                        (String) attrs.get("name"));
//            case "kakao": {
//                String id = String.valueOf(attrs.get("id"));
//                Map<String,Object> acc = (Map<String,Object>) attrs.get("kakao_account");
//                String email = acc != null ? (String) acc.get("email") : null;
//                String name = null;
//                if (acc != null) {
//                    Map<String,Object> prof = (Map<String,Object>) acc.get("profile");
//                    if (prof != null) name = (String) prof.get("nickname");
//                }
//                return new Profile(id, email, name);
//            }
//            case "naver": {
//                Map<String,Object> r = (Map<String,Object>) attrs.get("response");
//                return new Profile((String) r.get("id"),
//                        (String) r.get("email"),
//                        (String) r.get("name"));
//            }
            default:
                throw new IllegalArgumentException("Unsupported provider: " + provider);
        }
    }
}

package co.kr.cocomu.common.security;

import java.util.List;

public class SecurityPathConfig {

    public static final List<String> PUBLIC_START_URIS = List.of(
        "/h2-console",
        "/api/v1/auth",
        "/actuator"
    );

    public static final List<String> ANONYMOUS_URIS = List.of(
        "/api/v1/studies"
    );

}
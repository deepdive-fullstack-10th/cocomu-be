package co.kr.cocomu.common.security;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityPathConfig {

    public static final List<String> PUBLIC_START_URIS = List.of(
        "/h2-console",
        "/api/v1/auth",
        "/actuator"
    );

    public static final List<String> PUBLIC_URIS = List.of(
        "/api/v1/studies/languages",
        "/api/v1/studies/workbooks"
    );

    public static final List<String> ANONYMOUS_URIS = List.of(
        "/api/v1/studies"
    );

    public static final List<String> ANONYMOUS_END_URIS = List.of(
        "/studyInfo"
    );

    public static boolean isAnonymousUri(final String path) {
        return ANONYMOUS_URIS.contains(path)
            || ANONYMOUS_END_URIS.stream().anyMatch(path::endsWith);
    }

    public static boolean isPublicUri(final String path) {
        return PUBLIC_URIS.contains(path) ||
            PUBLIC_START_URIS.stream().anyMatch(path::startsWith);
    }

}
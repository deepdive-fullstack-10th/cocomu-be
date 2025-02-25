package co.kr.cocomu.common.admin;

import java.util.List;
import java.util.stream.Stream;

public class AdminConstants {

    public static final String SWAGGER_ROLE = "SWAGGER_ADMIN";

    public static final List<String> SWAGGER_URIS = List.of(
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/swagger-ui.html",
        "/swagger-resources/**",
        "/webjars/**"
    );

    public static String[] getAllAdminUris() {
        return Stream.of(SWAGGER_URIS) // 새로운 관리자 URI 그룹이 추가되면 여기에 포함
            .flatMap(List::stream)
            .toArray(String[]::new);
    }

}
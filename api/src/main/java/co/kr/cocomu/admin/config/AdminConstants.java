package co.kr.cocomu.admin.config;

import java.util.List;
import java.util.stream.Stream;

public class AdminConstants {

    public static final String ADMIN_ROLE = "ADMIN";

    public static final List<String> ADMIN_URIS = List.of(
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/swagger-ui.html",
        "/swagger-resources/**",
        "/webjars/**",
        "/api/v1/admin/**"
    );

    public static String[] getAllAdminUris() {
        return Stream.of(ADMIN_URIS) // 새로운 관리자 URI 그룹이 추가되면 여기에 포함
            .flatMap(List::stream)
            .toArray(String[]::new);
    }

}
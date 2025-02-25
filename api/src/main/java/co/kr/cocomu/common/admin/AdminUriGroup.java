package co.kr.cocomu.common.admin;

import static co.kr.cocomu.common.admin.AdminConstants.SWAGGER_ROLE;
import static co.kr.cocomu.common.admin.AdminConstants.SWAGGER_URIS;

import java.util.List;

public record AdminUriGroup(List<String> uris, String requiredRole) {

    public String[] getUrisArray() {
        return uris.toArray(String[]::new);
    }

    public static List<AdminUriGroup> getAdminUriGroups() {
        return List.of(new AdminUriGroup(SWAGGER_URIS, SWAGGER_ROLE)); // 새로운 그룹 추가
    }

}

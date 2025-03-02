package co.kr.cocomu.admin.config;

import static co.kr.cocomu.admin.config.AdminConstants.ADMIN_ROLE;
import static co.kr.cocomu.admin.config.AdminConstants.ADMIN_URIS;

import java.util.List;

public record AdminUriGroup(List<String> uris, String requiredRole) {

    public String[] getUrisArray() {
        return uris.toArray(String[]::new);
    }

    public static List<AdminUriGroup> getAdminUriGroups() {
        return List.of(new AdminUriGroup(ADMIN_URIS, ADMIN_ROLE)); // 새로운 그룹 추가
    }

}

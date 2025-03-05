package co.kr.cocomu.codingspace.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Schema(description = "코딩 스페이스 목록 응답")
public record CodingSpacesDto(
    @Schema(description = "코딩 스페이스 목록")
    List<CodingSpaceDto> codingSpaces,
    @Schema(description = "코딩 스페이스 마지막 조회 식별자", example = "10")
    Long lastId
) {

    public static CodingSpacesDto of(
        final List<CodingSpaceDto> codingSpaces,
        final Map<Long, List<UserDto>> usersBySpace
    ) {
        if (codingSpaces.isEmpty()) {
            return new CodingSpacesDto(codingSpaces, 0L);
        }

        for (CodingSpaceDto codingSpace : codingSpaces) {
            final List<UserDto> users = usersBySpace.getOrDefault(codingSpace.getId(), new ArrayList<>());
            codingSpace.setCurrentUsers(users);
        }
        return new CodingSpacesDto(codingSpaces, codingSpaces.getLast().getId());
    }

}

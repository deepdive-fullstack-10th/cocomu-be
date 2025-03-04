package co.kr.cocomu.codingspace.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record CodingSpacesDto(
    List<CodingSpaceDto> codingSpaces,
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

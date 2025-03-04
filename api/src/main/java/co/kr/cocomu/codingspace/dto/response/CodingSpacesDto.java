package co.kr.cocomu.codingspace.dto.response;

import java.util.List;

public record CodingSpacesDto(
    List<CodingSpaceDto> codingSpaces,
    Long lastId
) {

    public static CodingSpacesDto from(final List<CodingSpaceDto> codingSpaces) {
        if (codingSpaces.isEmpty()) {
            return new CodingSpacesDto(codingSpaces, 0L);
        }

        return new CodingSpacesDto(codingSpaces, codingSpaces.getLast().getId());
    }

}

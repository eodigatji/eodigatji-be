package eodigatji.eodigatjiserver.location.dto;

import eodigatji.eodigatjiserver.location.entity.LocationEntity;
import java.time.LocalDateTime;

public record LocationResponse(
        Long id,
        String name,
        String detail,
        String number,
        LocalDateTime createdAt
) {
    public static LocationResponse from(LocationEntity location) {
        return new LocationResponse(
                location.getId(),
                location.getName(),
                location.getDetail(),
                location.getNumber(),
                location.getCreatedAt()
        );
    }
}

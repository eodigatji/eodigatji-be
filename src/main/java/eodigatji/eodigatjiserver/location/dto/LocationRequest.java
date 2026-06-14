package eodigatji.eodigatjiserver.location.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LocationRequest(
        @NotBlank(message = "보관장소 이름은 필수입니다.")
        @Size(max = 100, message = "보관장소 이름은 100자 이하여야 합니다.")
        String name,

        @NotBlank(message = "보관장소 상세 정보는 필수입니다.")
        @Size(max = 255, message = "보관장소 상세 정보는 255자 이하여야 합니다.")
        String detail,

        @NotBlank(message = "보관장소 번호는 필수입니다.")
        @Size(max = 20, message = "보관장소 번호는 20자 이하여야 합니다.")
        String number,

        @DecimalMin(value = "-90.0", message = "위도는 -90 이상이어야 합니다.")
        @DecimalMax(value = "90.0", message = "위도는 90 이하여야 합니다.")
        Double latitude,

        @DecimalMin(value = "-180.0", message = "경도는 -180 이상이어야 합니다.")
        @DecimalMax(value = "180.0", message = "경도는 180 이하여야 합니다.")
        Double longitude
) {
}

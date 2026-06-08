package eodigatji.eodigatjiserver.location.dto;

import jakarta.validation.constraints.Size;

public record LocationPatchRequest(
        @Size(max = 100, message = "보관장소 이름은 100자 이하여야 합니다.")
        String name,

        @Size(max = 255, message = "보관장소 상세 정보는 255자 이하여야 합니다.")
        String detail,

        @Size(max = 20, message = "보관장소 번호는 20자 이하여야 합니다.")
        String number
) {
}

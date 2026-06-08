package eodigatji.eodigatjiserver.location.service;

import eodigatji.eodigatjiserver.location.dto.LocationPatchRequest;
import eodigatji.eodigatjiserver.location.dto.LocationRequest;
import eodigatji.eodigatjiserver.location.dto.LocationResponse;
import eodigatji.eodigatjiserver.location.entity.LocationEntity;
import eodigatji.eodigatjiserver.location.exception.LocationNotFoundException;
import eodigatji.eodigatjiserver.location.repository.LocationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocationService {

    private final LocationRepository locationRepository;

    public List<LocationResponse> getLocations() {
        return locationRepository.findAllByOrderByIdDesc().stream()
                .map(LocationResponse::from)
                .toList();
    }

    public LocationResponse getLocation(Long id) {
        return LocationResponse.from(getLocationEntity(id));
    }

    @Transactional
    public LocationResponse createLocation(LocationRequest request) {
        LocationEntity location = LocationEntity.builder()
                .name(request.name())
                .detail(request.detail())
                .number(request.number())
                .build();

        return LocationResponse.from(locationRepository.save(location));
    }

    @Transactional
    public LocationResponse updateLocation(Long id, LocationPatchRequest request) {
        LocationEntity location = getLocationEntity(id);

        if (request.name() == null && request.detail() == null && request.number() == null) {
            throw new IllegalArgumentException("수정할 값이 없습니다.");
        }

        if (request.name() != null) {
            location.updateName(validateNotBlank(request.name(), "보관장소 이름은 비어 있을 수 없습니다."));
        }

        if (request.detail() != null) {
            location.updateDetail(validateNotBlank(request.detail(), "보관장소 상세 정보는 비어 있을 수 없습니다."));
        }

        if (request.number() != null) {
            location.updateNumber(validateNotBlank(request.number(), "보관장소 번호는 비어 있을 수 없습니다."));
        }

        return LocationResponse.from(location);
    }

    @Transactional
    public void deleteLocation(Long id) {
        LocationEntity location = getLocationEntity(id);
        locationRepository.delete(location);
    }

    private LocationEntity getLocationEntity(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new LocationNotFoundException(id));
    }

    private String validateNotBlank(String value, String message) {
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }
}

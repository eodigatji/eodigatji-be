package eodigatji.eodigatjiserver.location.controller;

import eodigatji.eodigatjiserver.location.dto.LocationPatchRequest;
import eodigatji.eodigatjiserver.location.dto.LocationRequest;
import eodigatji.eodigatjiserver.location.dto.LocationResponse;
import eodigatji.eodigatjiserver.location.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Locations", description = "보관장소 API")
@RequiredArgsConstructor
@RequestMapping("/v1/locations")
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    @Operation(summary = "보관장소 목록 조회")
    public ResponseEntity<List<LocationResponse>> getLocations() {
        return ResponseEntity.ok(locationService.getLocations());
    }

    @GetMapping("/{id}")
    @Operation(summary = "보관장소 상세 조회")
    public ResponseEntity<LocationResponse> getLocation(@PathVariable Long id) {
        return ResponseEntity.ok(locationService.getLocation(id));
    }

    @PostMapping
    @Operation(summary = "보관장소 등록")
    public ResponseEntity<LocationResponse> createLocation(@Valid @RequestBody LocationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(locationService.createLocation(request));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "보관장소 수정")
    public ResponseEntity<LocationResponse> updateLocation(@PathVariable Long id,
                                                           @Valid @RequestBody LocationPatchRequest request) {
        return ResponseEntity.ok(locationService.updateLocation(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "보관장소 삭제")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}

package eodigatji.eodigatjiserver.location.repository;

import eodigatji.eodigatjiserver.location.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
}

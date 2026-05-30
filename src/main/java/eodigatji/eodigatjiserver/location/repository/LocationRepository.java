package eodigatji.eodigatjiserver.location.repository;

import eodigatji.eodigatjiserver.location.entity.LocationEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
    List<LocationEntity> findAllByOrderByIdDesc();
}

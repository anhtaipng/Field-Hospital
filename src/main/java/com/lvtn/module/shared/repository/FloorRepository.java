package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.model.Building;
import com.lvtn.module.shared.model.Floor;
import com.lvtn.module.shared.model.FloorPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FloorRepository extends JpaRepository<Floor, FloorPK> {
    List<Floor> findByBuilding_BuildingName(String buildingName);
}

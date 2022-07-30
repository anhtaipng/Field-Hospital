package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.model.Floor;
import com.lvtn.module.shared.model.FloorPK;
import com.lvtn.module.shared.model.Room;
import com.lvtn.module.shared.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomTypeRepository extends JpaRepository<RoomType, String> {
}

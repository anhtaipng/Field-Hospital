package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.model.Otp;
import com.lvtn.module.shared.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpRepository extends JpaRepository<Otp, String> {
}

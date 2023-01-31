package com.udteam.miristock.repository;

import com.udteam.miristock.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity,Long> {
    RefreshTokenEntity findByEmail(String email);

}

package com.udteam.miristock.repository;

import com.udteam.miristock.entity.RedisEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Component
public interface RedisRepository extends CrudRepository<RedisEntity,String> {

    Optional<RedisEntity> findByValue(String value);
}

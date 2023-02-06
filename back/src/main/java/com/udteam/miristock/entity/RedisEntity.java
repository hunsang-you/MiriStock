package com.udteam.miristock.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@RedisHash("RedisEntity")
@Getter
@AllArgsConstructor
@Builder
public class RedisEntity {

    @Id
    @Indexed
    private String id;

    @TimeToLive
    private Long expire;
}

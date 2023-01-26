package com.udteam.miristock.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ROLE_MEMBER,
    ROLE_ADMIN,
    ROLE_GUEST;
    private String key;
}

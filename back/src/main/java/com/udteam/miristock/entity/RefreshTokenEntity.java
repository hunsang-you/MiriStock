package com.udteam.miristock.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name="refreshtoken")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refreshtoken_no")
    private long refreshtokenNo;

    @Column(name = "email")
    private String email;

    @Column(name = "refreshtoken")
    private String refreshToken;

}

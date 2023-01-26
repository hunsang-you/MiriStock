package com.udteam.miristock.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Getter
@Builder
@NoArgsConstructor
public class MemberEntity {

    @Id
    @Column(name="member_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNo;

    @Column(name="member_email",nullable = false,length = 320)
    private String memberEmail;

    @Column(name="member_nickname",nullable= false, length = 20)
    private String memberNickname;

    @Column(name="member_totalasset")
    @ColumnDefault("50000000")
    private Long memberTotalasset;

    @Column(name="member_current_tile",nullable=false)
    private int memberCurrentTime;

    private Role role;

}

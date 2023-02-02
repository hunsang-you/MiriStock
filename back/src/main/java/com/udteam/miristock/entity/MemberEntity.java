package com.udteam.miristock.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name="member")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberEntity {

    @Id
    @Column(name="member_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer memberNo;

    @Column(name="member_email",nullable = false,length = 320)
    private String memberEmail;

    @Column(name="member_nickname", length = 20)
    private String memberNickname;

    @Column(name="member_totalasset")
    @ColumnDefault("50000000")
    private Long memberTotalasset;

    @Column(name="member_current_time",nullable=false)
    private Integer memberCurrentTime;

    @Column(name="provider",nullable=false)
    private String memberProvider;

    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private Role role;

}

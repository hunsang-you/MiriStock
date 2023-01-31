package com.udteam.miristock.entity;

import lombok.*;

import javax.persistence.*;

@Entity(name="memberasset")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberAssetEntity {

    @Id
    @Column(name="memberasset_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int memberassetNo;

    @OneToOne
    @JoinColumn(name="member_no")
    private MemberEntity member;

    @Column(name="memberasset_total_asset")
    private long memberassetTotalAsset;

    @Column(name = "memberasset_available_asset")
    private long memberassetAvailableAsset;

    @Column(name = "memberasset_stock_asset")
    private long memberassetStockAsset;

}

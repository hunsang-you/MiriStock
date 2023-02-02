package com.udteam.miristock.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="memberasset")
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
    private Integer memberassetNo;

    @OneToOne
    @JoinColumn(name="member_no")
    private MemberEntity member;

    @Column(name="memberasset_total_asset")
    private Long memberassetTotalAsset;

    @Column(name = "memberasset_available_asset")
    private Long memberassetAvailableAsset;

    @Column(name = "memberasset_stock_asset")
    private Long memberassetStockAsset;

}

package com.udteam.miristock.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name="memberasset")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@DynamicInsert
@DynamicUpdate
public class MemberAssetEntity {

    @Id
    @Column(name="memberasset_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer memberassetNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_no")
    private MemberEntity member;

    @Column(name="memberasset_total_asset")
    private Long memberassetTotalAsset;

    @Column(name = "memberasset_available_asset")
    private Long memberassetAvailableAsset;

    @Column(name = "memberasset_stock_asset")
    private Long memberassetStockAsset;

    @Column(name = "memberasset_current_time")
    private Integer memberassetCurrentTime;

    @Column(name = "memberasset_last_total_asset")
    private Long memberassetLastTotalAsset;
}

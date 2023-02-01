package com.udteam.miristock.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="interest")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InterestEntity {

    @Id
    @Column(name ="interest_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int interestNo;

    @ManyToOne
    @JoinColumn(name="stock_code")
    private StockEntity stock;

    @ManyToOne
    @JoinColumn(name = "member_no")
    private MemberEntity member;

}

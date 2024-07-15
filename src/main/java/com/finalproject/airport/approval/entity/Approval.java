package com.finalproject.airport.approval.entity;

import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaim;
import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounter;
import com.finalproject.airport.airplane.gate.entity.Gate;
import com.finalproject.airport.store.entity.StoreEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "TBL_APPROVAL")
@AllArgsConstructor
@Getter
@ToString
public class Approval {

    @Id
    @Column(name = "APPROVAL_CODE" ,nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int approvalCode;

    @Column(name = "APPROVAL_TYPE")
    @Enumerated(EnumType.STRING)
    private ApprovalType approvalType;

    @Column(name = "APPROVAL_STATUS")
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    @ManyToOne
    @JoinColumn(name = "GATE_CODE")
    private Gate gate;

    @ManyToOne
    @JoinColumn(name = "CHECKINCOUNTER_CODE")
    private CheckinCounter checkinCounter;

    @ManyToOne
    @JoinColumn(name = "BAGGAGECLAIM_CODE")
    private BaggageClaim baggageClaim;

    @ManyToOne
    @JoinColumn(name = "STORE_CODE")
    private StoreEntity store;





    protected Approval(){}
}

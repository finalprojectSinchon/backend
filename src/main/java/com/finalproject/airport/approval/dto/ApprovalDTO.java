package com.finalproject.airport.approval.dto;

import com.finalproject.airport.airplane.baggageclaim.dto.BaggageClaimDTO;
import com.finalproject.airport.airplane.checkincounter.dto.CheckinCounterDTO;
import com.finalproject.airport.airplane.gate.dto.GateDTO;
import com.finalproject.airport.facilities.dto.FacilitiesDTO;
import com.finalproject.airport.storage.dto.StorageDTO;
import com.finalproject.airport.store.dto.StoreDTO;
import com.finalproject.airport.approval.entity.ApprovalTypeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Schema(description = "승인 작업을 위한 데이터 전송 객체")
public class ApprovalDTO {

    @Schema(description = "승인 코드", example = "123")
    private int approvalCode;

    @Schema(description = "승인 유형", example = "등록", allowableValues = {"등록", "수정"})
    private ApprovalTypeEntity type;

    @Schema(description = "승인 상태", example = "승인됨")
    private String status;

    @Schema(description = "게이트 DTO")
    private GateDTO gateDTO;

    @Schema(description = "체크인 카운터 DTO")
    private CheckinCounterDTO checkinCounterDTO;

    @Schema(description = "수하물 수취대 DTO")
    private BaggageClaimDTO baggageClaimDTO;

    @Schema(description = "점포 DTO")
    private StoreDTO storeDTO;

    @Schema(description = "창고 DTO")
    private StorageDTO storageDTO;

    @Schema(description = "편의시설 DTO")
    private FacilitiesDTO facilitiesDTO;

    @Schema(description = "게이트 코드", example = "101")
    private Integer gateCode;

    @Schema(description = "체크인 카운터 코드", example = "202")
    private Integer checkinCounterCode;

    @Schema(description = "수하물 수취대 코드", example = "303")
    private Integer baggageClaimCode;

    @Schema(description = "창고 코드", example = "404")
    private Integer storageCode;

    @Schema(description = "편의시설 코드", example = "505")
    private Integer facilitiesCode;

    @Schema(description = "기타 코드", example = "606")
    private Integer code;

    @Schema(description = "생성 날짜")
    private Date createdDate;

    @Schema(description = "수정 날짜")
    private Date modifiedDate;

    @Schema(description = "검토 여부", example = "검토 완료")
    private String checked;

    @Schema(description = "알림 클릭 여부")
    private String noti;
}

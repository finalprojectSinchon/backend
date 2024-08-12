package com.finalproject.airport.airplane.checkincounter.dto;

import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounterLocation;
import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounterType;
import com.finalproject.airport.member.dto.UserDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Schema(description = "체크인 카운터 관련 DTO")
public class CheckinCounterDTO {

    @Schema(description = "체크인 카운터 정보(PK)")
    private int checkinCounterCode;

    @Schema(description = "체크인 카운터 위치")
    private CheckinCounterLocation location;

    @Schema(description = "체크인 카운터 타입")
    private CheckinCounterType type;

    @Schema(description = "체크인 카운터 상태(정상, 고장, 점검중)")
    private String status;

    @Schema(description = "체크인 카운터 등록 날짜")
    private Date registrationDate;

    @Schema(description = "최근 점검 날짜")
    private Date lastInspectionDate;

    @Schema(description = "담당자")
    private String manager;

    @Schema(description = "비고")
    private String note;

    @Schema(description = "비행기 코드")
    private int airplaneCode;

    @Schema(description = "지연 시간")
    private Integer delayTime;

    @Schema(description = "탑승구 오픈시간")
    private Timestamp scheduleDateTime;

    @Schema(description = "항공사")
    private String airline;

    @Schema(description = "도착공항")
    private String airport;

    @Schema(description = "편명")
    private String flightid;

    @Schema(description = "운항상태")
    private String remark;
    // 정적 메서드로 수정

    @Schema(description = "승인요청자")
    private UserDTO approvalRequester;
}

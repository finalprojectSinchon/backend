package com.finalproject.airport.approval.controller;

import com.finalproject.airport.approval.dto.ApprovalDTO;
import com.finalproject.airport.approval.entity.ApprovalEntity;
import com.finalproject.airport.approval.service.ApprovalService;
import com.finalproject.airport.common.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "승인 관리", description = "승인 관련 작업을 수행합니다.")
public class ApprovalController {

    private final ApprovalService approvalService;

    @Autowired
    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @Operation(summary = "모든 승인 조회", description = "모든 승인의 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "승인 목록을 성공적으로 조회",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    @GetMapping("/approve")
    public ResponseEntity<ResponseDTO> getApproval() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        List<ApprovalEntity> approvalEntityList = approvalService.findAll();
        Map<String, Object> approvalMap = new HashMap<>();
        approvalMap.put("approvalList", approvalEntityList);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseDTO(HttpStatus.OK, "승인 전체 조회 성공", approvalMap));
    }

    @Operation(summary = "승인 처리", description = "승인 코드를 사용하여 특정 승인 작업을 처리합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "승인 처리가 성공적으로 완료됨",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (승인 코드가 누락됨)"),
            @ApiResponse(responseCode = "500", description = "승인 처리 중 서버 오류 발생")
    })
    @PutMapping("/approve/{approvalCode}")
    public ResponseEntity<ResponseDTO> approve(
            @Parameter(description = "승인 처리할 코드", required = true) @PathVariable Integer approvalCode) {
        if (approvalCode == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(HttpStatus.BAD_REQUEST, "승인 코드가 누락되었습니다.", null));
        }

        boolean gateApproved = false;
        boolean checkInCounterApproved = false;
        boolean baggageClaimApproved = false;
        boolean storageApproved = false;
        boolean facilitiesApproved = false;
        boolean storeApproved = false;
        String errorMessage = null;

        ApprovalEntity approval = approvalService.approveCommon(approvalCode);

        if ("수정".equals(approval.getType().toString())) {
            try {
                if (approval.getGate() != null) {
                    approvalService.approveGate(approvalCode);
                    gateApproved = true;
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                errorMessage = e.getMessage();
            }

            try {
                if (approval.getCheckinCounter() != null) {
                    approvalService.approveCheckInCounter(approvalCode);
                    checkInCounterApproved = true;
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                errorMessage = e.getMessage();
            }

            try {
                if (approval.getBaggageClaim() != null) {
                    approvalService.approveBaggageClaim(approvalCode);
                    baggageClaimApproved = true;
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                errorMessage = e.getMessage();
            }

            try {
                if (approval.getStorage() != null) {
                    approvalService.approveStorage(approvalCode);
                    storageApproved = true;
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                errorMessage = e.getMessage();
            }

            try {
                if (approval.getStore() != null) {
                    approvalService.approveStore(approvalCode);
                    storeApproved = true;
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                errorMessage = e.getMessage();
            }

            try {
                if (approval.getFacilities() != null) {
                    approvalService.approveFacilities(approvalCode);
                    facilitiesApproved = true;
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                errorMessage = e.getMessage();
            }
        } else if ("등록".equals(approval.getType().toString())) {
            try {
                if (approval.getGate() != null) {
                    approvalService.saveGateApproval(approval);
                    gateApproved = true;
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                errorMessage = e.getMessage();
            }

            try {
                if (approval.getCheckinCounter() != null) {
                    approvalService.saveChkinCounterApproval(approval);
                    checkInCounterApproved = true;
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                errorMessage = e.getMessage();
            }

            try {
                if (approval.getBaggageClaim() != null) {
                    approvalService.saveBaggageClaimApproval(approval);
                    baggageClaimApproved = true;
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                errorMessage = e.getMessage();
            }

            try {
                if (approval.getStorage() != null) {
                    approvalService.saveStorageApproval(approval);
                    storageApproved = true;
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                errorMessage = e.getMessage();
            }

            try {
                if (approval.getFacilities() != null) {
                    approvalService.saveFacilities(approval);
                    facilitiesApproved = true;
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                errorMessage = e.getMessage();
            }
        }

        boolean anyApproved = gateApproved || checkInCounterApproved || baggageClaimApproved || storageApproved || facilitiesApproved || storeApproved;
        HttpStatus status = anyApproved ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
        String message = anyApproved ? "승인 처리된 시설물은 다음과 같습니다: " : "승인 처리 실패";

        if (gateApproved) message += " (Gate)";
        if (checkInCounterApproved) message += " (Check-in Counter)";
        if (baggageClaimApproved) message += " (Baggage Claim)";
        if (storageApproved) message += " (Storage)";
        if (facilitiesApproved) message += " (Facilities)";
        if (storeApproved) message += " (Store)";

        return ResponseEntity.status(status)
                .body(new ResponseDTO(status, message, errorMessage));
    }

    //    @GetMapping("/noti-checked")
//    public void notiChecked(@RequestParam("approveCode") String approveCode){
//        System.out.println("approveCode = " + approveCode);
//        approvalService.notiChecked(Integer.parseInt(approveCode));
//
//    }
//
//}

}







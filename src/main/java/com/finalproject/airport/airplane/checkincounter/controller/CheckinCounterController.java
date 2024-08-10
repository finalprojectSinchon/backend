package com.finalproject.airport.airplane.checkincounter.controller;

import com.finalproject.airport.airplane.checkincounter.dto.CheckinCounterDTO;
import com.finalproject.airport.airplane.checkincounter.service.CheckinCounterService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/airplane")
@Tag(name = "체크인 카운터 관리", description = "체크인 카운터와 관련된 API 관리")
public class CheckinCounterController {

    private final CheckinCounterService service;

    @Autowired
    public CheckinCounterController(CheckinCounterService service) {
        this.service = service;
    }

    @Operation(
            summary = "체크인 카운터 등록",
            description = "새로운 체크인 카운터를 등록합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "체크인 카운터 데이터",
                    content = @Content(
                            schema = @Schema(implementation = CheckinCounterDTO.class)
                    ),
                    required = true
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "체크인 카운터 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력")
    })
    @PostMapping("/checkin-counter")
    public ResponseEntity<Void> insertChkinCounter(@RequestBody CheckinCounterDTO chkinCounter) {
        service.insertchkinCounter(chkinCounter);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "모든 체크인 카운터 조회",
            description = "모든 체크인 카운터의 목록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "체크인 카운터 조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = ResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "체크인 카운터 없음")
    })
    @GetMapping("/checkin-counter")
    public ResponseEntity<ResponseDTO> getChkinCounter() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<CheckinCounterDTO> checkinCounterdata = service.feachdate();
        List<CheckinCounterDTO> chkinCounterList = service.findAll();
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("chkinCounterList", chkinCounterList);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseDTO(HttpStatus.OK, "체크인 카운터 전체 조회", responseMap));
    }

    @Operation(
            summary = "체크인 카운터 상세 조회",
            description = "특정 체크인 카운터의 세부 정보를 조회합니다.",
            parameters = @Parameter(name = "checkinCounterCode", description = "조회할 체크인 카운터 코드", required = true)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "체크인 카운터 조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = ResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "체크인 카운터 없음")
    })
    @GetMapping("/checkin-counter/{checkinCounterCode}")
    public ResponseEntity<ResponseDTO> chkinCounterDetail(
            @PathVariable int checkinCounterCode) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        CheckinCounterDTO chkinCounter = service.findByCheckinCounterCode(checkinCounterCode);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("chkinCounter", chkinCounter);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseDTO(HttpStatus.OK, "체크인 카운터 조회", responseMap));
    }

    @Operation(
            summary = "체크인 카운터 수정",
            description = "특정 체크인 카운터의 세부 정보를 수정합니다.",
            parameters = @Parameter(name = "checkinCounterCode", description = "수정할 체크인 카운터 코드", required = true)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "체크인 카운터 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력")
    })
    @PutMapping("/checkin-counter/{checkinCounterCode}")
    public ResponseEntity<String> modifyCheckinCounter(
            @PathVariable int checkinCounterCode,
            @RequestBody CheckinCounterDTO checkinCounterDTO
    ) {
        try {
            String resultMessage = service.modifyCheckinCounter(checkinCounterDTO);
            return ResponseEntity.ok(resultMessage);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(
            summary = "체크인 카운터 소프트 삭제",
            description = "특정 체크인 카운터를 소프트 삭제합니다.",
            parameters = @Parameter(name = "checkinCounterCode", description = "소프트 삭제할 체크인 카운터 코드", required = true)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "체크인 카운터 소프트 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "체크인 카운터 없음")
    })
    @PutMapping("/checkin-counter/{checkinCounterCode}/delete")
    public ResponseEntity<Void> removeCheckinCounter(
            @PathVariable int checkinCounterCode) {

        service.removeCheckinCounter(checkinCounterCode);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/checkin/test")
    public void test(){
        service.insertdb();
    }
}

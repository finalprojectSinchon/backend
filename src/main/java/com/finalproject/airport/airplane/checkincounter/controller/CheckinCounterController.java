package com.finalproject.airport.airplane.checkincounter.controller;

import com.finalproject.airport.airplane.checkincounter.dto.CheckinCounterDTO;
import com.finalproject.airport.airplane.checkincounter.service.CheckinCounterService;
import com.finalproject.airport.common.ResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/airplane")
@Api(tags = "체크인 카운터 관리")
public class CheckinCounterController {

    private final CheckinCounterService service;

    @Autowired
    public CheckinCounterController(CheckinCounterService service) {
        this.service = service;
    }

    @ApiOperation(value = "체크인 카운터 등록", notes = "새로운 체크인 카운터를 등록합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "체크인 카운터 등록 성공"),
            @ApiResponse(code = 400, message = "잘못된 입력")
    })
    @PostMapping("/checkin-counter")
    public ResponseEntity<?> insertchkinCounter(@ModelAttribute CheckinCounterDTO chkinCounter) {
        service.insertchkinCounter(chkinCounter);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "모든 체크인 카운터 조회", notes = "모든 체크인 카운터의 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "체크인 카운터 조회 성공"),
            @ApiResponse(code = 404, message = "체크인 카운터 없음")
    })
    @GetMapping("/checkin-counter")
    public ResponseEntity<ResponseDTO> getChkinCounter() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        List<CheckinCounterDTO> chkinCounterList = service.findAll();
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("chkinCounterList", chkinCounterList);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseDTO(HttpStatus.OK, "체크인 카운터 전체 조회", responseMap));
    }

    @ApiOperation(value = "체크인 카운터 상세 조회", notes = "특정 체크인 카운터의 세부 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "체크인 카운터 조회 성공"),
            @ApiResponse(code = 404, message = "체크인 카운터 없음")
    })
    @GetMapping("/checkin-counter/{checkinCounterCode}")
    public ResponseEntity<ResponseDTO> chkinCounterDetail(
            @ApiParam(value = "조회할 체크인 카운터 코드", required = true)
            @PathVariable int checkinCounterCode) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        CheckinCounterDTO chkinCounter = service.findBycheckinCounterCode(checkinCounterCode);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("chkinCounter", chkinCounter);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseDTO(HttpStatus.OK, "체크인 카운터 조회", responseMap));
    }

    @ApiOperation(value = "체크인 카운터 수정", notes = "특정 체크인 카운터의 세부 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "체크인 카운터 수정 성공"),
            @ApiResponse(code = 400, message = "잘못된 입력")
    })
    @PutMapping("/checkin-counter/{checkinCounterCode}")
    public ResponseEntity<?> modifyCheckinCounter(
            @ApiParam(value = "수정할 체크인 카운터 코드", required = true)
            @PathVariable int checkinCounterCode,
            @RequestBody CheckinCounterDTO modifyCheckinCounter) {

        service.modifyCheckinCounter(checkinCounterCode, modifyCheckinCounter);
        return ResponseEntity.created(URI.create("/checkin-counter/" + checkinCounterCode)).build();
    }

    @ApiOperation(value = "체크인 카운터 소프트 삭제", notes = "특정 체크인 카운터를 소프트 삭제합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "체크인 카운터 소프트 삭제 성공"),
            @ApiResponse(code = 404, message = "체크인 카운터 없음")
    })
    @PutMapping("/checkin-counter/{checkinCounterCode}/delete")
    public ResponseEntity<?> remodveCheckinCounter(
            @ApiParam(value = "소프트 삭제할 체크인 카운터 코드", required = true)
            @PathVariable int checkinCounterCode) {

        service.remodveCheckinCounter(checkinCounterCode);
        return ResponseEntity.ok().build();
    }
}

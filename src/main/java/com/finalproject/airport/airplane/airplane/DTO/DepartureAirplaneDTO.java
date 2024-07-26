package com.finalproject.airport.airplane.airplane.DTO;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Schema(description = "출발 비행기 관련 DTO")
public class DepartureAirplaneDTO {

    @Schema(description = "Response 객체")
    private Response response;

    @Schema(description = "Response 내부 클래스")
    public static class Response {
        @Schema(description = "Header 객체")
        private Header header;
        @Schema(description = "Body 객체")
        private Body body;

        @Schema(description = "Header 내부 클래스")
        public static class Header {
            @Schema(description = "결과 코드", example = "00")
            private String resultCode;
            @Schema(description = "결과 메시지", example = "Success")
            private String resultMsg;
        }

        @Schema(description = "Body 내부 클래스")
        public static class Body {
            @Schema(description = "한 페이지당 항목 수", example = "10")
            private int numOfRows;
            @Schema(description = "페이지 번호", example = "1")
            private int pageNo;
            @Schema(description = "전체 항목 수", example = "100")
            private int totalCount;
            @ArraySchema(schema = @Schema(description = "항목 리스트", implementation = Item.class))
            private List<Item> items;

            @Schema(description = "Item 내부 클래스")
            public static class Item {
                @Schema(description = "항공사", example = "Korean Air")
                private String airline;
                @Schema(description = "항공편 ID", example = "KE123")
                private String flightId;
                @Schema(description = "예정 출발 시간", example = "2024-07-26T12:34:56")
                private String scheduleDateTime;
                @Schema(description = "예상 출발 시간", example = "2024-07-26T12:45:00")
                private String estimatedDateTime;
                @Schema(description = "공항", example = "Incheon International Airport")
                private String airport;
                @Schema(description = "체크인 범위", example = "08:00-10:00")
                private String chkinrange;
                @Schema(description = "게이트 번호", example = "A12")
                private String gatenumber;
                @Schema(description = "코드쉐어", example = "OZ123")
                private String codeshare;
                @Schema(description = "마스터 항공편 ID", example = "KE123")
                private String masterflightid;
                @Schema(description = "비고", example = "On Time")
                private String remark;
                @Schema(description = "공항 코드", example = "ICN")
                private String airportCode;
                @Schema(description = "터미널 ID", example = "1")
                private String terminalid;
                @Schema(description = "비행 유형", example = "International")
                private String typeOfFlight;
                @Schema(description = "FID", example = "12345")
                private String fid;
                @Schema(description = "주기 위치", example = "SP01")
                private String fstandposition;
            }
        }
    }
}

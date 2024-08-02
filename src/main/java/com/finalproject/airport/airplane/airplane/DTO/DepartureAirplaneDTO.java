package com.finalproject.airport.airplane.airplane.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import lombok.*;

import java.util.List;

@Schema(description = "출발 비행기 관련 DTO")
public class DepartureAirplaneDTO {

    @Schema(description = "Response 객체")
    private Response response;

    // Getters and setters
    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    @Schema(description = "Response 내부 클래스")
    public static class Response {
        @Schema(description = "Header 객체")
        private Header header;

        @Schema(description = "Body 객체")
        private Body body;

        // Getters and setters
        public Header getHeader() {
            return header;
        }

        public void setHeader(Header header) {
            this.header = header;
        }

        public Body getBody() {
            return body;
        }

        public void setBody(Body body) {
            this.body = body;
        }

        @Schema(description = "Header 내부 클래스")
        public static class Header {
            @Schema(description = "결과 코드", example = "00")
            private String resultCode;

            @Schema(description = "결과 메시지", example = "Success")
            private String resultMsg;

            // Getters and setters
            public String getResultCode() {
                return resultCode;
            }

            public void setResultCode(String resultCode) {
                this.resultCode = resultCode;
            }

            public String getResultMsg() {
                return resultMsg;
            }

            public void setResultMsg(String resultMsg) {
                this.resultMsg = resultMsg;
            }
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

            // Getters and setters
            public int getNumOfRows() {
                return numOfRows;
            }

            public void setNumOfRows(int numOfRows) {
                this.numOfRows = numOfRows;
            }

            public int getPageNo() {
                return pageNo;
            }

            public void setPageNo(int pageNo) {
                this.pageNo = pageNo;
            }

            public int getTotalCount() {
                return totalCount;
            }

            public void setTotalCount(int totalCount) {
                this.totalCount = totalCount;
            }

            public List<Item> getItems() {
                return items;
            }

            public void setItems(List<Item> items) {
                this.items = items;
            }

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

                @Schema(description = "체크인 카운터 위치")
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

                // Getters and setters
                public String getAirline() {
                    return airline;
                }

                public void setAirline(String airline) {
                    this.airline = airline;
                }

                public String getFlightId() {
                    return flightId;
                }

                public void setFlightId(String flightId) {
                    this.flightId = flightId;
                }

                public String getScheduleDateTime() {
                    return scheduleDateTime;
                }

                public void setScheduleDateTime(String scheduleDateTime) {
                    this.scheduleDateTime = scheduleDateTime;
                }

                public String getEstimatedDateTime() {
                    return estimatedDateTime;
                }

                public void setEstimatedDateTime(String estimatedDateTime) {
                    this.estimatedDateTime = estimatedDateTime;
                }

                public String getAirport() {
                    return airport;
                }

                public void setAirport(String airport) {
                    this.airport = airport;
                }

                public String getChkinrange() {
                    return chkinrange;
                }

                public void setChkinrange(String chkinrange) {
                    this.chkinrange = chkinrange;
                }

                public String getGatenumber() {
                    return gatenumber;
                }

                public void setGatenumber(String gatenumber) {
                    this.gatenumber = gatenumber;
                }

                public String getCodeshare() {
                    return codeshare;
                }

                public void setCodeshare(String codeshare) {
                    this.codeshare = codeshare;
                }

                public String getMasterflightid() {
                    return masterflightid;
                }

                public void setMasterflightid(String masterflightid) {
                    this.masterflightid = masterflightid;
                }

                public String getRemark() {
                    return remark;
                }

                public void setRemark(String remark) {
                    this.remark = remark;
                }

                public String getAirportCode() {
                    return airportCode;
                }

                public void setAirportCode(String airportCode) {
                    this.airportCode = airportCode;
                }

                public String getTerminalid() {
                    return terminalid;
                }

                public void setTerminalid(String terminalid) {
                    this.terminalid = terminalid;
                }

                public String getTypeOfFlight() {
                    return typeOfFlight;
                }

                public void setTypeOfFlight(String typeOfFlight) {
                    this.typeOfFlight = typeOfFlight;
                }

                public String getFid() {
                    return fid;
                }

                public void setFid(String fid) {
                    this.fid = fid;
                }

                public String getFstandposition() {
                    return fstandposition;
                }

                public void setFstandposition(String fstandposition) {
                    this.fstandposition = fstandposition;
                }
            }
        }
    }
}

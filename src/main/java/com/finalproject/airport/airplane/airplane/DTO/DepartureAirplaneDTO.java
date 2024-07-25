package com.finalproject.airport.airplane.airplane.DTO;

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

    private Response response;

    // Getters and setters
    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    // Inner classes
    public static class Response {
        private Header header;
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

        public static class Header {
            private String resultCode;
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

        public static class Body {
            private int numOfRows;
            private int pageNo;
            private int totalCount;
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

            public static class Item {
                private String airline;
                private String flightId;
                private String scheduleDateTime;
                private String estimatedDateTime;
                private String airport;
                private String chkinrange;
                private String gatenumber;
                private String codeshare;
                private String masterflightid;
                private String remark;
                private String airportCode;
                private String terminalid;
                private String typeOfFlight;
                private String fid;
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

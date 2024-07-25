package com.finalproject.airport.airplane.airplane.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;


@Schema(description = "도착 비행기 관련 DTO")
public class ArrivalAirplaneDTO {


    private Response response;

    // Getter 및 Setter
    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public static class Response {
        private Header header;
        private Body body;

        // Getter 및 Setter
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

            // Getter 및 Setter
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

            // Getter 및 Setter
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
                private String gatenumber;
                private String carousel;
                private String exitnumber;
                private String codeshare;
                private String masterflightid;
                private String remark;
                private String airportCode;
                private String terminalid;
                private String typeOfFlight;
                private String fid;
                private String fstandposition;

                // Getter 및 Setter
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

                public String getGatenumber() {
                    return gatenumber;
                }

                public void setGatenumber(String gatenumber) {
                    this.gatenumber = gatenumber;
                }

                public String getCarousel() {
                    return carousel;
                }

                public void setCarousel(String carousel) {
                    this.carousel = carousel;
                }

                public String getExitnumber() {
                    return exitnumber;
                }

                public void setExitnumber(String exitnumber) {
                    this.exitnumber = exitnumber;
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

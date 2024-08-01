package com.finalproject.airport.location.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.finalproject.airport.airplane.airplane.DTO.ArrivalAirplaneDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class LocationAPIDTO {

    private Response response;

    @Getter
    @Setter
    @ToString
    public static class Response{
       private Header header;

       private Body body;

       @Getter
       @Setter
       @ToString
       public static class Header{
           private String resultCode;

           private String resultMsg;
       }

       @Getter
       @Setter
       @ToString
       public static class Body{
           private int numOfRows;
           private int pageNo;
           private int totalCount;
           private List<Item> items;

           @Getter
           @Setter
           @ToString
           public static class Item{
               private String arrordep;

               private String entrpskoreannm;

               private String lckoreannm;

               private String servicetime;

               private String tel;

               private String trtmntprdlstkoreannm;
           }
       }
    }
}

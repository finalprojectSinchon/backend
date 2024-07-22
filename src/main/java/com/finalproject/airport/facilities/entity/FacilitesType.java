package com.finalproject.airport.facilities.entity;

public enum FacilitesType {
    엘리베이터("엘리베이터"), 에스컬레이터("에스컬레이터") , 무빙워크("무빙워크") , 락커("락커") , 흡연실("흡연실") , 샤워실("샤워실") , 남자화장실("남자화장실") , 여자화장실("여자화장실");

    private final String type;

    private FacilitesType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}

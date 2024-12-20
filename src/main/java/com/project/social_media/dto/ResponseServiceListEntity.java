package com.project.social_media.dto;

import com.project.social_media.constants.ErrorCodes;

import java.util.List;

public class ResponseServiceListEntity<T> {
    private String errorCode;
    private String message;
    private String attr1;
    private String attr2;
    public Long totalItems;
    private List<T> data;
    public ResponseServiceListEntity() {}

    public ResponseServiceListEntity(ErrorCodes errorCode) {
        this.errorCode = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public ResponseServiceListEntity(ErrorCodes errorCode, List<T> data, Long totalItems) {
        this.errorCode = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.totalItems = totalItems;
        this.data = data;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Long getTotalItems() {
        return totalItems;
    }
    public void setTotalItems(Long totalItems) {
        this.totalItems = totalItems;
    }

    public static <T> ResponseServiceListEntity<T> success(List<T> data,Long totalItems, ErrorCodes errorCodes) {
        return new ResponseServiceListEntity<>(errorCodes, data, totalItems);
    }

    public static <T> ResponseServiceListEntity<T> error(ErrorCodes errorCodes) {
        return new ResponseServiceListEntity<>(errorCodes);
    }
}

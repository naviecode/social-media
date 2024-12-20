package com.project.social_media.dto;

import com.project.social_media.constants.ErrorCodes;

public class ResponseServiceEntity<T> {
    private String errorCode;
    private String message;
    private String attr1;
    private String attr2;
    private T data;

    public ResponseServiceEntity() {}

    public ResponseServiceEntity(ErrorCodes errorCode) {
        this.errorCode = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public ResponseServiceEntity(ErrorCodes errorCode, T data) {
        this.errorCode = errorCode.getCode();
        this.message = errorCode.getMessage();
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> ResponseServiceEntity<T> success(T data, ErrorCodes errorCodes) {
        return new ResponseServiceEntity<>(errorCodes, data);
    }

    public static <T> ResponseServiceEntity<T> error(ErrorCodes errorCodes) {
        return new ResponseServiceEntity<>(errorCodes);
    }
}

package com.project.social_media.constants;

public enum ErrorCodes {
    ERROR_AUTH_EXISTS_USERNAME("101", "Username đã tồn tại"),
    ERROR_AUTH_USERNAME_NOTFOUND("102", "Username không tìm thấy"),
    ERROR_AUTH_LOGIN_FAIL("103", "Tên đăng nhập hoặc mật khẩu bị sai"),


    SUCCESS("0", "Thành công");

    private final String code;
    private final String message;

    ErrorCodes(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

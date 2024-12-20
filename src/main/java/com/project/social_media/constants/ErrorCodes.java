package com.project.social_media.constants;

public enum ErrorCodes {
    ERROR_AUTH_EXISTS_USERNAME("101", "Username đã tồn tại"),
    ERROR_AUTH_USERNAME_NOTFOUND("102", "Username không tìm thấy"),
    ERROR_AUTH_LOGIN_FAIL("103", "Tên đăng nhập hoặc mật khẩu bị sai"),

    ERROR_CHAT_MESSAGE_EXISTS("201", "Nguoi dung khong trong cuoc trong chuyen"),

    ERROR_USER_NOT_EXISTS("301", "Không tìm thấy người dùng"),

    ERROR_CHAT_ADD_GROUP_SIZE("401", "Một group phải có ít nhất 3 thành viên"),
    ERROR_CHAT_NOT_FOUND("402", "Không tìm thấy cuộc trò chuyện"),
    ERROR_CHAT_NOT_MEMBER("403", "Bạn không có trong cuộc trò chuyện"),
    ERROR_CHAT_MEMBER_NOT_REMOVE_OWNER("404", "Không được xóa chủ nhóm"),
    ERROR_CHAT_NOT_CREATED("405", "Không phải người tạo group"),

    ERROR_CHAT_MEMBER_EXISTS("501", "Người dùng đã có trong cuộc trò chuyện"),
    ERROR_CHAT_MEMBER_NOT_EXISTS_FRIEND("502", "Người dùng không có trong danh sách bạn bè"),





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

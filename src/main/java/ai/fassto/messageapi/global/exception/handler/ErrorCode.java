package ai.fassto.messageapi.global.exception.handler;

import lombok.Getter;

@Getter
public enum ErrorCode {
    BAD_REQUEST_EXCEPTION(400, "잘못된 요청입니다.");

    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}

package ai.fassto.messageapi.global.exception.handler;

import lombok.Getter;

@Getter
public enum ErrorCode {
    BAD_REQUEST_EXCEPTION(400, "WARN", "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR(500, "ERROR", "서버 오류가 발생하였습니다."),

    SAMPLE_NOT_FOUND(400, "WARN", "샘플 정보가 없습니다.");

    private final int status;
    private final String logLevel;
    private final String message;

    ErrorCode(int status, String logLevel, String message) {
        this.status = status;
        this.logLevel = logLevel;
        this.message = message;
    }
}

package ai.fassto.messageapi.global.exception;

import ai.fassto.messageapi.global.exception.handler.ErrorCode;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    protected ErrorCode errorCode;
    protected Object data;

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BaseException(ErrorCode errorCode, Object data) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.data = data;
    }
}

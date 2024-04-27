package ai.fassto.messageapi.global.exception;

import ai.fassto.messageapi.global.exception.handler.ErrorCode;

public class SampleException extends BaseException {
    public SampleException() {
        super();
    }

    public SampleException(String message) {
        super(message);
    }

    public SampleException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public SampleException(ErrorCode errorCode, Object data) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.data = data;
    }
}

package org.zhongweixian.exception;

/**
 * Created by caoliang on  6/6/2018
 */
public class PayException extends RuntimeException {

    private ErrorCode errorCode;

    public PayException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;

    }

    public PayException(ErrorCode errorCode, Throwable t) {
        this.errorCode = errorCode;
    }

    public PayException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public PayException(ErrorCode errorCode, String message, Throwable t) {
        super(message, t);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}

package com.marcos.personalNotesWebApplication.exceptions;

public class BusinessLogicException extends RuntimeException {
    private final String code;
    
    public BusinessLogicException(String message) {
        super(message);
        this.code = "BUSINESS_ERROR";
    }
    
    public BusinessLogicException(String message, String code) {
        super(message);
        this.code = code;
    }
    
    public BusinessLogicException(String message, Throwable cause) {
        super(message, cause);
        this.code = "BUSINESS_ERROR";
    }
    
    public String getCode() {
        return code;
    }
}

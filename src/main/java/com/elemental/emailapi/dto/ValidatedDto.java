package com.elemental.emailapi.dto;

public abstract class ValidatedDto {

    private Exception exception;
    private String exceptionNote;

    public ValidatedDto() {
        exception = null;
        exceptionNote = "";
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public String getExceptionNote() {
        return exceptionNote;
    }

    public void setExceptionNote(String exceptionNote) {
        this.exceptionNote = exceptionNote;
    }
}

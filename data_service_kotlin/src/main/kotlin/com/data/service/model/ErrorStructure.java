package com.data.service.model;

public class ErrorStructure {
    private int code;
    private String description;
    private ErrorStructure(){
    }

    void setCode(int code) {
        this.code = code;
    }

    void setDescription(String description) {
        this.description = description;
    }

    int getCode() {
        return code;
    }

    String getDescription() {
        return description;
    }

    public static ErrorStructure code(int number){
      ErrorStructure z = new ErrorStructure();
      z.code=number;
      return z;
    }

    public ErrorStructure description(String description){
        setDescription(description);
        return this;
    }
}

package com.ecommerce.app.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseDTO<T> {
    private ResponseStatus responseStatus;
    private String error;
    private T response;
    public enum ResponseStatus{
        SUCCESS,ERROR
    }

}

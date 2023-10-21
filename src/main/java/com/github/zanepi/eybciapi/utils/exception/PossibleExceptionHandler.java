package com.github.zanepi.eybciapi.utils.exception;

import com.github.zanepi.eybciapi.dto.StringResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PossibleExceptionHandler {

    public final String MSG = "message";


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public StringResponse handleCommonException(Exception ex){
        return new StringResponse(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public MethodArgumentNotValidList handleInvalidAttributesInRequestBody(MethodArgumentNotValidException ex){
        MethodArgumentNotValidList argumentNotValidList = new MethodArgumentNotValidList();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach((e) -> argumentNotValidList.addNewArgumentError(e.getField(),e.getDefaultMessage()));

        return  argumentNotValidList;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public StringResponse handleEmptyRequestBody(HttpMessageNotReadableException ex){
        return new StringResponse("Required body to perform the request is missing");
    }
}

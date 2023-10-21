package com.github.zanepi.eybciapi.utils.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Builder
@AllArgsConstructor
@Data
public class MethodArgumentNotValidList {

    private final String message = "There are some errors on the attributes provided in the request body";

    private Map<String,String> errors;

    public MethodArgumentNotValidList(){
        this.errors = new HashMap<String,String>();
    }

    public void addNewArgumentError(String attribute,String errorMessage){
        this.errors.put(attribute,errorMessage);
    }
}

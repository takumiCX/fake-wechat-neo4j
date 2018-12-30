package com.zju.fakewechat.util;

import org.springframework.validation.Errors;

import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * @author: takumiCX
 * @create: 2018-12-29
 **/
public class ErrorsUtils {


    public static String deal(Errors errors){

        if(errors.hasErrors()){

            StringJoiner joiner = new StringJoiner(";");
            errors.getAllErrors().stream().map(err->joiner.add(err.getDefaultMessage())).collect(Collectors.toList());
            return joiner.toString();
        }
        return "";
    }

}

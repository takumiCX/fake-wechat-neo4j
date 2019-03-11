package com.zju.fakewechat.model.response;

import lombok.Data;

/**
 * @author: takumiCX
 * @create: 2018-12-28
 **/

@Data
public class Response<R> {

    private boolean success;

    private R data;

    private String errMsg;

    public static <R> Response success(R data){

        Response<R> response = new Response<>();
        response.setSuccess(true);
        response.setData(data);
        return response;
    }


    public static <R> Response failed(Throwable e){

        Response<R> response = new Response<>();

        response.setSuccess(false);

        response.setErrMsg(e.toString());

        return response;

    }


    public static <R> Response failed(String errMsg){

        Response<R> response = new Response<>();

        response.setSuccess(false);
        response.setErrMsg(errMsg);
        return response;
    }


}

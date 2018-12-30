package com.zju.fakewechat.controller;

import com.zju.fakewechat.domain.Message;
import com.zju.fakewechat.model.response.Response;
import com.zju.fakewechat.services.MsgService;
import com.zju.fakewechat.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * @author: takumiCX
 * @create: 2018-12-30
 **/
@Controller
@RequestMapping(value = "/msg")
@Slf4j
@Validated
public class MessageController {

    @Autowired
    private UserService userService;

    @Autowired
    private MsgService msgService;


    @PostMapping("/add")
    @ResponseBody
    public Response<Message> addMsg(@RequestParam("userId") @NotNull(message = "userId不能为null!") Long userId,
                                    @RequestParam("content") @NotBlank(message = "content不能为空!") String content) {

        Response<Message> response;
        try {
            Message message = msgService.add(userId, content);

            response = Response.success(message);

        } catch (Exception e) {
            log.error("addMsg error", e);
            response = Response.failed(e);
        }
        return response;
    }


    @GetMapping("/all")
    @ResponseBody
    public Response<List<Message>> findAllMsgsByUserId(@RequestParam("userId") @NotNull Long userId){

        Response<List<Message>> response;

        try {
            List<Message> msgs=msgService.findAllMsgsByUserId(userId);

            response=Response.success(msgs);

        } catch (Exception e) {
           log.error("FindAllMsgsByUserId error,userId:{}",userId,e);
           response=Response.failed(e);
        }
        return response;
    }


    @PostMapping("/delete/{msgId}")
    @ResponseBody
    public Response<Boolean> deleteMsg(@PathVariable("msgId") @NotNull Long msgId){

        Response<Boolean> response;
        try {
            msgService.delMsg(msgId);
            response=Response.success(true);
        } catch (Exception e) {
            log.error("DeleteMsg error,msgId:{}",msgId,e);
            response=Response.failed(e);
        }
        return response;
    }

}

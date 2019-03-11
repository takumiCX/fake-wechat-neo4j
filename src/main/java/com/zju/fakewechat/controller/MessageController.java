package com.zju.fakewechat.controller;

import com.zju.fakewechat.domain.Message;
import com.zju.fakewechat.model.response.Response;
import com.zju.fakewechat.services.ImageService;
import com.zju.fakewechat.services.MsgService;
import com.zju.fakewechat.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;


/**
 * @author: takumiCX
 * @create: 2018-12-30
 **/
@Controller
@RequestMapping(value = "/msg")
@Slf4j
@Validated
@Api("朋友圈动态相关接口")
public class MessageController {

    @Autowired
    private UserService userService;

    @Autowired
    private MsgService msgService;

    @Autowired
    private ImageService imageService;


    @RequestMapping("/add")
    @ResponseBody
    @ApiOperation("添加一条动态")
    @CrossOrigin(origins = "http://localhost:3000")
    public Response<Message> addMsg(@RequestParam("userId") @NotNull(message = "userId不能为null!") Long userId,
                                    @RequestParam("content") @NotBlank(message = "content不能为空!") String content,
                                    HttpServletRequest request) {

        CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());

        String imagePathSepComma="";

        if (resolver.isMultipart(request)) {

            StringJoiner joiner = new StringJoiner(",");

            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;

            Iterator<MultipartFile> images = multiRequest.getFiles("images").iterator();

            while (images.hasNext()) {
                MultipartFile file = images.next();
//                MultipartFile file = multiRequest.getFile(images.next());
                String path = imageService.handle(file);
                log.info(path);
                joiner.add(path);
            }
            imagePathSepComma=joiner.toString();
        }

        Response<Message> response;
        try {
            Message message = msgService.add(userId, content,imagePathSepComma);

            response = Response.success(message);

        } catch (Exception e) {
            log.error("addMsg error", e);
            response = Response.failed(e);
        }
        return response;
    }


    @GetMapping("/all")
    @ResponseBody
    @ApiOperation("查询用户的所有动态消息,最新的在最前")
    @CrossOrigin(origins = "http://localhost:3000")
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
    @ApiOperation("删除一条动态")
    @CrossOrigin(origins = "http://localhost:3000")
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

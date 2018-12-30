package com.zju.fakewechat.controller;

import com.zju.fakewechat.domain.User;
import com.zju.fakewechat.model.response.Response;
import com.zju.fakewechat.services.UserService;
import com.zju.fakewechat.util.EncryptUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * @author: takumiCX
 * @create: 2018-12-28
 **/
@Controller
@RequestMapping("/user")
@Slf4j
@Validated
public class UserController {


    @Autowired
    private UserService userService;


    /**
     * 用户名重复校验
     *
     * @param userName
     * @return true:通过 false:不通过
     */
    @GetMapping("/check")
    @ResponseBody
    public Response<Boolean> check(@RequestParam("userName") String userName) {

        Response<Boolean> response;
        boolean isExists = false;
        try {
            isExists = userService.check(userName);
            if (isExists) {
                response = Response.failed("用户名已经存在!");
            } else {
                response = Response.success(true);
            }

        } catch (Exception e) {
            log.error("check userName error: {}", e);
            response = Response.failed(e);
        }
        return response;
    }


    @PostMapping(value = "/register")
    @ResponseBody
    public Response<User> register(@RequestParam("userName")@NotBlank(message = "userName不能为空!") String userName,
                                   @RequestParam("password")@NotBlank(message = "password不能为空!") String password) {

        Response<User> response;
        try {

            User user = userService.addUser(userName,password);

            response = Response.success(user);

        } catch (Exception e) {
            log.error("Register user error,userName:{}", userName, e);
            response = Response.failed(e);
        }
        return response;
    }


    /**
     * 登录
     * @param userName
     * @param password
     * @param session
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public Response<User> login(@RequestParam("userName")@NotBlank(message = "userName不能为空!") String userName,
                                @RequestParam("password")@NotBlank(message = "password不能为空!") String password,
                                HttpSession session)  {
        Response<User> response;
        try {
            Optional<User> userOptional = userService.findUser(userName, EncryptUtils.encoderByMd5(password));
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getUserId());
                response = Response.success(user);

            } else {

                response = Response.failed("用户名或密码错误!");
            }
        } catch (Exception e) {
            log.error("login error", e);
            response = Response.failed(e);
        }
        return response;
    }


    /**
     * 添加朋友
     * @param userId
     * @param friendName
     * @return
     */
    @PostMapping("/addFriend")
    @ResponseBody
    public Response<Boolean> addFriend(@RequestParam("userId")@NotNull(message = "userId不能为null!") Long userId,
                                       @RequestParam("friendName")@NotBlank(message = "friendName不能为空!") String friendName) {

        Response<Boolean> response;
        try {
            userService.makeFriend(userId,friendName);
            response = Response.success(true);

        } catch (Exception e) {
            log.error("addFriend error,userId:{},friendName:{}",userId,friendName, e);
            response = Response.failed(e);
        }
        return response;
    }


    /**
     * 查询所有朋友
     * @param userId 用户Id
     * @return
     */
    @GetMapping("/friends/")
    @ResponseBody
    public Response<List<User>> listAllFriends(@RequestParam("userId")@NotNull(message = "userId不能为null!") long userId) {

        Response<List<User>> response;
        try {
            List<User> users = userService.findAllFriendsOf(userId);

            response = Response.success(users);

        } catch (Exception e) {
            log.error("listAllFriend error", e);
            response = Response.failed(e);
        }

        return response;

    }

}

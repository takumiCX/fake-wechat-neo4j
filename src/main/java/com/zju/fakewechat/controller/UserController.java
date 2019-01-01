package com.zju.fakewechat.controller;

import com.zju.fakewechat.domain.User;
import com.zju.fakewechat.model.response.Response;
import com.zju.fakewechat.services.UserService;
import com.zju.fakewechat.util.EncryptUtils;
import io.swagger.annotations.*;
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
@Api("用户操作相关接口")
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
    @ApiOperation(value = "校验用户名是否可用,true:可用;false:该用户名已经存在,不可用")
    @ApiResponse(code = 200, message = "success=true")
    public Response<Boolean> check(@RequestParam("userName") @NotBlank(message = "用户名不能为空!") String userName) {

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
    @ApiOperation("注册新用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名"),
            @ApiImplicitParam(name = "password", value = "密码")
    })
    public Response<User> register(@RequestParam("userName") @NotBlank(message = "userName不能为空!") String userName,
                                   @RequestParam("password") @NotBlank(message = "password不能为空!") String password) {

        Response<User> response;
        try {

            User user = userService.addUser(userName, password);

            response = Response.success(user);

        } catch (Exception e) {
            log.error("Register user error,userName:{}", userName, e);
            response = Response.failed(e);
        }
        return response;
    }


    /**
     * 登录
     *
     * @param userName
     * @param password
     * @param session
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    @ApiOperation("用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名"),
            @ApiImplicitParam(name = "password", value = "密码")
    })
    public Response<User> login(@RequestParam("userName") @NotBlank(message = "userName不能为空!") String userName,
                                @RequestParam("password") @NotBlank(message = "password不能为空!") String password,
                                HttpSession session) {
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
     *
     * @param userId
     * @param friendName
     * @return
     */
    @PostMapping("/addFriend")
    @ResponseBody
    @ApiOperation("为当前用户添加朋友")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "当前登录用户的用户id"),
            @ApiImplicitParam(name = "friendName", value = "要添加为朋友的用户名")
    }
    )
    public Response<Boolean> addFriend(@RequestParam("userId") @NotNull(message = "userId不能为null!") Long userId,
                                       @RequestParam("friendName") @NotBlank(message = "friendName不能为空!") String friendName) {

        Response<Boolean> response;
        try {
            userService.makeFriend(userId, friendName);
            response = Response.success(true);

        } catch (Exception e) {
            log.error("addFriend error,userId:{},friendName:{}", userId, friendName, e);
            response = Response.failed(e);
        }
        return response;
    }


    /**
     * 查询所有朋友
     *
     * @param userId 用户Id
     * @return
     */
    @GetMapping("/friends/")
    @ResponseBody
    @ApiOperation("查询和当前用户为朋友关系的所有用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id")
    })
    public Response<List<User>> listAllFriends(@RequestParam("userId") @NotNull(message = "userId不能为null!") Long userId) {

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


    /**
     * 查询当前用户(userId)某个朋友(friendId)的朋友但和当前用户不是朋友关系的所有用户,即二度人脉
     *
     * @param userId   当前用户的id
     * @param friendId 和当前用户具有朋友关系的用户id
     * @return 所有二度人脉用户列表
     */
    @GetMapping("/twoDegree")
    @ResponseBody
    @ApiOperation("查询当前用户(userId)某个朋友(friendId)的朋友但和当前用户不是朋友关系的所有用户,即二度人脉")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "当前用户的Id"),
            @ApiImplicitParam(name = "friendId", value = "朋友的Id")
    })
    public Response<List<User>> findTwoDegreeUsers(@RequestParam @NotNull(message = "userId") Long userId,
                                                   @RequestParam @NotNull(message = "friendId") Long friendId) {

        Response<List<User>> response;

        try {
            List<User> users = userService.findTwoDegreeUsers(userId, friendId);
            response = Response.success(users);

        } catch (Exception e) {
            log.error("findTwoDegreeUsers error,userId:{},friendId:{}", userId, friendId);
            response = Response.failed(e);
        }
        return response;
    }


}

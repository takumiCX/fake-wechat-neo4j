package com.zju.fakewechat.listener;

import com.zju.fakewechat.domain.Message;
import com.zju.fakewechat.domain.User;
import com.zju.fakewechat.repositories.MsgRepository;
import com.zju.fakewechat.repositories.UserRepository;
import com.zju.fakewechat.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author: takumiCX
 * @create: 2018-12-30
 **/

@Component
public class ContextStartedListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MsgRepository msgRepository;

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        userRepository.deleteAll();

        msgRepository.deleteAll();

        ArrayList<User> users = new ArrayList<>();
        User guanyu = new User("关羽", "guanyu");
        User zhangfei = new User("张飞", "zhangfei");
        User zhaoyun = new User("赵云", "zhaoyun");
        User zhangliao = new User("张辽", "zhangliao");
        User xuhuang = new User("徐晃", "xuhuang");

        users.add(guanyu);
        users.add(zhangfei);
        users.add(zhaoyun);
        users.add(zhangliao);
        users.add(xuhuang);
        //添加用户
        users.forEach(userService::save);

        //赵云和张飞、关羽是朋友
        zhaoyun.getFriends().add(zhangfei);
        zhaoyun.getFriends().add(guanyu);

        //关羽和张辽、徐晃是朋友
        guanyu.getFriends().add(zhangliao);
        guanyu.getFriends().add(xuhuang);

        //张辽和徐晃是朋友、
        zhangliao.getFriends().add(xuhuang);

        //赵云添加动态
        zhaoyun.getMessages().add(new Message("大家好,我叫赵云!",new Date()));
        //张辽添加动态
        zhangliao.getMessages().add(new Message("大家好,我叫张辽!",new Date()));

        userRepository.save(zhaoyun);
        userRepository.save(zhangliao);
        userRepository.save(guanyu);

    }
}

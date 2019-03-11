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
        //蜀国
        User guanyu = new User("关羽", "guanyu");
        User zhangfei = new User("张飞", "zhangfei");
        User kongming = new User("孔明", "kongming");

        //魏国
        User zhangliao = new User("张辽", "zhangliao");
        User caocao = new User("曹操", "caocao");

        //吴国
        User lusu = new User("鲁肃", "lusu");
        User zhouyu = new User("周瑜", "zhouyu");

        users.add(guanyu);
        users.add(zhangfei);
        users.add(kongming);
        users.add(zhangliao);
        users.add(caocao);
        users.add(lusu);
        users.add(zhouyu);

        //孔明和张飞、关羽,鲁肃是朋友
        kongming.getFriends().add(zhangfei);
        kongming.getFriends().add(guanyu);
        kongming.getFriends().add(lusu);


        //关羽和张飞,孔明,张辽,曹操是朋友
        guanyu.getFriends().add(kongming);
        guanyu.getFriends().add(zhangfei);
        guanyu.getFriends().add(zhangliao);
        guanyu.getFriends().add(caocao);


        //曹操和张辽是朋友
        caocao.getFriends().add(zhangliao);

        //周瑜和鲁肃是朋友
        zhouyu.getFriends().add(lusu);



        //关羽添加动态
        guanyu.getMessages().add(new Message("大家好,我叫关羽!",new Date()));

        //添加用户
        users.forEach(userService::save);
    }
}

package com.zju.fakewechat.services;

import com.zju.fakewechat.domain.Message;
import com.zju.fakewechat.domain.User;
import com.zju.fakewechat.repositories.MsgRepository;
import com.zju.fakewechat.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author: takumiCX
 * @create: 2018-12-30
 **/
@Service
@Slf4j
public class MsgService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MsgRepository msgRepository;


    @Transactional
    public Message add(long userId, String content) {


        Optional<User> userOptional = userRepository.findById(userId);

        if(!userOptional.isPresent()){
            throw new RuntimeException("用户不存在,用户id:"+userId);
        }

        User user = userOptional.get();

        Message msg = new Message();

        msg.setContent(content);
        msg.setTime(new Date());
        user.getMessages().add(msg);
        userRepository.save(user);
        return msg;
    }

    public List<Message> findAllMsgsByUserId(Long userId) {

        List<Message> msgs = msgRepository.findAllMsgsByUserId(userId);

        if(msgs!=null){
            Collections.sort(msgs,(o1,o2)->o2.getTime().compareTo(o1.getTime()));
        }
        return msgs;
    }

    @Transactional
    public void delMsg(Long msgId) {

        msgRepository.deleteById(msgId);

    }
}

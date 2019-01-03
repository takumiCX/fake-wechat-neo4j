package com.zju.fakewechat.services;

import com.zju.fakewechat.domain.User;
import com.zju.fakewechat.repositories.UserRepository;
import com.zju.fakewechat.util.EncryptUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @author: takumiCX
 * @create: 2018-12-28
 **/
@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean check(String userName) {

        Optional<User> user = userRepository.findByName(userName);
        return user.isPresent();
    }

    public User addUser(String userName, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        User user = new User();
        user.setName(userName);
        user.setPassword(EncryptUtils.encoderByMd5(password));
        User savedUser = userRepository.save(user);
        return savedUser;

    }

    public Optional<User> findUser(String userName, String password) {

        return userRepository.findByNameAndPassword(userName, password);
    }

    public void makeFriend(Long id, String friendName) {

        Optional<User> userOptional = userRepository.findById(id);
        if(!userOptional.isPresent()) throw new RuntimeException("用户不存在,id:"+id);
        User user = userOptional.get();

        if (!user.isFriendOf(friendName)) {
            Optional<User> friend = userRepository.findByName(friendName);
            if (!friend.isPresent()) {
                throw new RuntimeException("用户不存在,name:" + friendName);
            }
            user.beFriendWith(friend.get());
            userRepository.save(user);
            log.info("Add friend:{} for user:{}",friendName,id);
        }
    }

    public Optional<User> findUser(String userName) {

        return userRepository.findByName(userName);

    }

    public List<User> findAllFriendsOf(long userId) {

        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            List<User> friends = user.get().getFriends();
            Collections.sort(friends, new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });

            return friends;
        }

        return new ArrayList<>();

    }

    public List<User> findTwoDegreeUsers(Long userId, Long friendId) {

        Optional<User> userOptional = userRepository.findById(userId);
        if(!userOptional.isPresent()){
            throw new RuntimeException("用户不存在,id:"+userId);
        }

        Optional<User> friendOptional = userRepository.findById(friendId);
        if(!friendOptional.isPresent()){
            throw new RuntimeException("用户不存在,id:"+friendId);
        }

        List<User> friends = userOptional.get().getFriends();

        List<User> friendsOfFriend = friendOptional.get().getFriends();

        friendsOfFriend.removeAll(friends);
        friendsOfFriend.remove(userOptional.get());

        return friendsOfFriend;
    }

    public void save(User user){
        try {

            user.setPassword(EncryptUtils.encoderByMd5(user.getPassword()));
            userRepository.save(user);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}

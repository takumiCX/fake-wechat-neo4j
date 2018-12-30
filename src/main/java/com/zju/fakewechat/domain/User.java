package com.zju.fakewechat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zju.fakewechat.Relation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.ogm.annotation.Relationship.UNDIRECTED;

/**
 * @author: takumiCX
 * @create: 2018-12-27
 **/
@NodeEntity
@NoArgsConstructor
@Getter
@Setter
public class User {

    //userId就是nodeId
    @Id
    @GeneratedValue
    private Long userId;

    @Index(unique = true)
    private String name;

    @JsonIgnore
    private String password;

    @JsonIgnore
    @Relationship(type = Relation.IS_FRIEND_OF, direction = UNDIRECTED)
    private List<User> friends = new ArrayList<>();

    @Relationship(type = Relation.HAS_SENT)
    private List<Message> messages=new ArrayList<>();


    public User(String name) {
        this.name = name;
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public void beFriendWith(User user) {
        if (friends == null) {
            friends = new ArrayList<>();
        }
        friends.add(user);
    }

    public boolean isFriendOf(String friendName) {
        if (name.equals(friendName)) throw new RuntimeException("不能与自己成为朋友!");
        for (User user : friends) {
            if (user.getName().equals(friendName)) return true;
        }
        return false;
    }

}

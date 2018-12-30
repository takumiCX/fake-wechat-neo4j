package com.zju.fakewechat.domain;

import com.zju.fakewechat.Relation;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;

/**
 * @author: takumiCX
 * @create: 2018-12-30
 **/
@RelationshipEntity(type = Relation.HAS_SENT)
@Getter
@Setter
public class User2Msg {

    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private User user;

    @EndNode
    private Message message;

}

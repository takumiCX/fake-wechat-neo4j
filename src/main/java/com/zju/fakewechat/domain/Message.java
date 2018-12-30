package com.zju.fakewechat.domain;

import com.zju.fakewechat.Relation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import javax.validation.constraints.NotBlank;
import java.util.Date;

import static org.neo4j.ogm.annotation.Relationship.INCOMING;


/**
 * @author: takumiCX
 * @create: 2018-12-30
 **/


@NodeEntity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue
    private Long id;

    //内容
    @NotBlank(message = "内容不能为空")
    private String content;

    //时间
    private Date time;

    public Message(@NotBlank(message = "内容不能为空") String content, Date time) {
        this.content = content;
        this.time = time;
    }

    @Relationship(type = Relation.HAS_SENT,direction = INCOMING)
    private User user;

}

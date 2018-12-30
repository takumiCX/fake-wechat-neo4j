package com.zju.fakewechat.repositories;

import com.zju.fakewechat.domain.Message;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * @author: takumiCX
 * @create: 2018-12-30
 **/
public interface MsgRepository extends Neo4jRepository<Message,Long> {


    @Query("MATCH (n:User)-[r:HAS_SENT]->(m:Message) where id(n)={userId} RETURN m ")
    List<Message> findAllMsgsByUserId(@Param("userId") long userId);
}

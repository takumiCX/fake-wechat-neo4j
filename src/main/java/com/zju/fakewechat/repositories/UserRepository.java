package com.zju.fakewechat.repositories;

import com.zju.fakewechat.domain.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

/**
 * @author: takumiCX
 * @create: 2018-12-28
 **/
@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface UserRepository extends Neo4jRepository<User,Long> {


    Optional<User> findByName(@Param("name") String name);


    Optional<User> findByNameAndPassword(@Param("name") String name, @Param("password") String password);

}

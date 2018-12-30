package com.zju.fakewechat.domain;

import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * @author: takumiCX
 * @create: 2018-12-30
 **/
@NodeEntity
@Getter
@Setter
public class Phone {

    private Long id;

    private String phoneNo;
}

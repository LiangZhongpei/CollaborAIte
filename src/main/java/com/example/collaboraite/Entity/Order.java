package com.example.collaboraite.Entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Data//相当于get/set方法
@Document(collection="client")//对应数据库的集合名（表名）
public class Order implements Serializable {
    @Field("Oid")
    private Integer Oid;

    private Integer Tid;
    private Integer Cid;
    private String StartTime;
    private String Type;
    private String EndTime;
    private Integer Money;
    private String Time;
    private Integer State;
}

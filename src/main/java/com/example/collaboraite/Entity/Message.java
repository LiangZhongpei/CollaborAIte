package com.example.collaboraite.Entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Data//相当于get/set方法
@Document(collection="message")//对应数据库的集合名（表名）
public class Message implements Serializable {
    @Field("Mid")
    private Integer Mid;

    private Integer Cid;
    private String Content;
    private String Time;
}

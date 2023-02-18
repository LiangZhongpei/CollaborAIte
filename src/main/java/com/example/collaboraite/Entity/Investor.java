package com.example.collaboraite.Entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Data//相当于get/set方法
@Document(collection="investor")//对应数据库的集合名（表名）
public class Investor implements Serializable {
    @Field("Iid")
    private Integer Iid;

    private String Name;
    private String Email;
    private String Phone;
    private String Message;
    private String Time;
}

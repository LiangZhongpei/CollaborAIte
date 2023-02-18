package com.example.collaboraite.Entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Data//相当于get/set方法
@Document(collection="client")//对应数据库的集合名（表名）
public class Client implements Serializable {
    @Field("Cid")
    private Integer Cid;

    private String FirstName;
    private String LastName;
    private String Email;
    private String Password;
    private String Area;
}

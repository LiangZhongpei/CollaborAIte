package com.example.collaboraite.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data//相当于get/set方法
@Document(collection="info")//对应数据库的集合名（表名）
public class Info implements Serializable {
    @Id
    private Integer Id;

    private String CollName;
    private Integer IncId;
}

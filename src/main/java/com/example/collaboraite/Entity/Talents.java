package com.example.collaboraite.Entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Data//相当于get/set方法
@Document(collection="talents")//对应数据库的集合名（表名）
public class Talents implements Serializable {
    @Field("Tid")
    private Integer Tid;

    private String TechnicalCapacity;
    private String CaseShow;
    private String Salary;
    private String WorkExperience;
    private String Position;
    private String Unit;
    private String ProjectExperience;
}

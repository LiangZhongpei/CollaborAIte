package com.example.collaboraite.Entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Data//相当于get/set方法
@Document(collection="client")//对应数据库的集合名（表名）
public class Demand implements Serializable {
    @Field("Did")
    private Integer Did;

    private String TalentType;
    private String ProjectName;
    private String ProjectDescription;
    private String StartTime;
    private String Duration;
    private String DailyRate;
    private String Name;
    private String Phone;
    private String Email;
    private Integer State;
    private Integer Cid;
    private String Time;
}

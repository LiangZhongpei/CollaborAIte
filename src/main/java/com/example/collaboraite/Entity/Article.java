package com.example.collaboraite.Entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data//相当于get/set方法
@Document(collection = "articles")
public class Article {
    @Field("Aid")
    private Integer Aid;

    private String title;
    private String body;
    private String imagefile;
    private String time;
    private String author;
    private String type;
}

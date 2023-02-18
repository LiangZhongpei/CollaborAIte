package com.example.collaboraite.Entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Data
@Document(collection="cases")
public class Cases implements Serializable {
    @Field("Cid")
    private Integer Cid;

    private String Name;
    private String Type;
    private String ProjectIntroduction;
}

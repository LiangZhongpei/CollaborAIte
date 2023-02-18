package com.example.collaboraite.Entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection="cases")
public class Evaluates {
    @Field("Eid")
    private Integer Eid;

    private String OrderName;
    private String DemandsNumber;
    private String OrdersNumber;
    private String Comment;
    private String Unit;
    private String Position;
    private String Salary;
    private String Experience;
}

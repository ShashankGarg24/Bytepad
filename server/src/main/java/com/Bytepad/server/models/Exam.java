package com.Bytepad.server.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Exam {


    @Id
    @Column(nullable = false, unique = true)
    private UUID examTypeId;
    @Column(nullable = false)
    private String examType;


    protected Exam() {
    }

    public Exam(String examType) {
        this.examTypeId = UUID.randomUUID();
        this.examType = examType;
    }


}

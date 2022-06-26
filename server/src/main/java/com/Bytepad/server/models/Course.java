package com.Bytepad.server.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Course {

    @Id
    @Column(nullable = false, unique = true)
    private UUID courseId;
    @Column(nullable = false)
    private String course;


    public Course() {
    }

    public Course(String course) {
        this.courseId = UUID.randomUUID();
        this.course = course;
    }
}

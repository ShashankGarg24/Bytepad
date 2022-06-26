package com.Bytepad.server.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class CourseBranch {

    @Id
    @Column(nullable = false, unique = true)
    private UUID courseBranchId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Branch branch;

    public CourseBranch(Course course, Branch branch) {
        this.courseBranchId = UUID.randomUUID();
        this.course = course;
        this.branch = branch;

    }


}

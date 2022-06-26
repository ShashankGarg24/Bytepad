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
public class Branch {

    @Id
    @Column(nullable = false, unique = true)
    private UUID branchId;
    @Column(nullable = false)
    private String branch;

    public Branch() {
    }

    public Branch(String branch) {
        this.branchId = UUID.randomUUID();
        this.branch = branch;
    }
}

package com.Bytepad.server.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class AndroidStatus {

    @Id
    @Column(nullable = false, unique = true)
    private Integer id;
    @Column(nullable = false)
    private String androidVersion;

    public AndroidStatus() {

    }

    public AndroidStatus(String androidVersion) {
        id = 1;
        this.androidVersion = androidVersion;
    }


}

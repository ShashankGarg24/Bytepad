package com.Bytepad.server.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Session {

    @Id
    @Column(nullable = false, unique = true)
    private UUID sessionId;
    @Column(nullable = false)
    private String session;

    public Session() {
    }

    public Session(String session) {
        this.sessionId = UUID.randomUUID();
        this.session = session;
    }

}

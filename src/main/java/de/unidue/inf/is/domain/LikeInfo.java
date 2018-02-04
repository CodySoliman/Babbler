package de.unidue.inf.is.domain;

import java.sql.Timestamp;

public class LikeInfo {

    String username;
    Boolean type;
    Timestamp time;

    public LikeInfo(String username, Boolean type, Timestamp time) {
        super();
        this.username = username;
        this.type = type;
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public Boolean getType() {
        return type;
    }

    public Timestamp getTime() {
        return time;
    }

}

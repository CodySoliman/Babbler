package de.unidue.inf.is.domain;

import java.sql.Timestamp;

public class RebabbleInfo {
	
	String username;
	Timestamp time;
	
	public RebabbleInfo(String username, Boolean type, Timestamp time) {
		super();
		this.username = username;
		this.time = time;
	}

	public String getUsername() {
		return username;
	}

	public Timestamp getTime() {
		return time;
	}

}

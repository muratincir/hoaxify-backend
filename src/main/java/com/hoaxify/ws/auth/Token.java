package com.hoaxify.ws.auth;

import org.springframework.data.annotation.Id;

import com.hoaxify.ws.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Token {
	
	@Id
	private String token;
	
	@ManyToOne
	private User user;

}
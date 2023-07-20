package com.hoaxify.ws.user;

import java.awt.print.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<User,Long>{

	Page<User> findAll(Pageable page);

	Page<User> findByUsernameNot(String username, Pageable page);

	User findByUsername(String username);
	
	@Transactional
	void deleteByUsername(String username);
}

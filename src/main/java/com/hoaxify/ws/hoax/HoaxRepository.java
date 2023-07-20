package com.hoaxify.ws.hoax;

import java.awt.print.Pageable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaxify.ws.user.User;

import net.bytebuddy.asm.Advice.OffsetMapping.Sort;

public interface HoaxRepository extends JpaRepository<Hoax, Long> {

	Page<Hoax> findAll(Pageable page);
	Page<Hoax> findByUser(User user,Pageable page);
	Page<Hoax> findByIdLessThan(long id, Pageable page);
	Page<Hoax> findByIdLessThanAndUser(long id,User user,Pageable page);
	
	long countByIdGreaterThan(long id);
	
	long countByIdGreateThanAndUser(long id, User user);
	
	List<Hoax> findByIdGreaterThan(long id, Sort sort);
	
	List<Hoax> findByIdGreaterThanAndUser(long id, User user, Sort sort);
	Page<Hoax> findAll(Specification<Hoax> specification, Pageable page);
	
}

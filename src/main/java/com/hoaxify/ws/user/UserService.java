package com.hoaxify.ws.user;

import java.awt.print.Pageable;


import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hoaxify.ws.error.NotFoundException;
import com.hoaxify.ws.file.FileService;
import com.hoaxify.ws.user.vm.UserUpdateVM;

@Service
public class UserService {
	UserRepository userRepository;
	PasswordEncoder passwordEncoder; // en güvenli şifre saklama yöntemlerinden birisi !!!
	
	

	public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = new BCryptPasswordEncoder();
	}



	public void save(User user) {
		// user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
		
	}

	public Page<User> getUsers(Pageable page, User user) {
		if(user != null) {
			return userRepository.findByUsernameNot(user.getUsername(), page);
		}
		return userRepository.findAll(page);
	}


	public User getByUserName(String username) {
		User inDB =  userRepository.findByUsername(username);		
		if(inDB == null ) {
			throw new NotFoundException();
		}
		return inDB;
	}



	public User updateUser(String username, UserUpdateVM updatedUser) {
		User inDB = getByUserName(username);
		inDB.setDisplayName(updatedUser.getDisplayName());
		if(updatedUser.getImage() != null) {
		return userRepository.save(inDB);
	}
		return inDB;	
	}


	public void deleteUser(String username) {
		User inDB = userRepository.findByUsername(username);
		FileService fileService = new FileService(null, null);
		fileService.deleteAllStoredFilesForUser(inDB);
		userRepository.delete(inDB);
	}
}

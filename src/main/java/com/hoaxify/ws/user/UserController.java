package com.hoaxify.ws.user;

import java.awt.print.Pageable;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hoaxify.ws.error.ApiError;
import com.hoaxify.ws.shared.GenericResponse;
import com.hoaxify.ws.user.vm.UserUpdateVM;
import com.hoaxify.ws.user.vm.UserVM;

import net.bytebuddy.asm.MemberSubstitution.Current;
@RestController
@RequestMapping("/api/1.0")
public class UserController {

	
	@Autowired
	UserService userService;
	private User user;
	
	@PostMapping("/users")
	public GenericResponse createUser(@Validated  @RequestBody User user) {
		userService.save(user);
		return new GenericResponse("user created");
	}
	
	@GetMapping("/users")
	Page<User> getUser(Pageable page,@Current User user){
		return userService.getUsers(page,user);
	}
	
	@GetMapping("/users/{username}")
	UserVM getUser(@PathVariable String username) {
		userService.getByUserName(username);
		return new UserVM(user);
	}
	
	@PutMapping("/users/{username}")
	@PreAuthorize("#username == principal.username")
	UserVM updateUser(@Validated @RequestBody UserUpdateVM updatedUser, @PathVariable String username) {
		User user = userService.updateUser(username, updatedUser);
		return new UserVM(user);
	}
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus()
	public ApiError handleValidationException(MethodArgumentNotValidException exception) {
		ApiError error = new ApiError(400, "Validation error","/api/1.0/users");
		Map<String,String> validationErrors = new HashMap<>();
//		for(FieldError fieldError : exception.getBindingResult().getFieldError()) {
//			validationErrors.put(fieldError.getField(),fieldError.getDefaultMessage());
//		}
		error.setValidationErrors(validationErrors);
		return error;
	}
	
	@DeleteMapping("/user/{username}")
	GenericResponse deleteUser(@PathVariable String username) {
		userService.deleteUser(username);
		return new GenericResponse("user is removed");
	}
	
}

package com.hoaxify.ws.shared;


import org.springframework.beans.factory.annotation.Autowired;

import com.hoaxify.ws.file.FileService;

import jakarta.validation.ConstraintValidatorContext;

public class FileTypeValidator implements jakarta.validation.ConstraintValidator<FileType, String>{
	
	@Autowired
	FileService fileService;
	
	String[] types;
	
	@Override
	public void initialize(FileType constraintAnnotation) {
		this.types = constraintAnnotation.types();
	}
	

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value == null || value.isEmpty()) {
			return true;
		}
	
		
		context.disableDefaultConstraintViolation();
//		HibernateConstraintValidatorContext hibernateConstraintValidatorContext = context.unwrap(HibernateConstraintValidatorContext.class);
//		hibernateConstraintValidatorContext.addMessageParameter("types", supportedTypes);
//		hibernateConstraintValidatorContext.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate()).addConstraintViolation();
		return false;
	}

}
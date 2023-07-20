package com.hoaxify.ws.hoax;



import java.awt.print.Pageable;



import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import com.hoaxify.ws.error.AuthorizationException;
import com.hoaxify.ws.file.FileAttachment;
import com.hoaxify.ws.file.FileAttachmentRepository;
import com.hoaxify.ws.hoax.vm.HoaxSubmitVM;
import com.hoaxify.ws.user.User;
import com.hoaxify.ws.user.UserService;


import jakarta.validation.Valid;
import net.bytebuddy.asm.Advice.OffsetMapping.Sort;

@Service
public class HoaxService {
	private HoaxRepository hoaxRepository;
	private UserService userService;
	private FileAttachmentRepository fileAttachmentRepository;
	

	public HoaxService(HoaxRepository hoaxRepository, UserService userService,FileAttachmentRepository fileAttachmentRepository) {
		super();
		this.hoaxRepository = hoaxRepository;
		this.userService = userService;
		this.fileAttachmentRepository=fileAttachmentRepository;
	}

	public Page<Hoax> getHoaxes(Pageable page) {
		return hoaxRepository.findAll(page);
	}
	
	public void save(@Valid HoaxSubmitVM hoaxSubmitVM, User user) {
		Hoax hoax = new Hoax();
		hoax.setContent(hoaxSubmitVM.getContent());
		hoax.setTimestamp(new Date(0));
		hoax.setUser(user);
		hoaxRepository.save(hoax);
		Optional<FileAttachment> optionalFileAttachment = fileAttachmentRepository.findById(hoaxSubmitVM.getAttachmentId());
			if(optionalFileAttachment.isPresent()) {
				FileAttachment fileAttachment = (FileAttachment) optionalFileAttachment.get();
				fileAttachment.setHoax(hoax);
				fileAttachmentRepository.save(hoax);
			}
	}

	public Page<Hoax> getHoaxesOfUser(String username, Pageable page) {
		User inDB = userService.getByUserName(username);
		return hoaxRepository.findByUser(inDB,page);
	}

	public Page<Hoax> getOldHoaxes(long id, String username, Pageable page) {
		Specification<Hoax> specification = idLessThan(id);
		if(username != null) {
			User inDB = userService.getByUserName(username);
			specification = specification.and(userIs(inDB));
		}
		return hoaxRepository.findAll (specification,page);
	}
	Specification<Hoax> userIs(User user){
		return (root, query, criteriaBuilder) -> {
			return criteriaBuilder.equal(root.get("user"), user);
		};
	}

	public Page<Hoax> getOldHoaxesOfUser(long id, String username, Pageable page) {
		User inDB = userService.getByUserName(username);
		return hoaxRepository.findByIdLessThanAndUser(id, inDB, page);
	}

	public long getNewHoaxesCount(long id) {
		return hoaxRepository.countByIdGreaterThan(id);
		
	}

	public long getNewHoaxesCountOfUser(long id, String username) {
		User inDB = userService.getByUserName(username);
		return hoaxRepository.countByIdGreateThanAndUser(id, inDB);
	}

	
	public Streamable<Order> getNewHoaxes(long id, String username, Pageable page) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Hoax> getNewHoaxesOfUser(long id, String username, Pageable page,Sort sort) {
		User inDB = userService.getByUserName(username);
		return hoaxRepository.findByIdGreaterThanAndUser(id, inDB, sort);
	}
	
	
	Specification<Hoax> idLessThan(long id){
		return (arg0, arg1, arg2) -> {
				return arg2.lessThan(arg0.get("id"),id);
		};
	}

	public void delete(long id, User loggedInUser) {
		Optional<Hoax> optionalHoax = hoaxRepository.findById(id);
		if(!optionalHoax.isPresent()) {
			throw new AuthorizationException();
		}
		
		Hoax hoax = optionalHoax.get();
		if(hoax.getUser().getId() != loggedInUser.getId()) {
			throw new AuthorizationException();
		}
		hoaxRepository.deleteById(id);
		
	}

	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

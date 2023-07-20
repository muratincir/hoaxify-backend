package com.hoaxify.ws.hoax;

import java.awt.print.Pageable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.util.Streamable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hoaxify.ws.hoax.vm.HoaxSubmitVM;
import com.hoaxify.ws.hoax.vm.HoaxVM;
import com.hoaxify.ws.shared.CurrentUser;
import com.hoaxify.ws.shared.GenericResponse;
import com.hoaxify.ws.user.User;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/1.0")
public class HoaxController {
	
	@Autowired
	private HoaxService hoaxService;
	
	@PostMapping("/hoaxes")
	GenericResponse saveHoax(@Valid @RequestBody HoaxSubmitVM hoaxSubmitVM , @CurrentUser User user) {
		hoaxService.save(hoaxSubmitVM,user);
		return new GenericResponse("Hoax is save");
	}
	
	@GetMapping("/hoaxes")
	Page<HoaxVM> getHoaxes (@PageableDefault(sort = "id",direction = Direction.DESC) Pageable page){
		return hoaxService.getHoaxes(page).map(HoaxVM::new);
	}
	
	@GetMapping({"/hoaxes/{id:[0-9]+}", "/users/{username}/hoaxes/{id:[0-9]+}"})  
	ResponseEntity<?> getHoaxesRelative(@PageableDefault(sort = "id", direction = Direction.DESC) Pageable page,
			@PathVariable long id,
			@PathVariable(required=false) String username,
			@RequestParam(name="count", required = false, defaultValue = "false") boolean count,
			@RequestParam(name="direction", defaultValue = "before") String direction){
		if(count) {
			long newHoaxCount = hoaxService.getNewHoaxesCountOfUser(id, username);
			Map<String, Long> response = new HashMap<>();
			response.put("count", newHoaxCount);
			return ResponseEntity.ok(response);
		}
		if(direction.equals("after")) {
			Streamable<Order> newHoaxes = hoaxService.getNewHoaxes(id, username,page);
			return ResponseEntity.ok(newHoaxes);
		}
		
		return ResponseEntity.ok(hoaxService.getNewHoaxes(id, username, page));
	}
	
	@GetMapping("/users/{username}/hoaxes")
	Page<HoaxVM> getUserHoaxes(@PathVariable String username,@PageableDefault(sort = "id",direction = Direction.DESC) Pageable page){
		return hoaxService.getHoaxesOfUser(username,page).map(HoaxVM::new);
	}
	
	@GetMapping("/users/{username}/hoaxes/{id:[0-9]+}")
	ResponseEntity<?> getUserHoaxesRelative(@PageableDefault(sort = "id", direction = Direction.DESC) Pageable page,
			@PathVariable long id,
			@PathVariable(required=false) String username,
			@RequestParam(name="count", required = false, defaultValue = "false") boolean count,
			@RequestParam(name="direction", defaultValue = "before") String direction){
		if(count) {
			long newHoaxCount = hoaxService.getNewHoaxesCountOfUser(id, username);
			Map<String, Long> response = new HashMap<>();
			response.put("count", newHoaxCount);
			return ResponseEntity.ok(response);
		}
		if(direction.equals("after")) {
			List<Hoax> newHoaxes = hoaxService.getNewHoaxesOfUser(id, username,page, null);
			return ResponseEntity.ok(newHoaxes);
		}
		
		return ResponseEntity.ok(hoaxService.getNewHoaxesOfUser(id, username, page, null));
	}
	
	
	// Güncel hoax sayısı
	@GetMapping("/users/{username}/hoaxes/{id:[0-9]+}")
	ResponseEntity<?> getUserHoaxesRelative(@PageableDefault(sort = "id",direction = Direction.DESC) Pageable page,
			@PathVariable long id,@RequestParam(name = "count",required = false,defaultValue = "false") boolean count){
		if(count) {
			long newHoaxCount =hoaxService.getNewHoaxesCount(id);
			Map<String,Long> response = new HashMap<>();
			response.put("count", newHoaxCount);
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok(hoaxService.getOldHoaxes(id,null, page).map(HoaxVM::new));
	}
	
	@GetMapping("/users/{username}/hoaxes/{id:[0-9]+}")
	ResponseEntity<?> getUserHoaxesRelative(@PathVariable String username,@PageableDefault(sort = "id",direction = Direction.DESC) Pageable page,
			@PathVariable long id , @RequestParam(name = "count",required = false,defaultValue = "false") boolean count){
		if(count) {
			long newHoaxCount =hoaxService.getNewHoaxesCountOfUser(id,username);
			Map<String,Long> response = new HashMap<>();
			response.put("count", newHoaxCount);
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok(hoaxService.getOldHoaxesOfUser(id,username,page).map(HoaxVM::new));
	}
	
	@DeleteMapping("/hoaxes/{id:[0-9]+}")
	@PreAuthorize("@hoaxSecurity.isAllowedToDelete(#id, principal)")
	GenericResponse deleteHoax(@PathVariable long id) {
		hoaxService.delete(id, null);
		return new GenericResponse("Hoax removed");
	}

}

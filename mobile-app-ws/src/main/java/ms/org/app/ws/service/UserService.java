package ms.org.app.ws.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import ms.org.app.ws.shared.dto.UserDto;

public interface UserService extends UserDetailsService {
	UserDto createUser(UserDto user);
	UserDto updateUser(String userId, UserDto user);
	
	void deleteUser(String userId);
	

	UserDto getUser(String email);
	
	UserDto getUserByUserId (String userId);
	
}

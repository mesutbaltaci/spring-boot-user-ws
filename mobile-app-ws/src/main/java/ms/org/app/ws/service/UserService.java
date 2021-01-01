package ms.org.app.ws.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import ms.org.app.ws.shared.dto.UserDto;

public interface UserService extends UserDetailsService {
	UserDto createUser(UserDto user);
	UserDto updateUser(String userId, UserDto user);
	
	void deleteUser(String userId);
	
	List<UserDto> getUsers(int page, int limit);
	

	UserDto getUser(String email);
	
	UserDto getUserByUserId (String userId);
	boolean verifyEmailToken(String token);
	boolean requestPasswordReset(String email);
	
	boolean resetPassword(String token, String password);
}

package ms.org.app.ws.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import ms.org.app.ws.shared.dto.UserDto;

public interface UserService extends UserDetailsService {
	UserDto createUser(UserDto user);

	UserDto getUser(String email);
}

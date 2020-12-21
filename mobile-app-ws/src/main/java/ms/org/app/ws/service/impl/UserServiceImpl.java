package ms.org.app.ws.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ms.org.app.ws.UserRepository.UserRepository;
import ms.org.app.ws.io.entity.UserEntity;
import ms.org.app.ws.service.UserService;
import ms.org.app.ws.shared.dto.UserDto;
import ms.org.app.ws.shared.dto.Utils;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	Utils utils;
	
	@Override
	public UserDto createUser(UserDto user) {
		
		
		
		if (userRepository.findByEmail(user.getEmail())!=null) throw new RuntimeException("Record already exists");
		
		
		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);
		
		userEntity.setUserId(utils.generateUserId(30));
		userEntity.setEncryptedPassword("test");
		
		UserEntity storedUserDetails = userRepository.save(userEntity);
		
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(storedUserDetails, returnValue);
		
		
		
		return returnValue;
	}

}

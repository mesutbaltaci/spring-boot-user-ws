package ms.org.app.ws.service.impl;

import java.util.ArrayList;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ms.org.app.ws.exception.UserServiceException;
import ms.org.app.ws.io.entity.UserEntity;
import ms.org.app.ws.service.UserService;
import ms.org.app.ws.shared.dto.UserDto;
import ms.org.app.ws.shared.dto.Utils;
import ms.org.app.ws.ui.model.response.ErrorMessages;
import ms.org.app.ws.ui.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	Utils utils;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public UserDto createUser(UserDto user) {
		
		
		
		if (userRepository.findByEmail(user.getEmail())!=null) throw new RuntimeException("Record already exists");
		
		
		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);
		
		userEntity.setUserId(utils.generateUserId(30));
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		
		UserEntity storedUserDetails = userRepository.save(userEntity);
		
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(storedUserDetails, returnValue);
		
		
		
		return returnValue;
	}

	
	
	@Override
	public UserDto getUser(String email) {
		UserEntity userEntity= userRepository.findByEmail(email);
		if (userEntity==null) throw new UsernameNotFoundException(email);
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);
		return returnValue;
	}



	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(email);
		if (userEntity==null) throw new UsernameNotFoundException(email);
		
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(),new ArrayList<>());
	}



	@Override
	public UserDto getUserByUserId(String userId) {
		UserDto returnValue = new UserDto();
		UserEntity userEntity = userRepository.findByUserId(userId);
		if (userEntity==null) throw new UsernameNotFoundException(userId);
		BeanUtils.copyProperties(userEntity, returnValue);
		return returnValue;
	}



	@Override
	public UserDto updateUser(String userId, UserDto user) {
		UserDto returnValue = new UserDto();
		UserEntity userEntity = userRepository.findByUserId(userId);
		if (userEntity==null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		
		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());
		
		UserEntity updatedUserDetails = userRepository.save(userEntity);
		BeanUtils.copyProperties(updatedUserDetails, returnValue);
		return returnValue;
		
		
		/*
		 * to update user
		 * put method / http://localhost:8080/users/BAEdQmA6bUjiPPJtdNPx5KTHFxsrZ7
		 * Body
		 * {
    		"firstName": "Hakki",
    		"lastName": "Tavsan"
   			}
   			
   			Header
   			Authorization => we need to provide authorization code starts with Bearer.....
   			because the user needs to authorize to update her/his info
		 */		
	}

}

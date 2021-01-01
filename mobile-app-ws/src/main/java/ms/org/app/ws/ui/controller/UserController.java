package ms.org.app.ws.ui.controller;



import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ms.org.app.ws.exception.UserServiceException;
import ms.org.app.ws.service.AddressService;
import ms.org.app.ws.service.UserService;
import ms.org.app.ws.shared.dto.AddressDTO;
import ms.org.app.ws.shared.dto.UserDto;
import ms.org.app.ws.ui.model.request.PasswordResetModel;
import ms.org.app.ws.ui.model.request.PasswordResetRequestModel;
import ms.org.app.ws.ui.model.request.UserDetailsRequestModel;
import ms.org.app.ws.ui.model.response.AddressesRest;
import ms.org.app.ws.ui.model.response.ErrorMessages;
import ms.org.app.ws.ui.model.response.OperationStatusModel;
import ms.org.app.ws.ui.model.response.RequestOperationName;
import ms.org.app.ws.ui.model.response.RequestOperationStatus;
import ms.org.app.ws.ui.model.response.UserRest;

@RestController
@RequestMapping("users")  //http://localhost:8080/users
public class UserController {
	@Autowired
	UserService userService;
	
	@Autowired
	AddressService addressService;
	
	@Autowired
	AddressService addressesService;
	
	@GetMapping(path="/{id}", produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE}) // the order is matter, normally json is default bu now xml will be first. Also now I dont need to add accept key and json or xml in postman
	public UserRest getUser (@PathVariable String id) {  //get user by user id using http://localhost:8080/users/BAEdQmA6bUjiPPJtdNPx5KTHFxsrZ7
		UserRest returnValue = new UserRest();
		UserDto userDto = userService.getUserByUserId(id);
		ModelMapper modelMapper = new ModelMapper();
		returnValue = modelMapper.map(userDto, UserRest.class);

		return returnValue;
	}
	
	@PostMapping(
			consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE}, //accept xml or json both to create
			produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE}) 
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
		UserRest returnValue = new UserRest();
		if (userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		
		//UserDto userDto = new UserDto();
		//BeanUtils.copyProperties(userDetails, userDto);
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
		
		UserDto createdUser = userService.createUser(userDto);
		//BeanUtils.copyProperties(createdUser, returnValue);
		returnValue= modelMapper.map(createdUser,UserRest.class);
		return returnValue;
	}

	@PutMapping(path="/{id}",
			consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE}, //accept xml or json both to create
			produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
		UserRest returnValue = new UserRest();
		if (userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);
		
		UserDto updatedUser = userService.updateUser(id,userDto);
		BeanUtils.copyProperties(updatedUser, returnValue);
				
		return returnValue;
	}
	
	@DeleteMapping(path="/{id}",
			consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE}, //accept xml or json both to create
			produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public OperationStatusModel deleteUser(@PathVariable String id) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		
		userService.deleteUser(id);
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}
	
	@GetMapping(produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public List<UserRest> getUsers(@RequestParam(value="page", defaultValue="0") int page, 
									@RequestParam(value="limit", defaultValue="25") int limit) {
		List<UserRest> returnValue = new ArrayList<>();
		
		List<UserDto> users= userService.getUsers(page, limit);
		
		Type listType = new TypeToken<List<UserRest>>() {
		}.getType();
		returnValue = new ModelMapper().map(users, listType);
		/*
		 * for (UserDto userDto : users) { UserRest userModel = new UserRest();
		 * BeanUtils.copyProperties(userDto, userModel); returnValue.add(userModel); }
		 */
		return returnValue;
	}
	
	//localhost:8080/mobile-app-ws/users/{userId}/addresses
	@GetMapping(path="/{id}/addresses", produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE}) // the order is matter, normally json is default bu now xml will be first. Also now I dont need to add accept key and json or xml in postman
	public List<AddressesRest> getUserAddresses (@PathVariable String id) {  //get user by user id using http://localhost:8080/users/BAEdQmA6bUjiPPJtdNPx5KTHFxsrZ7
		
		List<AddressesRest> returnValue = new ArrayList<>();
		
		List<AddressDTO> addressesDTO = addressesService.getAddresses(id);
		if (addressesDTO!= null && !addressesDTO.isEmpty()) {
			java.lang.reflect.Type listType = new TypeToken<List<AddressesRest>>() {}.getType();
			ModelMapper modelMapper = new ModelMapper();
			returnValue = modelMapper.map(addressesDTO, listType);
		}
		
		
		return returnValue;
	}
	
	@GetMapping(path="/{userId}/addresses/{addressId}", produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE}) // the order is matter, normally json is default bu now xml will be first. Also now I dont need to add accept key and json or xml in postman
	public AddressesRest getUserAddress (@PathVariable String userId, @PathVariable String addressId) {  //get user by user id using http://localhost:8080/users/BAEdQmA6bUjiPPJtdNPx5KTHFxsrZ7
		
				
		AddressDTO addressDTO = addressesService.getAddress(addressId);
		
			ModelMapper modelMapper = new ModelMapper();
			AddressesRest returnValue = modelMapper.map(addressDTO, AddressesRest.class);
			//http://localhot:8888/users.<userId> /addresses
			Link userLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(userId).withRel("user");
			Link userAddresesLink = WebMvcLinkBuilder.linkTo(UserController.class)
					.slash(userId)
					.slash("addresses")
					.withRel("addresses");
			
			Link selfLink = WebMvcLinkBuilder.linkTo(UserController.class)
					.slash(userId)
					.slash("addresses")
					.slash(addressId)
					.withSelfRel();
			returnValue.add(userLink);
			returnValue.add(userAddresesLink);
			returnValue.add(selfLink);
			
			
		
		return returnValue;
	}
	
	//http://localhost:8888/mobile-app-ws/users/email-verification?token=sdsdsads
	@GetMapping(path="/email-verification", produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE}) // the order is matter, normally json is default bu now xml will be first. Also now I dont need to add accept key and json or xml in postman
	public OperationStatusModel verifyEmailToken (@RequestParam(value="token") String token) {  //get user by user id using http://localhost:8080/users/BAEdQmA6bUjiPPJtdNPx5KTHFxsrZ7
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());

		boolean isVerified = userService.verifyEmailToken(token);
		if (isVerified) {
			returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		}else {
			returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
		}
		return returnValue;
	}
	
	//http://localhost:8080/mobile-app-ws/users/password-reset-request
		@PostMapping(path="/password-reset-request", produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE}) // the order is matter, normally json is default bu now xml will be first. Also now I dont need to add accept key and json or xml in postman
		public OperationStatusModel requestReset(@RequestBody PasswordResetRequestModel passwordResetRequestModel) {
	    	OperationStatusModel returnValue = new OperationStatusModel();
	 
	        boolean operationResult = userService.requestPasswordReset(passwordResetRequestModel.getEmail());
	        
	        returnValue.setOperationName(RequestOperationName.REQUEST_PASSWORD_RESET.name());
	        returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
	 
	        if(operationResult)
	        {
	            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
	        }

	        return returnValue;
	    }
		
		 @PostMapping(path = "/password-reset",
		            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
		    )
		    public OperationStatusModel resetPassword(@RequestBody PasswordResetModel passwordResetModel) {
		    	OperationStatusModel returnValue = new OperationStatusModel();
		 
		        boolean operationResult = userService.resetPassword(
		                passwordResetModel.getToken(),
		                passwordResetModel.getPassword());
		        
		        returnValue.setOperationName(RequestOperationName.PASSWORD_RESET.name());
		        returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
		 
		        if(operationResult)
		        {
		            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		        }

		        return returnValue;
		    }
	
}

package ms.org.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ms.org.app.ws.io.entity.AddressEntity;
import ms.org.app.ws.io.entity.UserEntity;
import ms.org.app.ws.shared.dto.AddressDTO;
import ms.org.app.ws.ui.model.response.AddressesRest;
import ms.org.app.ws.ui.repository.AddressRepository;
import ms.org.app.ws.ui.repository.UserRepository;

@Service
public class AddressServiceImpl implements ms.org.app.ws.service.AddressService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AddressRepository addressRepository;
	
	
	@Override
	public List<AddressDTO> getAddresses(String userId) {
		List<AddressDTO> returnValue = new ArrayList<>();
		ModelMapper modelMapper = new ModelMapper();
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity==null) return returnValue;
		
		Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);
		
		for (AddressEntity addressEntity: addresses) {
			returnValue.add(modelMapper.map(addressEntity, AddressDTO.class));
		}
		return returnValue;
	}


	@Override
	public AddressDTO getAddress(String addressId) {
		AddressDTO returnValue =null;
		ModelMapper modelMapper = new ModelMapper();
		
		AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
		
		if (addressEntity!=null) {
			returnValue= modelMapper.map(addressEntity, AddressDTO.class);
		}
		
		
		return returnValue;
		
	}

}

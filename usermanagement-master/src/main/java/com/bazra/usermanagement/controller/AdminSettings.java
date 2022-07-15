package com.bazra.usermanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bazra.usermanagement.model.AgentInfo;
import com.bazra.usermanagement.model.MerchantInfo;
import com.bazra.usermanagement.model.UserInfo;
import com.bazra.usermanagement.repository.AgentRepository;
import com.bazra.usermanagement.repository.MerchantRepository;
import com.bazra.usermanagement.repository.UserRepository;
import com.bazra.usermanagement.request.CreateSettingRequest;
import com.bazra.usermanagement.request.SettingRequest;
import com.bazra.usermanagement.response.TotalResponseAgents;
import com.bazra.usermanagement.response.TotalResponseCustomers;
import com.bazra.usermanagement.response.TotalResponseMerchants;
import com.bazra.usermanagement.service.SettingService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@CrossOrigin("*")
@RequestMapping("/settings")
public class AdminSettings {
	@Autowired
	SettingService settingService;
	
	@Autowired
	AgentRepository agentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	MerchantRepository merchantRepository;
	
	@PostMapping("/setsetting")
    @ApiOperation(value ="This EndPoint is to set configuration ")
    public ResponseEntity<?> setSetting(@RequestBody SettingRequest settingRequest,Authorization authorization) {
    	
        return settingService.setEntity(settingRequest);
    }
	
	@PostMapping("/createsetting")
    @ApiOperation(value ="This EndPoint is to set configuration ")
    public ResponseEntity<?> createSetting(@RequestBody CreateSettingRequest createSettingRequest,Authorization authorization) {
    	
        return settingService.createSetting(createSettingRequest);
    }
	
	@GetMapping("/totagents")
    @ApiOperation(value ="This EndPoint To Get Total Number of Agents")
    public ResponseEntity<?> getNumberOfAgents(Authentication authentication) {
		
		List<AgentInfo> agentInfo = agentRepository.findAll();
		return ResponseEntity.ok(new TotalResponseAgents(agentInfo.size(),"Total number of registered agents: "+agentInfo.size()));
        
    }
	
	@GetMapping("/totcustomers")
    @ApiOperation(value ="This EndPoint To Get Total Number of Agents")
    public ResponseEntity<?> getNumberOfCustomers(Authentication authentication) {
		

		List<UserInfo> userInfo = userRepository.findAll();

		return ResponseEntity.ok(new TotalResponseCustomers(userInfo.size(),"Total number of registered customers: "+userInfo.size()));

        
    }
	
	@GetMapping("/totmerchants")
    @ApiOperation(value ="This EndPoint To Get Total Number of Agents")
    public ResponseEntity<?> getNumberOfMerchants(Authentication authentication) {
		
		List<MerchantInfo> merchanInfo = merchantRepository.findAll();
		return ResponseEntity.ok(new TotalResponseMerchants(merchanInfo.size(),"Total number of registered customers: "+merchanInfo.size()));
        
    }
	
	
	

}

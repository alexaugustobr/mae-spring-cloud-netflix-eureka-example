package com.algaworks.example.product.api;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ServiceInstanceRestController {

	@Autowired
	private EurekaClient discoveryClient;

	@GetMapping("/service-instances")
	public Application serviceInstances() {
		return this.discoveryClient.getApplication("review-api");
	}
}
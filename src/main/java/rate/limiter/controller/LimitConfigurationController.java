package rate.limiter.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import rate.limiter.dto.ConfigurationReq;
import rate.limiter.service.ConfigurationService;

@RestController
public class LimitConfigurationController {

	private final Logger log = LoggerFactory.getLogger(LimitConfigurationController.class);

	@Autowired
	private ConfigurationService configService;

	/**
	 * @param configurationDTO
	 * @return
	 */
	@PostMapping(path = "/limit/configure")
	public boolean configureLimit(@Valid  @RequestBody final ConfigurationReq configReq) {
		log.info("Received request for rate limit configuration: {}", configReq);
		return configService.createConfiguration(configReq);
	}

}

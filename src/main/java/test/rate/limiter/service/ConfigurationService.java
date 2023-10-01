package test.rate.limiter.service;

import test.rate.limiter.dto.ConfigurationReq;
import test.rate.limiter.repository.entity.LimitConfiguration;

public interface ConfigurationService {
	
	public boolean createConfiguration(ConfigurationReq configReq);
	
	public LimitConfiguration findLimitConfiguration(String requestUrl, String userId);

}

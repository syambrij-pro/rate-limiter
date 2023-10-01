package rate.limiter.service;

import rate.limiter.dto.ConfigurationReq;
import rate.limiter.repository.entity.LimitConfiguration;

public interface ConfigurationService {
	
	public boolean createConfiguration(ConfigurationReq configReq);
	
	public LimitConfiguration findLimitConfiguration(String requestUrl, String userId);

}

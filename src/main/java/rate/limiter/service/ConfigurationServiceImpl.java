package rate.limiter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rate.limiter.dto.ConfigurationReq;
import rate.limiter.repository.ConfigurationRepository;
import rate.limiter.repository.entity.LimitConfiguration;
import rate.limiter.repository.entity.RateLimitPolicy;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

	@Autowired
	private ConfigurationRepository configRepository;

	@Override
	public boolean createConfiguration(ConfigurationReq configReq) {
		LimitConfiguration limitConfig = convertToEntity(configReq);
		configRepository.save(limitConfig);
		return true;
	}

	private LimitConfiguration convertToEntity(ConfigurationReq configReq) {
		LimitConfiguration limitConfig = new LimitConfiguration();
		limitConfig.setRequestUrl(configReq.getRequestUrl());
		limitConfig.setLimitPerMinute(configReq.getLimitPerMinute());
		limitConfig.setUserId(configReq.getUserId());
		RateLimitPolicy applicablePolicy = RateLimitPolicy.SLIDING_WINDOW;
		String rateLimitPolicyString = configReq.getRateLimitPolicy();
		RateLimitPolicy foundPolicy = null;
		if (rateLimitPolicyString != null
				&& (foundPolicy = RateLimitPolicy.valueOf(rateLimitPolicyString.toUpperCase())) != null) {
			applicablePolicy = foundPolicy;
		}
		if (configReq.getUserId() == null && rateLimitPolicyString == null) {
			// global limit for this url.
			applicablePolicy = RateLimitPolicy.LAZY_FILL_BUCKET;
		}
		limitConfig.setPolicy(applicablePolicy);
		return limitConfig;
	}

	@Override
	public LimitConfiguration findLimitConfiguration(String requestUrl, String userId) {
		List<LimitConfiguration> limitConfigList = null;
		if (userId != null) {
			limitConfigList = configRepository.findByRequestUrlAndUserId(requestUrl, userId);
		} else {
			limitConfigList = configRepository.findByRequestUrl(requestUrl);
		}
		if (limitConfigList == null || limitConfigList.isEmpty()) {
			return null;
		}
		return limitConfigList.get(0);
	}

}

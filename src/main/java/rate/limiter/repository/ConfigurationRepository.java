package rate.limiter.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rate.limiter.repository.entity.LimitConfiguration;

@Repository
public interface ConfigurationRepository extends JpaRepository<LimitConfiguration, UUID>{
	
	List<LimitConfiguration> findByRequestUrlAndUserId(String requestUrl, String userId);
	
	List<LimitConfiguration> findByRequestUrl(String requestUrl);

}

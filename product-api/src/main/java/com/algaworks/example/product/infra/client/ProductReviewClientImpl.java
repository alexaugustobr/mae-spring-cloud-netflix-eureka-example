package com.algaworks.example.product.infra.client;

import com.algaworks.example.product.api.ProductReviewClient;
import com.algaworks.example.product.api.ReviewModel;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class ProductReviewClientImpl implements ProductReviewClient {

	private final Logger logger = LoggerFactory.getLogger(ProductReviewClientImpl.class);
	private final RestTemplate restTemplate;
	private final EurekaClient eurekaClient;
	
	public ProductReviewClientImpl(RestTemplate restTemplate, EurekaClient eurekaClient) {
		this.restTemplate = restTemplate;
		this.eurekaClient = eurekaClient;
	}

	@Override
	public List<ReviewModel> findByProduct(Long productId) {
		final Map<String, Object> parametros = new HashMap<>();
		parametros.put("productId", productId);

		logger.debug("Buscando avaliações");
		final ReviewModel[] avaliacoes;

		Optional<InstanceInfo> availableInstance = getAvailableInstance();
		if (availableInstance.isEmpty()) {
			return new ArrayList<>();
		}

		final var path = "products/" + productId + "/reviews";
		final var requestUrl = availableInstance.get().getHomePageUrl() + path;

		try {
			avaliacoes = restTemplate.getForObject(requestUrl, ReviewModel[].class, parametros);
		} catch (Exception e) {
			logger.error("Erro ao buscar avaliações", e);
			return new ArrayList<>();
		}

		if (avaliacoes == null) {
			return new ArrayList<>();
		}

		return Arrays.asList(avaliacoes);
	}

	private Optional<InstanceInfo> getAvailableInstance() {
		final var reviewApiApp = eurekaClient.getApplication("review-api");
		if (reviewApiApp== null) {
			return Optional.empty();
		}
		return reviewApiApp.getInstances()
				.stream()
				.filter(p -> p.getStatus().equals(InstanceInfo.InstanceStatus.UP))
				.findAny();
	}
}

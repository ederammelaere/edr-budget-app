package org.edr.resource;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.edr.domain.BudgetStaat;
import org.edr.resource.json.MixInBudgetStaat;

import com.fasterxml.jackson.databind.ObjectMapper;

@Provider
public class JacksonConfigurator implements ContextResolver<ObjectMapper> {

	private final ObjectMapper mapper = new ObjectMapper();

	public JacksonConfigurator() {
		mapper.addMixInAnnotations(BudgetStaat.class, MixInBudgetStaat.class);
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return mapper;
	}

}

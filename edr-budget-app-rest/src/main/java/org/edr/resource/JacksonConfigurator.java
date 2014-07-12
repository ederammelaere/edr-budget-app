package org.edr.resource;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.edr.domain.BudgetStaat;
import org.edr.resource.json.MixInBudgetStaat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

@Provider
public class JacksonConfigurator implements ContextResolver<ObjectMapper> {

	private final ObjectMapper mapper = new ObjectMapper();

	private static final class LocalDateSerializer extends StdSerializer<LocalDate> {

		protected LocalDateSerializer(Class<?> t, boolean dummy) {
			super(t, dummy);
		}

		@Override
		public void serialize(LocalDate value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
				JsonProcessingException {
			// jgen.writeStartObject();
			jgen.writeString(value.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			// jgen.writeEndObject();
		}

	}

	public JacksonConfigurator() {
		mapper.addMixInAnnotations(BudgetStaat.class, MixInBudgetStaat.class);
		SimpleModule module = new SimpleModule("SimpleModule", new Version(1, 1, 0, null, null, null));
		module.addSerializer(new LocalDateSerializer(LocalDate.class, false));
		mapper.registerModule(module);
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return mapper;
	}

}

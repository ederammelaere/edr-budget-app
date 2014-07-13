package org.edr.resource;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.edr.domain.BudgetStaat;
import org.edr.po.Bankrekening;
import org.edr.po.Boekrekening;
import org.edr.po.jpa.BankrekeningPO;
import org.edr.po.jpa.BoekrekeningPO;
import org.edr.resource.json.MixInBudgetStaat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

@Provider
public class JacksonConfigurator implements ContextResolver<ObjectMapper> {

	private final ObjectMapper mapper = new ObjectMapper();

	private static final class LocalDateSerializer extends StdSerializer<LocalDate> {

		protected LocalDateSerializer() {
			super(LocalDate.class, false);
		}

		@Override
		public void serialize(LocalDate value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
				JsonProcessingException {
			// jgen.writeStartObject();
			jgen.writeString(value.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			// jgen.writeEndObject();
		}

	}

	private static final class LocalDateDeserializer extends StdDeserializer<LocalDate> {

		private static final long serialVersionUID = 1L;

		protected LocalDateDeserializer() {
			super(LocalDate.class);
		}

		@Override
		public LocalDate deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
				JsonProcessingException {
			return LocalDate.parse(jp.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		}

	}

	public JacksonConfigurator() {
		mapper.addMixInAnnotations(BudgetStaat.class, MixInBudgetStaat.class);
		SimpleModule module = new SimpleModule("SimpleModule", new Version(1, 1, 0, null, null, null));
		module.addSerializer(new LocalDateSerializer());
		module.addDeserializer(LocalDate.class, new LocalDateDeserializer());

		module.addAbstractTypeMapping(Bankrekening.class, BankrekeningPO.class);
		module.addAbstractTypeMapping(Boekrekening.class, BoekrekeningPO.class);

		mapper.registerModule(module);
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return mapper;
	}

}

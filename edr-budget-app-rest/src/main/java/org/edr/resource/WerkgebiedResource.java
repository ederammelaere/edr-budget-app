package org.edr.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Path("werkgebied")
public class WerkgebiedResource {

	@Autowired
	@Value("${jdbc.url}")
	private String jdbcUrl;

	@GET
	@Produces("text/plain")
	public String getWerkgebied() {
		return jdbcUrl.split("/")[3];
	}

}

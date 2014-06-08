package org.edr.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Path("helloworld")
public class HelloWorldResource {

	@Autowired
	@Value("${jdbc.url}")
	private String jdbcUrl;

	@GET
	@Produces("text/plain")
	public String getHello() {
		return "Hello world! " + jdbcUrl;
	}

}

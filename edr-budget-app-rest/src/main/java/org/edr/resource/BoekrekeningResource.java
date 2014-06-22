package org.edr.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.edr.po.Boekrekening;
import org.edr.services.BoekrekeningService;
import org.springframework.beans.factory.annotation.Autowired;

@Path("boekrekening")
public class BoekrekeningResource {

	@Autowired
	BoekrekeningService boekrekeningService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Boekrekening> findBoekrekeningen() {
		return boekrekeningService.findBoekrekeningen();
	}

}

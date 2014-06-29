package org.edr.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.edr.po.Boekrekening;
import org.edr.po.jpa.BoekrekeningPO;
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

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void createBoekrekening(BoekrekeningPO boekrekening) {
		boekrekeningService.createBoekrekening(boekrekening);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public void updateBoekrekening(BoekrekeningPO boekrekening) {
		boekrekeningService.updateBoekrekening(boekrekening);
	}

	@DELETE
	@Path("/{id}")
	public void deleteBoekrekening(@PathParam("id") Long id) {
		boekrekeningService.deleteBoekrekening(id);
	}

}

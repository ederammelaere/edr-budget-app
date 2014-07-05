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

import org.edr.po.Bankrekening;
import org.edr.po.jpa.BankrekeningPO;
import org.edr.services.BankrekeningService;
import org.springframework.beans.factory.annotation.Autowired;

@Path("bankrekening")
public class BankrekeningResource {

	@Autowired
	BankrekeningService bankrekeningService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Bankrekening> findBankrekeningen() {
		return bankrekeningService.findBankrekeningen();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void createBankrekening(BankrekeningPO bankrekening) {
		bankrekeningService.createBankrekening(bankrekening);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public void updateBankrekening(BankrekeningPO bankrekening) {
		bankrekeningService.updateBankrekening(bankrekening);
	}

	@DELETE
	@Path("/{id}")
	public void deleteBankrekening(@PathParam("id") Long id) {
		bankrekeningService.deleteBankrekening(id);
	}

}

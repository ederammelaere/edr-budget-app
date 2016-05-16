package org.edr.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.edr.domain.BudgetStaat;
import org.edr.services.BudgetStaatService;
import org.springframework.beans.factory.annotation.Autowired;

@Path("budgetstaat")
public class BudgetStaatResource {

	@Autowired
	BudgetStaatService budgetStaatService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<BudgetStaat> getBudgetStaat(@QueryParam("jaar") int jaar, @QueryParam("referentieJaar") int referentieJaar) {
		return budgetStaatService.getBudgetStaatAsList(jaar, referentieJaar);
	}

}

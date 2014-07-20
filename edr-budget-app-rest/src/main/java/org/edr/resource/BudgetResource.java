package org.edr.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.edr.po.Budget;
import org.edr.po.jpa.BudgetPO;
import org.edr.services.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;

@Path("budget")
public class BudgetResource {

	@Autowired
	BudgetService budgetService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Budget> findBudgetten(@QueryParam("jaar") int jaar) {
		return budgetService.findBudgetten(jaar);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void createBudget(BudgetPO budget) {
		budgetService.createBudget(budget);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public void updateBudget(BudgetPO budget) {
		budgetService.updateBudget(budget);
	}

	@DELETE
	@Path("/{id}")
	public void deleteBudget(@PathParam("id") Long id) {
		budgetService.deleteBudget(id);
	}

}

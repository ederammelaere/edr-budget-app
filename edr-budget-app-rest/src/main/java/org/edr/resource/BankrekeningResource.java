package org.edr.resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("csv")
	public String findBankrekeningenCsv() {
		return bankrekeningService.findBankrekeningen().stream()
				.map(s -> s.getId() + "|" + s.getOmschrijving() + "|" + s.getSaldo() + "|").reduce((s, t) -> {
					s += '\n' + t;
					return s;
				}).get();
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("index")
	public String getIndexHtml() throws IOException {
		return fromStream(BankrekeningResource.class.getResourceAsStream("/bankrekening.html"));
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

	private String fromStream(InputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder out = new StringBuilder();
		String newLine = System.getProperty("line.separator");
		String line;
		while ((line = reader.readLine()) != null) {
			out.append(line);
			out.append(newLine);
		}
		return out.toString();
	}
}

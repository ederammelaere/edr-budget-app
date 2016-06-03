package org.edr.resource;

import java.time.LocalDate;
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

import org.edr.po.Boeking;
import org.edr.po.jpa.BoekingPO;
import org.edr.services.BoekingService;
import org.springframework.beans.factory.annotation.Autowired;

@Path("boeking")
public class BoekingResource {

    @Autowired
    BoekingService boekingService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Boeking> findBoekingen(@QueryParam("jaar") int jaar) {
        return boekingService.findBoekingen(jaar);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/manueel/{jaar}/{maand}/{dag}")
    public List<Boeking> findManueleBoekingen(@PathParam("jaar") int jaar, @PathParam("maand") int maand,
                                              @PathParam("dag") int dag) {
        return boekingService.findManueleBoekingen(LocalDate.of(jaar, maand, dag));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void createBoeking(BoekingPO boeking) {
        boekingService.createBoeking(boeking);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public void updateBoeking(BoekingPO boeking) {
        boekingService.updateBoeking(boeking);
    }

    @DELETE
    @Path("/{id}")
    public void deleteBoeking(@PathParam("id") Long id) {
        boekingService.deleteBoeking(id);
    }

}

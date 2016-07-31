package ru.vadimlyalin.revolut.controller;

import ru.vadimlyalin.revolut.TransferDao;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

@Path("/transfers/from/{accountFrom}/to/{accountTo}/sum/{sum}")
public class TransferController {
	@Inject
	TransferDao transferDao;

	/**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @POST
	@Produces("text/plain")
    public Response transfer(@PathParam("accountFrom") long accountFrom, @PathParam("accountTo") long accountTo, @PathParam("sum") BigDecimal sum) {
		try {
			transferDao.transfer(accountFrom, accountTo, sum);
			return Response.status(Response.Status.CREATED).build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
    }

	public void setTransferDao(TransferDao transferDao) {
		this.transferDao = transferDao;
	}
}

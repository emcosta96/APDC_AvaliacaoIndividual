package pt.unl.fct.di.apdc.aindividual.resources;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Transaction;

import pt.unl.fct.di.apdc.aindividual.util.LogoutData;

@Path("/logout")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class LogoutResource {

	private static final Logger LOG = Logger.getLogger(UpdateResource.class.getName());
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	public LogoutResource() {
	}

	@DELETE
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response doLogout(LogoutData data) {
		
		if (data.getToken() == null || data.getToken().expirationData < System.currentTimeMillis())
			return Response.status(Status.UNAUTHORIZED).entity("The session has expired.").build();
		
		Key tokenKey = datastore.newKeyFactory().setKind("Token").newKey(data.getToken().tokenID);
		Entity token = datastore.get(tokenKey);
		
		if(token==null || !token.getString("token_username").equals(data.getToken().username))
			return Response.status(Status.BAD_REQUEST).entity("Operation not allowed.").build();

		Transaction txn = datastore.newTransaction();
		try {
			datastore.delete(tokenKey);
			LOG.fine("Logged out.");
			txn.commit();
			return Response.ok("{}").build();
		} finally {
			if (txn.isActive())
				txn.rollback();
		}
	}
	
	
	

}

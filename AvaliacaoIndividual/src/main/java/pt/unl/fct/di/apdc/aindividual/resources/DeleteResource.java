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
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Transaction;
import com.google.cloud.datastore.Entity;

import pt.unl.fct.di.apdc.aindividual.util.DeleteData;

@Path("/delete")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class DeleteResource {

	private static final Logger LOG = Logger.getLogger(UpdateResource.class.getName());
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	public DeleteResource() {
	}

	@DELETE
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response doDelete(DeleteData data) {
		LOG.fine("Attempt to delete user");

		if (data.getToken() == null || data.getToken().expirationData < System.currentTimeMillis())
			return Response.status(Status.UNAUTHORIZED).entity("The session has expired.").build();
		if (!data.validDelete())
			return Response.status(Status.BAD_REQUEST).entity("Wrong parameter.").build();

		Key tokenKey = datastore.newKeyFactory().setKind("Token").newKey(data.getToken().tokenID);
		Entity token = datastore.get(tokenKey);
		
		if(token==null || !token.getString("token_username").equals(data.getToken().username))
			return Response.status(Status.BAD_REQUEST).entity("Operation not allowed.").build();
		
		Transaction txn = datastore.newTransaction();
		try {
			Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.getToken().username);
			Entity user = datastore.get(userKey);
			
			user = Entity.newBuilder(userKey).set("user_name", user.getString("user_name"))
					.set("user_pwd", user.getString("user_pwd"))
					.set("user_email", user.getString("user_email"))
					.set("user_creation_time", user.getTimestamp("user_creation_time"))
					.set("user_place", user.getString("user_place"))
					.set("user_country", user.getString("user_country"))
					.set("user_role", user.getString("user_role"))
					.set("user_status", "INACTIVE")
					.build();
			
			txn.put(user);
			txn.delete(tokenKey);
			LOG.fine("User deleted.");
			txn.commit();
			return Response.ok("{}").build();
			
		} finally {
			if (txn.isActive())
				txn.rollback();
		}
	}

}

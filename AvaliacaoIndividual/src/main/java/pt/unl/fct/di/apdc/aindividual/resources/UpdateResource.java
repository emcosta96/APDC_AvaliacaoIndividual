package pt.unl.fct.di.apdc.aindividual.resources;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
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

import pt.unl.fct.di.apdc.aindividual.util.UpdateData;

@Path("/update")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class UpdateResource {

	private static final Logger LOG = Logger.getLogger(UpdateResource.class.getName());
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	public UpdateResource() {
	}

	@PUT
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response doUpdate(UpdateData data) {
		LOG.fine("Attempt to update user");

		if (data.getToken() == null || data.getToken().expirationData < System.currentTimeMillis())
			return Response.status(Status.UNAUTHORIZED).entity("The session has expired.").build();
		if (!data.validUpdate())
			return Response.status(Status.BAD_REQUEST).entity("Wrong parameter.").build();

		Key tokenKey = datastore.newKeyFactory().setKind("Token").newKey(data.getToken().tokenID);
		Entity token = datastore.get(tokenKey);

		if (token == null || !token.getString("token_username").equals(data.getToken().username))
			return Response.status(Status.BAD_REQUEST).entity("Operation not allowed.").build();

		Transaction txn = datastore.newTransaction();
		try {
			Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.getToken().username);
			Entity user = datastore.get(userKey);

			if (user == null || user.getString("user_status").equals("INACTIVE")) {
				txn.rollback();
				return Response.status(Status.CONFLICT).entity("Operation not allowed.").build();
			}

			String name = data.getName();
			if (name == "" || name == null)
				name = user.getString("user_name");

			String pwd = data.getPassword();
			if (pwd == "" || pwd == null)
				pwd = user.getString("user_pwd");

			String place = data.getPlace();
			if (place == "" || place == null)
				place = user.getString("user_place");

			String country = data.getCountry();
			if (country == "" || country == null)
				country = user.getString("user_country");

			user = Entity.newBuilder(userKey).set("user_name", name).set("user_pwd", pwd)
					.set("user_email", user.getString("user_email"))
					.set("user_creation_time", user.getTimestamp("user_creation_time"))
					.set("user_place", data.getPlace()).set("user_country", data.getCountry())
					.set("user_role", user.getString("user_role")).set("user_status", user.getString("user_status"))
					.build();

			txn.put(user);
			LOG.fine("User updated.");
			txn.commit();
			return Response.ok("{}").build();

		} finally {
			if (txn.isActive())
				txn.rollback();
		}

	}
	
	

}

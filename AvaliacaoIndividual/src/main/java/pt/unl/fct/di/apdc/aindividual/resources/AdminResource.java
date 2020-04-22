package pt.unl.fct.di.apdc.aindividual.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.Transaction;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.gson.Gson;

import pt.unl.fct.di.apdc.aindividual.util.ActiveUsersAdminData;
import pt.unl.fct.di.apdc.aindividual.util.DeleteUserAdminData;
import pt.unl.fct.di.apdc.aindividual.util.RegisterAdminData;

@Path("/admin")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class AdminResource {

	private static final String ADMIN_PWD = "SECRETPWD";
	private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	private final Gson g = new Gson();

	public AdminResource() {
	}

	@GET
	@Path("/hasadmin")
	public Response hasAdmin() {

		Query<Entity> query = Query.newEntityQueryBuilder().setKind("User")
				.setFilter(PropertyFilter.eq("user_role", "ADMIN")).build();
		QueryResults<Entity> admins = datastore.run(query);

		return Response.ok(g.toJson(admins.hasNext())).build();
	}

	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response doAdminRegistration(RegisterAdminData data) {
		LOG.fine("Attempt to register admin user " + data.getUsername());

		if (!data.getPasswordAdmin().equals(ADMIN_PWD))
			return Response.status(Status.BAD_REQUEST).entity("Wrong parameter.").build();

		if (!data.validRegistration())
			return Response.status(Status.BAD_REQUEST).entity("Missing or wrong parameter.").build();

		Transaction txn = datastore.newTransaction(); // Inicio da transacao
		try {
			Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.getUsername());
			Entity user = datastore.get(userKey);
			if (user != null) {
				txn.rollback(); // Aborta a transacao
				return Response.status(Status.BAD_REQUEST).entity("User already exists.").build();
			} else {
				user = Entity.newBuilder(userKey).set("user_name", data.getName())
						.set("user_pwd", DigestUtils.sha512Hex(data.getPassword())).set("user_email", data.getEmail())
						.set("user_creation_time", Timestamp.now()).set("user_place", data.getPlace())
						.set("user_country", data.getCountry()).set("user_role", "ADMIN").set("user_status", "ACTIVE")
						.build();

				txn.add(user); // Executa a escrita
				LOG.fine("User " + data.getUsername() + " registered successfully.");
				txn.commit(); // Confirma transacao
				return Response.ok("{}").build();
			}
		} finally {
			if (txn.isActive())
				txn.rollback();
		}

	}

	@GET
	@Path("/users")
	public Response activeUsers(ActiveUsersAdminData data) {

		if (data.getToken() == null || data.getToken().expirationData < System.currentTimeMillis())
			return Response.status(Status.UNAUTHORIZED).entity("The session has expired.").build();

		Key tokenKey = datastore.newKeyFactory().setKind("Token").newKey(data.getToken().tokenID);
		Entity token = datastore.get(tokenKey);

		if (token == null || !token.getString("token_username").equals(data.getToken().username))
			return Response.status(Status.BAD_REQUEST).entity("Operation not allowed.").build();

		Key userAdminKey = datastore.newKeyFactory().setKind("User").newKey(data.getToken().username);
		Entity userAdmin = datastore.get(userAdminKey);

		if (userAdmin == null || !userAdmin.getString("user_role").equals("ADMIN"))
			return Response.status(Status.BAD_REQUEST).entity("Operation not allowed.").build();

		Query<Entity> query = Query.newEntityQueryBuilder().setKind("User")
				.setFilter(PropertyFilter.eq("user_status", "ACTIVE")).build();
		QueryResults<Entity> users = datastore.run(query);

		List<String> activeUsers = new ArrayList<String>();

		while (users.hasNext()) {

			activeUsers.add(users.next().getKey().getName().toString());
		}

		return Response.ok(g.toJson(activeUsers)).build();
	}

	@DELETE
	@Path("/deleteusers")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteUsers(DeleteUserAdminData data) {

		if (data.getToken() == null || data.getToken().expirationData < System.currentTimeMillis())
			return Response.status(Status.UNAUTHORIZED).entity("The session has expired.").build();

		Key tokenKey = datastore.newKeyFactory().setKind("Token").newKey(data.getToken().tokenID);
		Entity token = datastore.get(tokenKey);

		if (token == null || !token.getString("token_username").equals(data.getToken().username))
			return Response.status(Status.BAD_REQUEST).entity("Operation not allowed.").build();

		Key userAdminKey = datastore.newKeyFactory().setKind("User").newKey(data.getToken().username);
		Entity userAdmin = datastore.get(userAdminKey);

		if (!userAdmin.getString("user_role").equals("ADMIN"))
			return Response.status(Status.BAD_REQUEST).entity("Operation not allowed.").build();

		Transaction txn = datastore.newTransaction();
		try {
			Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.getUser());
			Entity user = datastore.get(userKey);

			user = Entity.newBuilder(userKey).set("user_name", user.getString("user_name"))
					.set("user_pwd", user.getString("user_pwd")).set("user_email", user.getString("user_email"))
					.set("user_creation_time", user.getTimestamp("user_creation_time"))
					.set("user_place", user.getString("user_place")).set("user_country", user.getString("user_country"))
					.set("user_role", user.getString("user_role")).set("user_status", "INACTIVE").build();

			txn.put(user);

			Query<Entity> query = Query.newEntityQueryBuilder().setKind("Token")
					.setFilter(PropertyFilter.eq("token_username", data.getUser())).build();
			QueryResults<Entity> tokenQuery = datastore.run(query);

			Entity tokenUser = null;

			while (tokenQuery.hasNext()) {
				tokenUser = tokenQuery.next();
				txn.delete(tokenUser.getKey());
			}				

			LOG.fine("User deleted.");
			txn.commit();
			return Response.ok("{}").build();

		} finally {
			if (txn.isActive())
				txn.rollback();
		}

	}

}

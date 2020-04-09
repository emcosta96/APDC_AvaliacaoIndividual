package pt.unl.fct.di.apdc.aindividual.resources;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.gson.Gson;
import com.google.cloud.datastore.Transaction;
import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;

import pt.unl.fct.di.apdc.aindividual.util.RegisterAdminData;
import pt.unl.fct.di.apdc.aindividual.util.RegisterData;

@Path("/register")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class RegisterResource {

	/**
	 * Logger Object
	 */
	private static final String ADMIN_PWD = "SECRETPWD";
	private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	
	private final Gson g = new Gson();


	public RegisterResource() {
	}

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response doRegistration(RegisterData data) {
		LOG.fine("Attempt to register user " + data.getUsername());

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
						.set("user_pwd", DigestUtils.sha512Hex(data.getPassword()))
						.set("user_email", data.getEmail())
						.set("user_creation_time", Timestamp.now())
						.set("user_place", data.getPlace())
						.set("user_country", data.getCountry())
						.set("user_role", "END_USER")
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
	@Path("/hasadmin")
	public Response hasAdmin() {
		
		Query<Entity> query = Query.newEntityQueryBuilder().setKind("User").setFilter(PropertyFilter.eq("user_role", "ADMIN")).build();
		QueryResults<Entity> admins = datastore.run(query);
		
		return Response.ok(g.toJson(admins.hasNext())).build();
	}
	
	@POST
	@Path("/admin")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response doAdminRegistration(RegisterAdminData data) {
		LOG.fine("Attempt to register admin user " + data.getUsername());
		
		if(!data.getPasswordAdmin().equals(ADMIN_PWD))
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
						.set("user_pwd", DigestUtils.sha512Hex(data.getPassword()))
						.set("user_email", data.getEmail())
						.set("user_creation_time", Timestamp.now())
						.set("user_place", data.getPlace())
						.set("user_country", data.getCountry())
						.set("user_role", "ADMIN")
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
	
	
	

}

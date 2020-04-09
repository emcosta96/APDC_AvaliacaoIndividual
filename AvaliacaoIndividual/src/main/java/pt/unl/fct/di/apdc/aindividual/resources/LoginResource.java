package pt.unl.fct.di.apdc.aindividual.resources;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Transaction;
import com.google.gson.Gson;

import pt.unl.fct.di.apdc.aindividual.util.AuthToken;
import pt.unl.fct.di.apdc.aindividual.util.LoginData;
import pt.unl.fct.di.apdc.aindividual.util.TokenUserData;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class LoginResource {

	/**
	 * Logger Object
	 */
	private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	private final KeyFactory userKeyFactory = datastore.newKeyFactory().setKind("User");
	private final KeyFactory tokenKeyFactory = datastore.newKeyFactory().setKind("Token");

	private final Gson g = new Gson();

	public LoginResource() {
	}

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response doLogin(LoginData data) {
		LOG.fine("Attempt to login user " + data.getUsername());

		Transaction txn = datastore.newTransaction();

		try {
			Key userkey = userKeyFactory.newKey(data.getUsername());
			Entity user = datastore.get(userkey);
			if (user != null) {
				String hashedPWD = user.getString("user_pwd");
				if (hashedPWD.equals(DigestUtils.sha512Hex(data.getPassword()))) {
					AuthToken at = new AuthToken(data.getUsername());
					Key tokenKey = tokenKeyFactory.newKey(at.getTokenID());
					Entity token = Entity.newBuilder(tokenKey).set("token_username", at.getUsername())
							.set("token_creation_data", at.getCreationData())
							.set("token_expiration_data", at.getExpirationData()).build();
					txn.add(token);
					LOG.fine("User " + data.getUsername() + " logged in successfully.");
					txn.commit();
					TokenUserData tu= new TokenUserData(at.getUsername(), at.getTokenID(), at.getExpirationData());
					return Response.ok(g.toJson(tu)).build();
				}
				txn.rollback();
				return Response.status(Status.FORBIDDEN).entity("Wrong username or password.").build();
			}
			txn.rollback();
			return Response.status(Status.FORBIDDEN).entity("Failed login attempt for username: " + data.getUsername())
					.build();
		} finally {
			if (txn.isActive())
				txn.rollback();
		}

	}

}

package pt.unl.fct.di.apdc.aindividual.util;

public class ActiveUsersAdminData {

	private TokenUserData token;

	public ActiveUsersAdminData() {

	}

	public ActiveUsersAdminData(TokenUserData token) {

		this.token = token;
	}

	public TokenUserData getToken() {

		return token;
	}

}

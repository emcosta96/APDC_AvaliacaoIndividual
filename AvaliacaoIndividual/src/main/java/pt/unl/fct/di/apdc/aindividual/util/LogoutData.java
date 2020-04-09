package pt.unl.fct.di.apdc.aindividual.util;

public class LogoutData {

	private TokenUserData token;

	public LogoutData() {
	}

	public LogoutData(TokenUserData token) {

		this.token = token;
	}

	public TokenUserData getToken() {

		return token;
	}

}

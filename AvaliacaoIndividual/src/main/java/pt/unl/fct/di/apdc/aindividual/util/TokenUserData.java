package pt.unl.fct.di.apdc.aindividual.util;

public class TokenUserData {

	
	public String username;
	public String tokenID;
	public long expirationData;

	public TokenUserData() {

	}

	public TokenUserData(String username, String tokenID, long expirationData) {
		this.username = username;
		this.tokenID = tokenID;
		this.expirationData = expirationData;
	}

	public String getUsername() {

		return username;
	}

	public String getTokenID() {

		return tokenID;
	}

	public long getExpirationData() {

		return expirationData;
	}

}

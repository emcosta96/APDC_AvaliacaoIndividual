package pt.unl.fct.di.apdc.aindividual.util;

public class DeleteData {

	private String password;
	private String confirmation;
	private TokenUserData token;

	public DeleteData() {

	}

	public DeleteData(String password, String confirmation, TokenUserData token) {

		this.password = password;
		this.confirmation = confirmation;
		this.token = token;
	}

	public String getPassword() {

		return password;
	}

	public String getConfirmation() {

		return confirmation;
	}
	
	public TokenUserData getToken() {
		
		return token;
	}

	private boolean validPassword(String value) {
		return value != null && !value.equals("") && value.length() >= 8;
	}

	public boolean validDelete() {
		return validPassword(password) && validPassword(confirmation) && password.equals(confirmation);
	}

}

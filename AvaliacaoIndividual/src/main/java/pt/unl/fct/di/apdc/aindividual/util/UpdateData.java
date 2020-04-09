package pt.unl.fct.di.apdc.aindividual.util;

public class UpdateData {

	private String name;
	private String password;
	private String confirmation;
	private String place;
	private String country;
	private TokenUserData token;

	public UpdateData() {
	}

	public UpdateData(String name, String password, String confirmation, String place, String country, TokenUserData token) {
		this.name = name;
		this.password = password;
		this.confirmation = confirmation;
		this.place = place;
		this.country = country;
		this.token = token;
	}

	private boolean validField(String value) {
		return value != null;
	}

	private boolean validPassword(String value) {
		if (value == null)
			return false;

		if (!value.equals(""))
			return value.length() >= 8;
		else
			return true;
	}

	public boolean validUpdate() {
		return validField(name) && validPassword(password) && validPassword(confirmation)
				&& password.equals(confirmation);
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public String getConfirmation() {
		return confirmation;
	}

	public String getPlace() {
		return place;
	}

	public String getCountry() {
		return country;
	}

	public TokenUserData getToken() {
		return token;
	}

}

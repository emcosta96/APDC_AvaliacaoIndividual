package pt.unl.fct.di.apdc.aindividual.util;

import org.apache.commons.validator.routines.DomainValidator;

public class RegisterData {

	private String username;
	private String name;
	private String email;
	private String password;
	private String confirmation;
	private String place;
	private String country;

	public RegisterData() {
	}

	public RegisterData(String username, String name, String email, String password, String confirmation, String place,
			String country) {

		this.username = username;
		this.name = name;
		this.email = email;
		this.password = password;
		this.confirmation = confirmation;
		this.place = place;
		this.country = country;
	}

	private boolean validField(String value) {
		return value != null && !value.equals("");
	}

	private boolean validPassword(String value) {
		return value != null && !value.equals("") && value.length() >= 8;
	}

	private boolean validEmail(String value) {

		String[] values = value.split("@");
		if (values.length != 2)
			return false;

		values = values[1].split("[.]");
		if (values.length != 2)
			return false;
		return DomainValidator.getInstance().isValidTld("." + values[1]);
	}

	public boolean validRegistration() {
		return validField(username) && validField(name) && validEmail(email) && validPassword(password)
				&& validPassword(confirmation) && password.equals(confirmation) && email.contains("@");
	}

	public String getUsername() {

		return username;
	}

	public String getName() {

		return name;
	}

	public String getEmail() {

		return email;
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

}

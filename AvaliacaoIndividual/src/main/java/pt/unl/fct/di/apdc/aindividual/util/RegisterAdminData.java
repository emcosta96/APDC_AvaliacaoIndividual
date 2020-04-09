package pt.unl.fct.di.apdc.aindividual.util;

import org.apache.commons.validator.routines.DomainValidator;

public class RegisterAdminData {

	private String username;
	private String name;
	private String email;
	private String adminPwd;
	private String password;
	private String confirmation;
	private String place;
	private String country;

	public RegisterAdminData() {
	}

	public RegisterAdminData(String username, String name, String email, String adminPwd, String password,
			String confirmation, String place, String country) {

		this.username = username;
		this.name = name;
		this.email = email;
		this.adminPwd = adminPwd;
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
		return validField(username) && validField(name) && validField(adminPwd) && validEmail(email)
				&& validPassword(password) && validPassword(confirmation) && password.equals(confirmation)
				&& email.contains("@");
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

	public String getPasswordAdmin() {

		return adminPwd;
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

	public void setAdminPwd(String adminPwd) {
		this.adminPwd = adminPwd;
	}
}

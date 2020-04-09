package pt.unl.fct.di.apdc.aindividual.util;

public class DeleteUserAdminData {

	private TokenUserData token;
	private String userToDelete;

	public DeleteUserAdminData() {
	}

	public DeleteUserAdminData(TokenUserData token, String userToDelete) {
		this.token = token;
		this.userToDelete = userToDelete;
	}
	
	public TokenUserData getToken() {
		
		return token;
	}
	
	public String getUser() {
		
		return userToDelete;
	}
	
	public void setUserToDelete(String userToDelete) {
		this.userToDelete = userToDelete;
	}

}

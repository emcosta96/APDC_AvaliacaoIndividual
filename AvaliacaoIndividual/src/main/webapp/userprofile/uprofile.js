

function updateuser() {

	window.location = "updateuser/updateuser.html";
}

function deleteuser() {

	window.location = "deleteuser/deleteuser.html";
}

function signout() {
	
	xmlhttp = new XMLHttpRequest();
	
	var token = JSON.parse(localStorage.getItem('token'));
	
	var myObj = {
			username : token.username,
			tokenID : token.tokenID,
			expirationData : token.expirationData
		};
	
	console.log(myObj);
	
	xmlhttp.onreadystatechange = function() {

		if (xmlhttp.readyState == 4) {
			if (xmlhttp.status == 200)
				window.location = "/index.html"
		}
	}

	xmlhttp.open("DELETE", window.location.protocol + "/rest/logout", true);
	xmlhttp.setRequestHeader("Content-Type", "application/json");
	
	var myJSON = JSON.stringify(myObj);
	xmlhttp.send(myJSON);

}
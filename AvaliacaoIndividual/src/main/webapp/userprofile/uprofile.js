
xmlhttp = new XMLHttpRequest();

function updateuser() {

	window.location = "updateuser/updateuser.html";
}

function deleteuser() {

	window.location = "deleteuser/deleteuser.html";
}

function signout() {

	xmlhttp.onreadystatechange = function() {

		if (xmlhttp.readyState == 4) {
			if (xmlhttp.status == 200)
				window.location = "/index.html"
		}
	}

	xmlhttp.open("DELETE", "http://localhost:8080/rest/logout", true);
	xmlhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
	var myJSON = JSON.stringify(localStorage.getItem('token'));
	xmlhttp.send(myJSON);
}
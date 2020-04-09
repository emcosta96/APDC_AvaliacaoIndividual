xmlhttp = new XMLHttpRequest();

function login() {

	var myObj = {
		username : document.getElementById("username").value,
		password : document.getElementById("password").value
	};
	
	xmlhttp.onreadystatechange = function() {

		if (xmlhttp.readyState == 4) {
			if (xmlhttp.status == 200) {
				localStorage.setItem('token', xmlhttp.responseText);
				window.location = "/userprofile/uprofile.html";
			}
		}
	}

	xmlhttp.open("POST", window.location.protocol + "/rest/login", true);
	xmlhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
	var myJSON = JSON.stringify(myObj);
	xmlhttp.send(myJSON);
}
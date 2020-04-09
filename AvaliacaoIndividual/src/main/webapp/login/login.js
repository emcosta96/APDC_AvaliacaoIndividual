xmlhttp = new XMLHttpRequest();

function login() {

	var myObj = {
		username : document.getElementById("username").value,
		password : document.getElementById("password").value
	};
	console.log(myObj);
	xmlhttp.onreadystatechange = function() {

		if (xmlhttp.readyState == 4) {
			if (xmlhttp.status == 200) {
				var token = JSON.parse(xmlhttp.responseText);
				localStorage.setItem('token', token);
				window.location = "/userprofile/uprofile.html";
			}
		}
	}

	xmlhttp.open("POST", "http://localhost:8080/rest/login", true);
	xmlhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
	var myJSON = JSON.stringify(myObj);
	console.log(myJSON);
	xmlhttp.send(myJSON);
}
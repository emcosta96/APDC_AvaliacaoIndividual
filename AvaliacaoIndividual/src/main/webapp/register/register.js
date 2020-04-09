
xmlhttp = new XMLHttpRequest();

function register(){
	
	var myObj = {
				username: document.getElementById("username").value,
				name: document.getElementById("name").value,
				email: document.getElementById("email").value,
				password: document.getElementById("password").value,
				confirmation: document.getElementById("confirmation").value,
				place: document.getElementById("place").value,
				country: document.getElementById("country").value
				};
	
	
	
	xmlhttp.onreadystatechange = function() {

		if (xmlhttp.readyState == 4) {
			if (xmlhttp.status == 200)
				window.location = "/index.html"
		}
	}

	xmlhttp.open("POST", "http://localhost:8080/rest/register", true);
	xmlhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
	var myJSON = JSON.stringify(myObj);
	xmlhttp.send(myJSON);	
}
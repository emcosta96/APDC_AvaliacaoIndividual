xmlhttp = new XMLHttpRequest();

function login() {
	window.location = "login/login.html";

}

function register() {
	window.location = "register/register.html";

}

function init() {

	xmlhttp.onreadystatechange = function() {

		if (xmlhttp.readyState == 4) {
			if (xmlhttp.status == 200) {
				var hasadmin = JSON.parse(xmlhttp.responseText);
				if (hasadmin == false)
					window.location = "admin/admin.html"
			}
		}
	}

	xmlhttp.open("GET","http://localhost:8080/rest/register/hasadmin",true);
	xmlhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
	xmlhttp.send(null);

}
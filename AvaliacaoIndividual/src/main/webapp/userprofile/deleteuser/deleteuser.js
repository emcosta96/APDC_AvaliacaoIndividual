
xmlhttp = new XMLHttpRequest();

function deleteuser(){
	
	var myObj = {
				password: document.getElementById("password").value,
				confirmation: document.getElementById("confirmation").value,
				token: JSON.parse(localStorage.getItem('token'))
				};
	
	
	
	xmlhttp.onreadystatechange = function() {

		if (xmlhttp.readyState == 4) {
			if (xmlhttp.status == 200)
				window.location = "/../index.html"
		}
	}

	xmlhttp.open("DELETE", window.location.protocol + "/rest/delete", true);
	xmlhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
	var myJSON = JSON.stringify(myObj);
	xmlhttp.send(myJSON);	
}
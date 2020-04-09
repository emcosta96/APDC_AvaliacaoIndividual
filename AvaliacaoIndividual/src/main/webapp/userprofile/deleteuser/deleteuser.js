
xmlhttp = new XMLHttpRequest();

function deleteuser(){
	
	var myObj = {
				password: document.getElementByID("password").value,
				confirmation: document.getElementByID("confirmation").value,
				token: localStorage.getItem('token')
				};
	
	
	
	xmlhttp.onreadystatechange = function() {

		if (xmlhttp.readyState == 4) {
			if (xmlhttp.status == 200)
				window.location = "/../index.html"
		}
	}

	xmlhttp.open("DELETE", "http://localhost:8080/rest/delete", true);
	xmlhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
	var myJSON = JSON.stringify(myObj);
	xmlhttp.send(myJSON);	
}

xmlhttp = new XMLHttpRequest();

function updateuser(){
	
	var myObj = {
				name: document.getElementByID("name").value,
				password: document.getElementByID("password").value,
				confirmation: document.getElementByID("confirmation").value,
				place: document.getElementByID("place").value,
				country: document.getElementByID("country").value,
				token: localStorage.getItem('token')
				};
	
	
	
	xmlhttp.onreadystatechange = function() {

		if (xmlhttp.readyState == 4) {
			if (xmlhttp.status == 200)
				window.location = "/uprofile.html"
		}
	}

	xmlhttp.open("PUT", "http://localhost:8080/rest/update", true);
	xmlhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
	var myJSON = JSON.stringify(myObj);
	xmlhttp.send(myJSON);	
}
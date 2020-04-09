
xmlhttp = new XMLHttpRequest();

function updateuser(){
	
	var myObj = {
				name: document.getElementById("name").value,
				password: document.getElementById("password").value,
				confirmation: document.getElementById("confirmation").value,
				place: document.getElementById("place").value,
				country: document.getElementById("country").value,
				token: JSON.parse(localStorage.getItem('token'))
				};
	
	
	
	xmlhttp.onreadystatechange = function() {

		if (xmlhttp.readyState == 4) {
			if (xmlhttp.status == 200)				
				window.location = "/userprofile/uprofile.html";
		}
	}

	xmlhttp.open("PUT", window.location.protocol + "/rest/update", true);
	xmlhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
	var myJSON = JSON.stringify(myObj);
	xmlhttp.send(myJSON);	
}
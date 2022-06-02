function logout() {
	alert("logout");
    window.localStorage.removeItem("name");
    window.localStorage.removeItem("role");
    window.location.replace("login");
}
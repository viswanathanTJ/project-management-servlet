function logout() {
    if (confirm("Are you sure to logout")) {
        window.localStorage.removeItem("name");
        window.localStorage.removeItem("role");
        window.location.replace("login");
    }
}
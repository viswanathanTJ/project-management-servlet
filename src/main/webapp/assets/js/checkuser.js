function getCookie(cname) {
  let name = cname + "=";
  let decodedCookie = decodeURIComponent(document.cookie);
  let ca = decodedCookie.split(";");
  for (let i = 0; i < ca.length; i++) {
    let c = ca[i];
    while (c.charAt(0) == " ") {
      c = c.substring(1);
    }
    if (c.indexOf(name) == 0) {
      return c.substring(name.length, c.length);
    }
  }
  return "";
}

let username = getCookie("username");
if (username != "") {
    window.location.replace("/");
} else {
    const data = "name=" + username + "&role=" + role;
    $.ajax({
      url: "check",
      type: "GET",
      data: data,
      success: function (resp) {
        console.log(resp);
        window.location.replace(resp.role);
        setCookie("username", resp.name, 1);
        setCookie("role", resp.role, 1);
        $("#registerBtn").removeClass("loading");
      },
      error: function (resp) {
        if (resp.responseText == "Mail ID already exists.")
          $("#email1").val("");
        else if (resp.responseText == "Username already exists.")
          $("#name1").val("");
        else if (resp.responseText == "Password Mismatch") {
          $("#password1").val("");
          $("#cpassword1").val("");
        }
        $("#registerBtn").removeClass("loading");
        error.innerHTML = resp.responseText;
      },
    });
}

function logout() {
  if (confirm("Are you sure to logout")) {
    window.localStorage.removeItem("name");
    window.localStorage.removeItem("role");
    window.location.replace("login");
  }
}

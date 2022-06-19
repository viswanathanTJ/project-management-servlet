// const role = CookieManager.getCookie({ name: "role" });
const crole = window.location.pathname.split("/").pop(); // admin
if (crole != "login")
  $.ajax({
    url: "check",
    type: "GET",
    success: function (resp) {
      if (!crole.startsWith(resp)) window.location.replace(resp);
    },
    error: function () {
      window.location.replace("login");
    },
  });
var cookie_username;
$(document).ready(function () {
  var profile_img = document.getElementById("profile_img");
  if (profile_img instanceof Element) {
    cookie_username = getCookie("username");
    profile_img.innerHTML = cookie_username.charAt(0).toUpperCase();
  }
});

function setCookie(cname, cvalue, exdays) {
  const d = new Date();
  d.setTime(d.getTime() + exdays * 24 * 60 * 60 * 1000);
  let expires = "expires=" + d.toUTCString();
  document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

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

function deleteAllCookies() {
  var cookies = document.cookie.split(";");

  for (var i = 0; i < cookies.length; i++) {
    var cookie = cookies[i];
    var eqPos = cookie.indexOf("=");
    var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
    document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
  }
}

function logout() {
  if (confirm("Are you sure to logout?")) {
    $.ajax({
      url: "logout",
      type: "GET",
    });
    deleteAllCookies();
    window.location.replace("login");
  }
}

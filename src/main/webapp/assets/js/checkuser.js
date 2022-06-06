const role = CookieManager.getCookie({ name: "role" });
const crole = window.location.pathname.split("/").pop();
$.ajax({
  url: "check",
  type: "POST",
  data: "role=" + role,
  success: function (resp) {
    if (resp != crole) window.location.replace("login");
  },
  error: function () {
    window.location.replace("login");
  },
});

function logout() {
  if (confirm("Are you sure to logout?")) {
    CookieManager.removeCookie({ name: "name" });
    CookieManager.removeCookie({ name: "role" });
    window.location.replace("login");
  }
}

$(document).ready(function () {
  const role = CookieManager.getCookie({ name: "role" });
  $.ajax({
    url: "check",
    type: "POST",
    data: { role: role },
    success: function (resp) {
      window.location.replace(resp);
    },
    error: function () {
      window.location.replace("login");
    },
  });
});
function logout() {
  if (confirm("Are you sure to logout")) {
    window.localStorage.removeItem("name");
    window.localStorage.removeItem("role");
    window.location.replace("login");
  }
}

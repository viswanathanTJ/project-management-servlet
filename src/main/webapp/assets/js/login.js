console.log(window.location.pathname.split("/").pop());
$(document).ready(function () {
  $("form[name=register]").submit(function (e) {
    $("#registerBtn").addClass("loading");
    var $form = $(this);
    var data = $form.serialize();
    var error = document.getElementById("error");
    error.innerHTML = "";
    $.ajax({
      url: "register",
      type: "POST",
      data: data,
      success: function (resp) {
        console.log(resp);
        CookieManager.setCookie({
          name: "name",
          value: resp.name,
          days: 1,
        });
        CookieManager.setCookie({
          name: "role",
          value: resp.role,
          days: 1,
        });
        $("#registerBtn").removeClass("loading");
        window.location.replace("employee");
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

    e.preventDefault();
  });

  $("form[name=login]").submit(function (e) {
    $("#loginBtn").addClass("loading");
    var $form = $(this);
    var data = $form.serialize();
    var error = document.getElementById("error1");
    error.innerHTML = "";
    console.log(data);
    $.ajax({
      url: "Login",
      type: "POST",
      data: data,
      success: function (resp) {
        error.innerHTML = "Login Successful";
        $("#loginBtn").removeClass("loading");
        console.log(resp);
        CookieManager.setCookie({
          name: "name",
          value: resp.name,
          days: 1,
        });
        CookieManager.setCookie({
          name: "role",
          value: resp.role,
          days: 1,
        });
        console.log(resp.role);
        window.location.replace(resp.redirect);
        $("#loginBtn").removeClass("loading");
      },
      error: function (resp) {
        if (resp.responseText == "Invalid Password.") $("#password").val("");
        else $("#login")[0].reset();
        $("#loginBtn").removeClass("loading");
        error.innerHTML = resp.responseText;
      },
    });

    e.preventDefault();
  });

  function togglePassword(el) {
    var checked = el.checked;
    if (checked) {
      document.getElementById("password").type = "text";
      document.getElementById("toggleText").textContent = "Hide";
    } else {
      document.getElementById("password").type = "password";
      document.getElementById("toggleText").textContent = "Show";
    }
  }
});

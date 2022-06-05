function setCookie(cname, cvalue, exdays) {
  const d = new Date();
  d.setTime(d.getTime() + exdays * 24 * 60 * 60 * 1000);
  let expires = "expires=" + d.toUTCString();
  document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}



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
        window.location.replace(resp.role);
        setCookie('username', resp.name , 1);
        setCookie('role', resp.role , 1);
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
        window.location.replace(resp.role);
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

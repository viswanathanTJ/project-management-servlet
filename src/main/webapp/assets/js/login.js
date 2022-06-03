$("form[name=register]").submit(function (e) {
  $("#registerBtn").addClass("loading");
  var $form = $(this);
  var data = $form.serialize();
  var error = document.getElementById("error");
  error.innerHTML = "";
  $.ajax({
    url: "Register",
    type: "POST",
    data: data,
    success: function (resp) {
      var res = JSON.parse(resp);
      window.localStorage.setItem("name", res.name);
      window.localStorage.setItem("role", res.role);
      error.innerHTML = "Successfully Registered";
      window.location.replace(res.role);
    },
    error: function (resp) {
      if (resp.responseText == "Mail ID already exists.") $("#email1").val("");
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
  $.ajax({
    url: "Login",
    type: "POST",
    data: data,
    success: function (resp) {
      var res = JSON.parse(resp);
      window.localStorage.setItem("name", res.name);
      window.localStorage.setItem("role", res.role);
      error.innerHTML = "Successfully Login";
      window.location.replace(res.role);
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

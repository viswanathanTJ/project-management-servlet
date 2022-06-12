const lusername = document.getElementById("lname");
const lpassword = document.getElementById("lpassword");

lusername.addEventListener("keyup", function () {
  validator(lusername, nameRegex);
});

lpassword.addEventListener("keyup", function () {
  validator(lpassword, passwordRegex);
});

$(document).ready(function () {
  $("form[name=register]").submit(function (e) {
    $("#registerBtn").addClass("loading");
    var $form = $(this);
    var data = $form.serialize();
    var error = document.getElementById("error");
    error.innerHTML = "";
    if (
      !username.value.match(nameRegex) ||
      !password.value.match(passwordRegex) ||
      !email.value.match(emailRegex) ||
      !cpassword.value.match(passwordRegex) ||
      !cpassword.value == password.value
    ) {
      $("#registerBtn").removeClass("loading");
      error.innerHTML = "Please correct all the fields";
      return false;
    }
    $.ajax({
      url: "register",
      type: "POST",
      data: data,
      success: function (resp) {
        setCookie("username", username.value, 1);
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
    if (
      username.value.match(nameRegex) ||
      password.value.match(passwordRegex)
    ) {
      $("#loginBtn").removeClass("loading");
      error.innerHTML = "Please correct all the fields";
      return false;
    }
    $.ajax({
      url: "Login",
      type: "POST",
      data: data,
      success: function (resp) {
        error.innerHTML = "Login Successful";
        setCookie("username", username.value, 1);
        $("#loginBtn").removeClass("loading");
        console.log(resp);
        window.location.replace(resp);
        $("#loginBtn").removeClass("loading");
      },
      error: function (resp) {
        if (resp.responseText == "Invalid Password.") {
          alert("changed");
          lpassword.style.borderColor = "red";
          lpassword.value("");
        }
        $("#loginBtn").removeClass("loading");
        error.innerHTML = resp.responseText;
      },
    });

    e.preventDefault();
  });
});

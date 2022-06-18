const email = document.getElementById("email");
const username = document.getElementById("name");
const password = document.getElementById("password");
const emailHelp = document.getElementById("emailHelp");
const usernameHelp = document.getElementById("nameHelp");
const passwordHelo = document.getElementById("passwordHelp");
const cpassword = document.getElementById("cpassword");
const error = document.getElementById("error");

var emailRegex =
  /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

var nameRegex = /^[a-zA-Z0-9]{3,}$/;

var passwordRegex =
  /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{4,}$/;

function validator(element, regex, message) {
  element.addEventListener("keyup", function () {
    if (element.value.match(regex)) {
      element.style.borderColor = "green";
      error.innerHTML = "";
    } else {
      element.style.borderColor = "red";
      error.innerHTML = message;
    }
  });
}

["keyup", "focus"].forEach((evt) =>
  password.addEventListener(evt, function () {
    validator(
      password,
      passwordRegex,
      "Password must contain one uppercase, one lowercase, one number and one special character"
    );
  })
);

["keyup", "focus"].forEach((evt) =>
  username.addEventListener(evt, function () {
    validator(
      username,
      nameRegex,
      "Username must contain atleast 3 characters"
    );
  })
);

["keyup", "focus"].forEach((evt) =>
  email.addEventListener(evt, function () {
    validator(email, emailRegex, "Email must be valid");
  })
);

if (cpassword instanceof Element) {
  cpassword.addEventListener("keyup", function () {
    if (password.value != cpassword.value) {
      cpassword.style.borderColor = "red";
      error.innerHTML = "Password must match with confirm password";
    } else {
      cpassword.style.borderColor = "green";
      error.innerHTML = "";
    }
  });
}

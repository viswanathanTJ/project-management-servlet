const email = document.getElementById("email");
const username = document.getElementById("name");
const password = document.getElementById("password");
const cpassword = document.getElementById("cpassword");

var emailRegex =
  /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

var nameRegex = /^[a-zA-Z0-9]{3,}$/;

var passwordRegex =
  /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{4,}$/;

function validator(element, regex) {
  element.addEventListener("keyup", function () {
    if (element.value.match(regex)) {
      element.style.borderColor = "green";
    } else {
      element.style.borderColor = "red";
    }
  });
}

["keyup", "focus"].forEach((evt) =>
  password.addEventListener(evt, function () {
    validator(password, passwordRegex);
  })
);

["keyup", "focus"].forEach((evt) =>
  username.addEventListener(evt, function () {
    validator(username, nameRegex);
  })
);

["keyup", "focus"].forEach((evt) =>
  email.addEventListener(evt, function () {
    validator(email, emailRegex);
  })
);


cpassword.addEventListener("keyup", function () {
  if (password.value != cpassword.value) {
    cpassword.style.borderColor = "red";
  } else {
    cpassword.style.borderColor = "green";
  }
});

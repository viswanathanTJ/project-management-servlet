function login() {
  var user;
  $.ajax({
    type: "GET",
    url: "register?action=find",
    headers: {
      Accept: "application/json; charset=utf-8",
      "Content-Type": "application/json; charset=utf-8",
    },
    success: function (result) {
      user = $.parseJSON(result);
      alert(user.Name);
      var name = document.getElementById("name");
      name.innerHTML = user.Name;
    },
  });
}

$("form[name=register").submit(function (e) {
  var $form = $(this);
  var data = $form.serialize();
  var error = document.getElementById("error");

  $.ajax({
    url: "Register",
    type: "POST",
    data: data,
    success: function (resp) {
      error.innerHTML = "Successfully Registered";
    },
    error: function(resp) {
		error.innerHTML = resp.responseText;
	}
  });

  e.preventDefault();
});

function register() {
  alert("hi");

  var name = document.getElementById("name");
  name.style.background = "blue";
  $(".password").css("background", "red");
  var email = document.getElementById("email");
  var password = document.getElementById("password");
  var cpassword = document.getElementById("cpassword");
  if (password != cpassword) $("#password,#cpassword").css("color", "red");
  var user;
  $.ajax({
    type: "POST",
    url: "Register",
    headers: {
      Accept: "application/json; charset=utf-8",
      "Content-Type": "application/json; charset=utf-8",
    },
    success: function (result) {
      user = $.parseJSON(result);
      alert(user.Name);
      var name = document.getElementById("name");
      name.innerHTML = user.Name;
    },
  });
}

$(".register").click(function () {
  $.ajax({
    type: "POST",
    url: "Register",
    data: $(this).parent().serialize(),
    success: function (data) {
      alert(data);
    },
  });
  return false;
});

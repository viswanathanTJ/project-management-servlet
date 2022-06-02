$("form[name=register").submit(function (e) {
  var $form = $(this);
  var data = $form.serialize();
  var error = document.getElementById("error");

  $.ajax({
    url: "Register",
    type: "POST",
    data: data,
    success: function (resp) {
      var res = JSON.parse(resp);
      window.localStorage.setItem("name", res.name);
      window.localStorage.setItem("role", res.role);
      error.innerHTML = "Successfully Registered";
      window.location.replace("employee");
    },
    error: function (resp) {
	$("#register")[0].reset();
      error.innerHTML = resp.responseText;
    },
  });

  e.preventDefault();
});

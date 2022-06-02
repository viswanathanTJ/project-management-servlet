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
      window.localStorage.setItem("name", res.role);
      error.innerHTML = "Successfully Registered";
    },
    error: function (resp) {
      error.innerHTML = resp.responseText;
    },
  });

  e.preventDefault();
});

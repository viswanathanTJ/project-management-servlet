$(document).ready(function() {
  $("form[name=add_user]").submit(function (e) {
    alert("hi");
      var $form = $(this);
      var data = $form.serialize();
      var error = document.getElementById("add-error");
      var msg = document.getElementById("message");
      error.innerHTML = "";
      $.ajax({
        url: "AddUser",
        type: "POST",
        data: data,
        success: function (resp) {
          alert(resp.responseText);
          error.innerHTML = "Added Successfully";
          $("#add_user")[0].reset();
          msg.innerHTML = resp.responseText;
        },
        error: function (resp) {
          alert(resp.responseText);
          $("#createBtn").removeClass("loading");
          error.innerHTML = resp.responseText;
        },
      });

    e.preventDefault();
  });
});
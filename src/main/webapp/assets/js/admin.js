const swipeHandler = new SwipeHandler();
const toastsFactory = new ToastsFactory(swipeHandler);


$(document).ready(function () {
  $("form[username=add-user]").submit(function (e) {
    $("#exampleModal").modal("hide");
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
        toastsFactory.createToast({
          type: "success",
          icon: "info-circle",
          message: resp.responseText,
          duration: 1000,
        });
      },
      error: function (resp) {
        toastsFactory.createToast({
          type: "error",
          icon: "info-circle",
          message: resp,
          duration: 1000,
        });
      },
    });

    e.preventDefault();
  });
});

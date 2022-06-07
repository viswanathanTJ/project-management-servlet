const swipeHandler = new SwipeHandler();
const toastsFactory = new ToastsFactory(swipeHandler);

$(document).ready(function () {
  $("form[name=add-user]").submit(function (e) {
    if (
      !username.value.match(nameRegex) ||
      !email.value.match(emailRegex) ||
      !password.value.match(passwordRegex)
    ) {
      toastsFactory.createToast({
        type: "error",
        icon: "info-circle",
        message: "Please correct all the fields",
        duration: 1000,
      });
      return false;
    }
    var $form = $(this);
    var data = $form.serialize();
    $.ajax({
      url: "AddUser",
      type: "POST",
      data: data,
      success: function (resp) {
        $("#add-user")[0].reset();
        $("#formModal").modal("hide");
        [username, email, password].forEach((ele) =>
          ele.style.removeProperty("border")
        );
        toastsFactory.createToast({
          type: "system",
          icon: "check-circle",
          message: resp,
          duration: 1000,
        });
      },
      error: function (resp) {
        toastsFactory.createToast({
          type: "error",
          icon: "info-circle",
          message: resp.responseText,
          duration: 1000,
        });
        $("#formModal").modal("show");
      },
    });

    e.preventDefault();
  });
});

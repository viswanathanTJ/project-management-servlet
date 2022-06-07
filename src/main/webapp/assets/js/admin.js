$(document).ready(function () {
  // Initialize DataTable
  var table = $("#usersTable").DataTable({
    columnDefs: [{ width: "2%", targets: 0 }],
    fixedColumns: true,
  });
  table.columns.adjust().draw();

  const swipeHandler = new SwipeHandler();
  const toastsFactory = new ToastsFactory(swipeHandler);
  var sno;

  let users;
  let xhr = new XMLHttpRequest();
  xhr.open("GET", "http://localhost:8080/Project_Management/getUsers", true);
  xhr.onload = function () {
    if (this.status === 200) {
      users = JSON.parse(this.responseText).users;
      $.each(users, function (index, item) {
        sno = index + 1;
        table.row
          .add([
            sno,
            item.name,
            item.email,
            item.role,
            item.createdat,
            '<span><i class="fas fa-edit"></i></span><span><i class="fas fa-trash"></i></span>',
          ])
          .draw(false);
      });
    }
  };
  xhr.send();

  // Form Handling
  $("form[name=add-user]").submit(function (e) {
    // Validation
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

    // Ajax Call
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
        sno += 1;
        var item = JSON.parse(resp);
        table.row
          .add([
            sno,
            item.username,
            item.email,
            item.role,
            item.joined,
            '<span><i class="fas fa-edit"></i></span><span><i class="fas fa-trash"></i></span>',
          ])
          .draw(false);
        toastsFactory.createToast({
          type: "system",
          icon: "check-circle",
          message: "Added successfully",
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

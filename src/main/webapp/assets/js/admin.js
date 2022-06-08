var sno;
var table;
function initializeDatabase() {
  table = $("#usersTable").DataTable({
    columnDefs: [
      { targets: 0, visible: false },
      { targets: 1, width: ".3%" },
      { targets: 3, width: "2%" },
      { targets: [2, 4], width: "1%" },
      { targets: 5, width: "2%" },
      {
        className: "text-center",
        targets: -1,
        width: "2%",
        defaultContent: [
          `<a href="#" id="btnEdit"><span><i class="fas fa-edit"></i></span></a>
            <a href="#" id="btnDelete"><span><i class="fas fa-trash"></i></span></a>`,
        ],
      },
    ],
    fixedColumns: true,
  });
}

function loadData() {
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
            item.uid,
            sno,
            item.name,
            item.email,
            item.role,
            item.createdat,
          ])
          .draw(false);
      });
    }
  };
  xhr.send();
}


// Edit Button
$("#usersTable").on("click", "#btnEdit", function () {
  $("#formModal").modal("show");
  var row = $(this).parents("tr")[0];
  var data = table.row(row).data();
  // Set data
  username.setAttribute("value", data[2]);
  email.setAttribute("value", data[3]);
  document.getElementById("role").value = data[4];
  // Change modal
  document.getElementById("formModalLabel").innerHTML = "Edit User";
  password.placeholder = "New Password";
});

// Delete Button 
$("#usersTable").on("click", "#btnDelete", function () {
  var row = $(this).parents("tr")[0];
  var data = table.row(row).data();
  var xhr = new XMLHttpRequest();
  xhr.open("DELETE", "http://localhost:8080/Project_Management/deleteUser/" + data[0], true);
  xhr.onload = function () {
    if (this.status === 200) {
      table.row(row).remove().draw();
    }
  };
  xhr.send();
});


$(document).ready(function () {
  // Initialize DataTable
  initializeDatabase();
  // Load DataTable
  loadData();

  const swipeHandler = new SwipeHandler();
  const toastsFactory = new ToastsFactory(swipeHandler);
  var sno;

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

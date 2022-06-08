var table;
var eusername = document.getElementById("ename");
// var epassword = document.getElementById("epassword");
var eemail = document.getElementById("eemail");
var erole = document.getElementById("erole");
var uid, rowData, row;

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
  $("#formEditModal").modal("show");
  row = $(this).parents("tr")[0];
  rowData = table.row(row).data();
  uid = rowData[0];
  // Set data
  eusername.setAttribute("value", rowData[2]);
  eemail.setAttribute("value", rowData[3]);
  erole.value = rowData[4];
});

// Delete Button
$("#usersTable").on("click", "#btnDelete", function () {
  var row = $(this).parents("tr")[0];
  var data = table.row(row).data();
  var xhr = new XMLHttpRequest();
  xhr.open(
    "DELETE",
    "http://localhost:8080/Project_Management/deleteUser/" + data[0],
    true
  );
  xhr.onload = function () {
    if (this.status === 200) {
      table.row(row).remove().draw();
    }
  };
  xhr.send();
});

$(document).ready(function () {
  var sno = 1;

  // Initialize DataTable
  initializeDatabase();
  // Load DataTable
  loadData();

  const swipeHandler = new SwipeHandler();
  const toastsFactory = new ToastsFactory(swipeHandler);

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

  // Put request
  $("form[name=edit-user]").submit(function (e) {
    var $form = $(this);
    var data = $form.serialize();
    if (
      eusername.value == rowData[2] &&
      eemail.value == rowData[3] &&
      erole.value == rowData[4]
    ) {
      toastsFactory.createToast({
        type: "system",
        icon: "info-circle",
        message: "Kindly make some changes",
        duration: 1000,
      });
      return false;
    }
    $.ajax({
      type: "POST",
      url: "updateUser",
      data: data + "&id=" + uid,
      success: function (data, status, xhr) {
        sno += 1;
        table
          .row(row)
          .data([
            uid,
            rowData[1],
            eusername.value,
            eemail.value,
            erole.value,
            rowData[5],
          ])
          .draw(false);
        $("#formEditModal").modal("hide");
        toastsFactory.createToast({
          type: "system",
          icon: "check-circle",
          message: "Updated successfully",
          duration: 1000,
        });
        $("#edit-user")[0].reset();
      },
      error: function (resp, status, error) {
        toastsFactory.createToast({
          type: "error",
          icon: "info-circle",
          message: resp.responseText,
          duration: 1000,
        });
      },
    });

    e.preventDefault();
  });
});

const username = document.getElementById("ename");
const desc = document.getElementById("edesc");
const owner = document.getElementById("eowner");

var sno, addId;
// VALIDATION
["keyup", "focus"].forEach((evt) =>
  username.addEventListener(evt, function () {
    username.addEventListener("keyup", function () {
      if (username.value.length > 10) {
        username.style.borderColor = "green";
      } else {
        username.style.borderColor = "red";
      }
    });
  })
);

["keyup", "focus"].forEach((evt) =>
  desc.addEventListener(evt, function () {
    desc.addEventListener("keyup", function () {
      if (desc.value.length > 20) {
        desc.style.borderColor = "green";
      } else {
        desc.style.borderColor = "red";
      }
    });
  })
);

function initializeDatabase() {
  table = $("#projectsTable").DataTable({
    columnDefs: [
      { targets: 0, visible: false },
      { targets: 1, width: ".2%" },
      { targets: 2, width: "2%" },
      { targets: 3, width: "4%" },
      { targets: 4, width: "1%" },
      {
        className: "text-center",
        targets: -1,
        width: "1%",
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
  let projects;
  let xhr = new XMLHttpRequest();
  xhr.open("GET", "http://localhost:8080/Project_Management/getProjects", true);
  xhr.onload = function () {
    if (this.status === 200) {
      projects = JSON.parse(this.responseText).projects;
      $.each(projects, function (index, item) {
        sno = index + 1;
        addId = item.p_id;
        table.row
          .add([item.p_id, sno, item.name, item.desc, item.oname])
          .draw(false);
      });
    }
  };
  xhr.send();
}

// Edit Button
$("#projectsTable").on("click", "#btnEdit", function () {
  $("#formEditModal").modal("show");
  row = $(this).parents("tr")[0];
  rowData = table.row(row).data();
  uid = rowData[0];
  // Set data
  username.value = rowData[2];
  desc.value = rowData[3];
  owner.value = rowData[4];
});

// Delete Button
$("#projectsTable").on("click", "#btnDelete", function () {
  $("#deleteModal").modal("show");
  row = $(this).parents("tr")[0];
  rowData = table.row(row).data();
  uid = rowData[0];
});

$(document).ready(function () {
  // Initialize DataTable
  initializeDatabase();
  const swipeHandler = new SwipeHandler();
  const toastsFactory = new ToastsFactory(swipeHandler);
  // Load DataTable
  loadData();

  // Form Handling
  $("form[name=create-project]").submit(function (e) {
    // Validation
    if (username.value.length < 10 || desc.value.length < 20) {
      toastsFactory.createToast({
        type: "error",
        icon: "info-circle",
        message:
          "Please enter project name and description with atleast 20 characters",
        duration: 1000,
      });
      return false;
    }

    // Ajax Call
    var $form = $(this);
    var data = $form.serialize();
    $.ajax({
      url: "AddProject",
      type: "POST",
      data: data,
      success: function (resp) {
        $("#create-project")[0].reset();
        $("#formModal").modal("hide");
        var item = JSON.parse(resp);
        sno = sno + 1;
        addId += 1;
        table.row
          .add([addId, sno, item.name, item.desc, item.oname])
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

  // Update Project
  $("form[name=edit-project]").submit(function (e) {
    var $form = $(this);
    var data = $form.serialize();
    if (
      username.value == rowData[2] &&
      desc.value == rowData[3] &&
      owner.value == rowData[4]
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
      url: "updateProject",
      data: data + "&id=" + uid,
      success: function (data, status, xhr) {
        table
          .row(row)
          .data([
            uid,
            rowData[1],
            username.value,
            desc.value,
            owner.value,
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

  // Delete Project
  $("form[name=delete-project]").submit(function (e) {
    $.ajax({
      type: "POST",
      url: "deleteProject",
      data: "id=" + uid,
      success: function (data, status, xhr) {
        table.row(row).remove().draw();
        $("#deleteModal").modal("hide");
        toastsFactory.createToast({
          type: "system",
          icon: "check-circle",
          message: "Deleted successfully",
          duration: 1000,
        });
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

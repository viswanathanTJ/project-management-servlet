const username = document.getElementById("name");
const desc = document.getElementById("desc");
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
      { targets: 1, width: "1%" },
      { targets: 2, width: "2%" },
      { targets: 3, width: "4%" },
      { targets: 4, width: "2%" },
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
  xhr.open("GET", "http://localhost:8080/Project_Management/getProjects", true);
  xhr.onload = function () {
    if (this.status === 200) {
      users = JSON.parse(this.responseText).projects;
      $.each(users, function (index, item) {
        sno = index + 1;
        addId = item.p_id;
        table.row
          .add([
            item.p_id,
            sno,
            item.name,
            item.desc,
            item.oname,
            item.createdat,
          ])
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

  alert(uid);
});

// Delete Button
$("#projectsTable").on("click", "#btnDelete", function () {
  $("#deleteModal").modal("show");
  row = $(this).parents("tr")[0];
  rowData = table.row(row).data();
  uid = rowData[0];
  alert(uid);
});

$(document).ready(function () {
  // Initialize DataTable
  var addId = 0,
    sno = 0;
  initializeDatabase();
  const swipeHandler = new SwipeHandler();
  const toastsFactory = new ToastsFactory(swipeHandler);
  // Load DataTable
  loadData();

  // Form Handling
  $("form[name=create-project]").submit(function (e) {
    // Validation
    // alert('create project');
    if (!username.value.length < 10 || !desc.value.length < 20) {
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
          .add([addId, sno, item.name, item.desc, item.owner, item.created])
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

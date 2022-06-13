const pname = document.getElementById("name");
const desc = document.getElementById("desc");
const eusername = document.getElementById("ename");
const edesc = document.getElementById("edesc");
const pTitle = document.getElementById("pTitle");
const listMembers = document.getElementById("listMembers");
const eowner = document.getElementById("eowner");
const ownerList = document.getElementById("ownerList");
const eownerList = document.getElementById("eownerList");
var jsonObj = {};
var sno = 0,
  pid,
  coname,
  prname,
  addId = 0;

// VALIDATION
function validator(element, count) {
  element.addEventListener("keyup", function () {
    if (element.value.length > count) {
      element.style.borderColor = "green";
    } else {
      element.style.borderColor = "red";
    }
  });
}

["keyup", "focus"].forEach((evt) =>
  pname.addEventListener(evt, function () {
    validator(pname, 3);
  })
);
["keyup", "focus"].forEach((evt) =>
  eusername.addEventListener(evt, function () {
    validator(pname, 3);
  })
);
["keyup", "focus"].forEach((evt) =>
  desc.addEventListener(evt, function () {
    validator(desc, 20);
  })
);
["keyup", "focus"].forEach((evt) =>
  edesc.addEventListener(evt, function () {
    validator(edesc, 20);
  })
);

function initializeDatabase() {
  table = $("#projectsTable").DataTable({
    columnDefs: [
      { targets: 0, visible: false },
      { targets: 1, width: "50" },
      { targets: 2, width: "200" },
      { targets: 3, width: "400", className: "text-overflow: ellipsis;" },
      { targets: 4, width: "80" },
      {
        className: "text-center",
        targets: -1,
        width: "200",
        defaultContent: [
          `<button class="btn btn-success btn-sm" title="Add members" id="btnAdd">Members</button>
          <button class="btn btn-primary btn-sm" title="Edit project" id="btnEdit">Edit</button>
            <button class="btn btn-danger btn-sm" title="Delete project" id="btnDelete">Delete</button>`,
        ],
      },
    ],
    rowReorder: true,
    autoWidth: false,
    fixedColumns: true,
  });
}

function loadData() {
  var projects;
  $.ajax({
    url: "getProjects",
    type: "GET",
    success: function (resp) {
      projects = resp.projects;
      $.each(projects, function (index, item) {
        sno = index + 1;
        addId = item.p_id;
        table.row
          .add([item.p_id, sno, item.name, item.desc, item.oname])
          .draw(false);
      });
    },
  });
}

// Load Owner
function loadOwner() {
  let users;
  $.ajax({
    url: "getUsers",
    type: "GET",
    success: function (resp) {
      users = resp.users;
      $.each(users, function (index, item) {
        if (item.role == "admin" || item.role == "manager") {
          if (index === 0) {
            eownerList.innerHTML = `<option value="${item.name}" selected>${item.name}</option>`;
            ownerList.innerHTML = `<option value="${item.name}" selected>${item.name}</option>`;
          } else {
            eownerList.innerHTML += `<option value="${item.name}">${item.name}</option>`;
            ownerList.innerHTML += `<option value="${item.name}">${item.name}</option>`;
          }
        }
      });
    },
  });
}

// Add Button
$("#projectsTable").on("click", "#btnAdd", function (e) {
  $("#formAddModal").modal("show");
  row = $(this).parents("tr")[0];
  rowData = table.row(row).data();
  pid = rowData[0];
  coname = rowData[4];
  prname = rowData[2];
  pTitle.innerHTML = "Project: " + rowData[2];
  listMembers.innerHTML = "";
  $.ajax({
    url: "getProjectMembers?pid=" + rowData[0],
    type: "GET",
    success: function (resp) {
      let members = JSON.parse(resp).members;
      $.each(members, function (index, item) {
        if (item.isMember != "" || item.isMember != null)
          jsonObj[item.uid] = item.name;
        listMembers.innerHTML += `<li class="list-group-item">
                                                <div class="custom-control custom-checkbox">
                                                    <input type="checkbox" id="${item.uid}" name="${item.uid}" class="custom-control-input" ${item.isMember} value="${item.uid}">
                                                    <label class="custom-control-label" for="${item.uid}">${item.name}</label>
                                                </div>
                                            </li>`;
      });
    },
  });
});

// Edit Button
$("#projectsTable").on("click", "#btnEdit", function (e) {
  $("#formEditModal").modal("show");
  row = $(this).parents("tr")[0];
  rowData = table.row(row).data();
  uid = rowData[0];
  // Set data
  eusername.value = rowData[2];
  edesc.value = rowData[3];
  eowner.value = rowData[4];
});

// Delete Button
$("#projectsTable").on("click", "#btnDelete", function (e) {
  $("#deleteModal").modal("show");
  row = $(this).parents("tr")[0];
  rowData = table.row(row).data();
  uid = rowData[0];
});

// Info Modal
$("#projectsTable tbody").on("click", "td", function () {
  if ($(this).index() == 4) return;
  const projectDetails = document.getElementById("projectDetails");
  row = $(this).parents("tr")[0];
  $("#formShowModal").modal("show");
  var proData = table.row(row).data();
  var hasMembers = false;
  projectDetails.innerHTML = `<h4>Title: ${proData[2]}</h4>
  <p>Description: ${proData[3]}</p>
  <hr>
  <h5>Members</h5>
  <ul class="list-group" id="displayMembers"></ul>`;
  $.ajax({
    url: "getProjectMembers?pid=" + proData[0],
    type: "GET",
    success: function (resp) {
      var displayMembers = document.getElementById("displayMembers");
      let members = JSON.parse(resp).members;
      console.log(members);
      $.each(members, function (index, member) {
        if (member.isMember == "checked") {
          hasMembers = true;
          displayMembers.innerHTML += `<li class="list-group-item">${member.name}</li>`;
        }
      });
      if (!hasMembers)
        listMembers.innerHTML =
          "<p>No member is available in this project.</p>";
    },
  });
});

$(document).ready(function () {
  // Initialize DataTable
  initializeDatabase();
  // Load DataTable
  loadData();
  loadOwner();

  // Form Handling
  // Create Project
  $("form[name=create-project]").submit(function (e) {
    // Validation
    if (pname.value.length < 3 || desc.value.length < 20) {
      toastsFactory.createToast({
        type: "error",
        icon: "info-circle",
        message:
          "Please enter project name and description with atleast 20 characters",
        duration: 1000,
      });
      return false;
    }

    var $form = $(this);
    var data = $form.serialize();
    $.ajax({
      url: "AddProject",
      type: "POST",
      data: data,
      success: function (resp) {
        [pname, desc].forEach((ele) => ele.style.removeProperty("border"));
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

  // Add Members
  $("form[name=add-members]").submit(function (e) {
    var $form = $(this);
    var data = $form.serialize();
    console.log(data + "&pid=" + pid + "&oname=" + coname);
    $.ajax({
      url: "ModifyMembers",
      type: "POST",
      data: data + "&pid=" + pid + "&oname=" + coname,
      success: function (resp) {
        $("#add-members")[0].reset();
        $("#formAddModal").modal("hide");
        if (resp.inserted == 0 && resp.removed == 0)
          toastsFactory.createToast({
            type: "system",
            icon: "check-circle",
            message: "Kindly add/remove members to submit",
            duration: 1000,
          });
        if (resp.inserted > 0)
          toastsFactory.createToast({
            type: "system",
            icon: "check-circle",
            message: "Added " + resp.inserted + " members to " + prname,
            duration: 1000,
          });
        if (resp.removed > 0)
          toastsFactory.createToast({
            type: "system",
            icon: "check-circle",
            message: "Removed " + resp.removed + " members from " + prname,
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
      },
    });
    e.preventDefault();
  });

  // Update Project
  $("form[name=edit-project]").submit(function (e) {
    var $form = $(this);
    var data = $form.serialize();
    if (
      eusername.value == rowData[2] &&
      edesc.value == rowData[3] &&
      eowner.value == rowData[4]
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
          .data([uid, rowData[1], eusername.value, edesc.value, eowner.value])
          .draw(false);
        $("#formEditModal").modal("hide");
        toastsFactory.createToast({
          type: "system",
          icon: "check-circle",
          message: "Updated successfully",
          duration: 1000,
        });
        $("#edit-project")[0].reset();
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

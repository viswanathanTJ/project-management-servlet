const eusername = document.getElementById("ename");
const edesc = document.getElementById("edesc");
const pTitle = document.getElementById("pTitle");
const listMembers = document.getElementById("editMemberList");
const eowner = document.getElementById("eowner");
const ownerList = document.getElementById("ownerList");
const eownerList = document.getElementById("eownerList");
const deleteBtn = document.getElementById("deleteBtn");
const editModal = new bootstrap.Modal(document.getElementById("editModal"));
const deleteModal = new bootstrap.Modal(document.getElementById("deleteModal"));
const editMembers = document.getElementById("editMembers");
const memberList = document.getElementById("memberList");
const addMemberList = document.getElementById("addMemberList");
const heading = document.getElementById("heading");
const popMembers = document.getElementById("popMembers");

const cards = document.getElementById("cards");

let tname, tdesc, towner;
var inputTextValue, pid;

function myFunction(e) {
  var filter, ul, li, i, txtValue;
  filter = e.value.toUpperCase();
  ul = document.getElementById("memberList");
  li = ul.querySelectorAll("li");
  li = document.getElementsByClassName("member");
  for (i = 0; i < li.length; i++) {
    txtValue = li[i].innerHTML;
    if (txtValue.toUpperCase().indexOf(filter) > -1) li[i].style.display = "";
    else li[i].style.display = "none";
  }
}

function memberFilter(e, list) {
  var filter, ul, li, i, txtValue;
  filter = e.value.toUpperCase();
  ul = document.getElementById(list);
  li = ul.querySelectorAll("li");
  li = document.getElementsByClassName("list-group-item");
  for (i = 0; i < li.length; i++) {
    txtValue = li[i].innerHTML;
    if (txtValue.toUpperCase().indexOf(filter) > -1) li[i].style.display = "";
    else li[i].style.display = "none";
  }
}

var jsonObj = {};
var sno = 0,
  pid,
  coname,
  prname;

// Event Listener
$(document).on("click", "#editBtn", function () {
  editModal.show();
  var pid = $("#editBtn").attr("pid");
  eusername.value = document.getElementById("pname" + pid).innerText.trim();
  edesc.value = document.getElementById("pdesc" + pid).innerHTML;
  eownerList.value = document.getElementById("powner" + pid).innerText;
  loadMembers(pid);
  $(".popover").fadeOut(300);
});
$(document).on("click", "#deleteBtn", function () {
  deleteModal.show();
  $(".popover").fadeOut(300);
});

// VALIDATION
// function validator(element, count) {
//   element.addEventListener("keyup", function () {
//     if (element.value.length > count) {
//       element.style.borderColor = "green";
//     } else {
//       element.style.borderColor = "red";
//     }
//   });
// }

// ["keyup", "focus"].forEach((evt) =>
//   eusername.addEventListener(evt, function () {
//     validator(pname, 3);
//   })
// );

// ["keyup", "focus"].forEach((evt) =>
//   edesc.addEventListener(evt, function () {
//     validator(edesc, 20);
//   })
// );

function loadData() {
  var projects;
  $.ajax({
    url: "Employee/getProjects",
    type: "GET",
    success: function (resp) {
      projects = resp.projects;
      $.each(projects, function (index, item) {
        addCard(item);
      });
    },
  });
}

// Load Owner
function loadOwner() {
  let users;
  $.ajax({
    url: "Get/getUsers",
    type: "GET",
    success: function (resp) {
      users = resp.users;
      $.each(users, function (index, item) {
        if (item.role == "admin" || item.role == "manager") {
          if (index === 0) {
            eownerList.innerHTML = `<option value="${item.name}" selected>${item.name}</option>`;
            // ownerList.innerHTML = `<option value="${item.name}" selected>${item.name}</option>`;
          } else {
            eownerList.innerHTML += `<option value="${item.name}">${item.name}</option>`;
            // ownerList.innerHTML += `<option value="${item.name}">${item.name}</option>`;
          }
        }
      });
    },
  });
}

function loadMembers(pid) {
  listMembers.innerHTML = "";
  $.ajax({
    url: "Get/getProjectMembers?pid=" + pid,
    type: "GET",
    success: function (resp) {
      let members = resp.members;
      $.each(members, function (index, item) {
        if (item.isMember != "" || item.isMember != null)
          jsonObj[item.uid] = item.name;
        listMembers.innerHTML += `
        <label>
          <li class="list-group-item">
              <input class="form-check-input" id="${item.uid}" name="${item.uid}" type="checkbox" ${item.isMember} value="${item.uid}">
              &nbsp;&nbsp;${item.name}
          </li>
        </label>`;
      });
    },
  });
}

function loadAddMemberList() {
  addMemberList.innerHTML = "";
  $.ajax({
    url: "Get/getUsers",
    type: "GET",
    success: function (resp) {
      let users = resp.users;
      $.each(users, function (index, item) {
        if (item.name != cookie_username) {
          addMemberList.innerHTML += `
      <li class="list-group-item">
          <input class="form-check-input" id="${item.uid}" name="${item.uid}" type="checkbox" value="${item.uid}">
          <label for="${item.uid}">&nbsp;&nbsp;${item.name}</label>
      </li>
      `;
        }
      });
    },
  });
}

function deleteProject() {
  alert(pid);
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
    url: "Get/getProjectMembers?pid=" + rowData[0],
    type: "GET",
    success: function (resp) {
      let members = resp.members;
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
    url: "Get/getProjectMembers?pid=" + proData[0],
    type: "GET",
    success: function (resp) {
      var displayMembers = document.getElementById("displayMembers");
      let members = resp.members;
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

loadData();
loadAddMemberList();
// DOCUMENT READY FUNCTIONS
$(document).keyup(function (event) {
  if (event.which === 27) $(".popover").fadeOut(300);
});

var popover = '[data-toggle="popover"]';

$(document).on("click", function (e) {
  $(popover).each(function () {
    if (
      !$(this).is(e.target) &&
      $(this).has(e.target).length === 0 &&
      $(".popover").has(e.target).length === 0
    ) {
      $(this).popover("hide");
    }
  });
});

function initializePopOver() {
  $(popover).hover(function () {
    var content = $(this).attr("data-popover-content");
    pid = $(this).attr("pid");
    $("#editBtn").attr("pid", pid);
    $("#deleteBtn").attr("pid", pid);
    var ctitle = $(this).attr("p-title");
    loadPop(content, pid, ctitle);
  });

  $(popover).click(function (e) {
    console.log("pop clicked");
    $(popover).not(this).popover("hide");
    var content = $(this).attr("data-popover-content");
    pid = $(this).attr("pid");
    $("#editBtn").attr("pid", pid);
    $("#deleteBtn").attr("pid", pid);
    var ctitle = $(this).attr("p-title");
    loadPop(content, pid, ctitle);
  });
  $(popover).popover({
    html: true,
    sanitize: false,
    content: function () {
      var content = $(this).attr("data-popover-content");
      return $(content).children(".popover-body").html();
    },
    title: function () {
      var title = $(this).attr("data-popover-content");
      return $(title).children(".popover-heading").html();
    },
  });
  $(document).on("click", ".popover .close", function () {
    $(this).parents(".popover").popover("hide");
  });
}

window.addEventListener("load", function alertFunc() {
  setTimeout(() => {
    initializePopOver();
    $("#loader").fadeOut("slow");
  }, 1000);
});

$(document).ready(function () {
  // Load DataTable
  loadOwner();

  $("#addProject").on("click", function () {
    $(".popover").fadeOut(300);
    $("#addModal").modal("show");
  });

  // Form Handling
  // Create Project
  $("form[name=add-project]").submit(function (e) {
    // Validation
    // if (pname.value.length < 3 || desc.value.length < 20) {
    //   toastsFactory.createToast({
    //     type: "error",
    //     icon: "info-circle",
    //     message:
    //       "Please enter project name and description with atleast 20 characters",
    //     duration: 1000,
    //   });
    //   return false;
    // }

    var $form = $(this);
    var data = $form.serialize();
    console.log(data);
    $.ajax({
      url: "AddProject",
      type: "POST",
      data: data,
      success: function (resp) {
        // [pname, desc].forEach((ele) => ele.style.removeProperty("border"));
        $("#add-project")[0].reset();
        $("#addModal").modal("hide");
        console.log(resp);
        addCardByProject(resp);
        toastsFactory.createToast({
          type: "system",
          icon: "check-circle",
          message: "Added successfully",
          duration: 1000,
        });
        setTimeout(() => {
          initializePopOver();
        }, 1000);
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
  $("form[name=edit-project]").submit(function (e) {
    var $form = $(this);
    var data = $form.serialize();
    console.log(data + "&pid=" + pid);
    $.ajax({
      url: "ModifyMembers",
      type: "POST",
      data: data + "&pid=" + pid,
      success: function (resp) {
        $("#editModal").modal("hide");
        console.log(resp);
        toastsFactory.createToast({
          type: "system",
          icon: "check-circle",
          message: "Updated successfully",
          duration: 1000,
        });
        document.getElementById("pname" + pid).innerText =
          " " + eusername.value;
        document.getElementById("pdesc" + pid).innerHTML = edesc.value;
        document.getElementById("powner" + pid).innerText = eownerList.value;
        document.getElementById("team" + pid).innerText =
          resp.count + " Employee";
        $("#edit-project")[0].reset();
      },
      error: function (resp) {
        console.log("error");
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
      data: "id=" + pid,
      success: function (data, status, xhr) {
        $("#deleteModal").modal("hide");
        $(`#del${pid}`).remove();
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

$("#cardId").on("click", function () {
  $("#popover__content").addClass("show");
});

// Sidebar toggle
document.addEventListener("DOMContentLoaded", function (event) {
  const showNavbar = (toggleId, navId, bodyId, headerId) => {
    const toggle = document.getElementById(toggleId),
      nav = document.getElementById(navId),
      bodypd = document.getElementById(bodyId),
      headerpd = document.getElementById(headerId);

    if (toggle && nav && bodypd && headerpd) {
      toggle.addEventListener("click", () => {
        nav.classList.toggle("show");
        toggle.classList.toggle("bx-x");
        bodypd.classList.toggle("body-pd");
        headerpd.classList.toggle("body-pd");
      });
    }
  };

  showNavbar("header-toggle", "nav-bar", "body-pd", "header");

  /*===== LINK ACTIVE =====*/
  const linkColor = document.querySelectorAll(".nav_link");

  function colorLink() {
    if (linkColor) {
      linkColor.forEach((l) => l.classList.remove("active"));
      this.classList.add("active");
    }
  }
  linkColor.forEach((l) => l.addEventListener("click", colorLink));

  // Your code to run since DOM is loaded and ready
});

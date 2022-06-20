// Create form variables
const pname = document.getElementById("name");
const desc = document.getElementById("desc");
// Edit form variables
const eusername = document.getElementById("ename");
const edesc = document.getElementById("edesc");

const error = document.getElementById("error");

const pTitle = document.getElementById("pTitle");
const listMembers = document.getElementById("editMemberList");
const eowner = document.getElementById("eowner");
const ownerList = document.getElementById("ownerList");
const eownerList = document.getElementById("eownerList");
const deleteBtn = document.getElementById("deleteBtn");

const editMembers = document.getElementById("editMembers");
const memberList = document.getElementById("memberList");
const addMemberList = document.getElementById("addMemberList");
const heading = document.getElementById("heading");
const popMembers = document.getElementById("popMembers");
const cards = document.getElementById("cards");

// Modal variables
const editModal = new bootstrap.Modal(document.getElementById("editModal"));
const deleteModal = new bootstrap.Modal(document.getElementById("deleteModal"));

var pid;
var url_prefix = "User";

// Get user role
if (crole.startsWith("admin")) url_prefix = "Get";

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
  $(".popover").popover("hide");
});
$(document).on("click", "#deleteBtn", function () {
  deleteModal.show();
  $(".popover").popover("hide");
});

// VALIDATION
function validator(element, count) {
  element.addEventListener("keyup", function () {
    if (element.value.length > count) {
      element.style.borderColor = "green";
      error.innerHTML = "";
    } else {
      element.style.borderColor = "red";
      if (count == 3)
        error.innerHTML = "Project name should be atleast 3 characters long";
      else
        error.innerHTML =
          "Project description should be atleast 10 characters long";
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
    validator(eusername, 3);
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

// Event Listener

loadData();
loadAddMemberList();

// DOCUMENT READY FUNCTIONS
var popover = '[data-toggle="popover"]';

$(document).click(function (e) {
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
var loaded_pid = 0;
function initializePopOver() {
  $("[data-toggle=popover]").hover(function (e) {
    var content = $(this).attr("data-popover-content");
    pid = $(this).attr("pid");
    $("#editBtn").attr("pid", pid);
    $("#deleteBtn").attr("pid", pid);
    var ctitle = $(this).attr("p-title");
    if (loaded_pid != pid) {
      loadPop(content, pid, ctitle);
      loaded_pid = pid;
    }
  });

  $("[data-toggle=popover]").on("click", function (e) {
    $("[data-toggle=popover]").not(this).popover("hide");
    var content = $(this).attr("data-popover-content");
    pid = $(this).attr("pid");
    $("#editBtn").attr("pid", pid);
    $("#deleteBtn").attr("pid", pid);
    var ctitle = $(this).attr("p-title");
    loadPop(content, pid, ctitle);
  });
  $("[data-toggle=popover]").popover({
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

$("#addProject").on("click", function () {
  $(".popover").popover("hide");
  $("#addModal").modal("show");
});

$(document).ready(function () {
  // Load DataTable
  loadOwner();

  $("form[name=add-project]").submit(function (e) {
    addProject();
    e.preventDefault();
  });

  // Add Members
  $("form[name=edit-project]").submit(function (e) {
    editProject();
    e.preventDefault();
  });

  // Delete Project
  $("form[name=delete-project]").submit(function (e) {
    deleteProject();
    e.preventDefault();
  });
});

window.addEventListener("load", function alertFunc() {
  setTimeout(() => {
    initializePopOver();
    $("#loader").fadeOut("slow");
  }, 1000);
});

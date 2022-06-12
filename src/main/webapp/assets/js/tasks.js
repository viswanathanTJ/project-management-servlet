var createBtn = document.getElementById("createBtn");
var rowEditForm = document.getElementById("rowEditForm");
var tableResize = document.getElementById("tableSize");
var table, row, rowData;
var tid = 0;
// Create form variables
const completed = document.getElementById("completed");
const startDate = document.getElementById("startDate");
const endDate = document.getElementById("endDate");
const project = document.getElementById("project");
const priority = document.getElementById("priority");
const description = document.getElementById("description");
const title = document.getElementById("title");
// Edit form variables
const etitle = document.getElementById("etitle");
const ecompleted = document.getElementById("ecompleted");
const eassignee = document.getElementById("eassignee");
const edescription = document.getElementById("edescription");
const epriority = document.getElementById("epriority");
const estartDate = document.getElementById("estartDate");
const edueDate = document.getElementById("dueDate");
const eBtn = document.getElementById("eBtn");

function initializeDatabase() {
  table = $("#tasksTable").DataTable({
    columnDefs: [
      { targets: 0, visible: false },
      { targets: 1, width: "10px", orderable: false, className: "dt-center" },
      { targets: 2, width: "600", className: "text-overflow: ellipsis;" },
    ],
    columns: [
      { width: ".3%" },
      { width: "1px" },
      { width: "600px" },
      { width: "20px" },
      { width: "20px" },
      { width: "20px" },
      { width: "20px" },
    ],
    autoWidth: false,
    fixedColumns: true,
  });
}

function loadData() {
  var tr;
  $.ajax({
    url: "getTasks",
    type: "GET",
    success: function (resp) {
      var tasks = resp.tasks;
      var tmp;
      $.each(tasks, function (index, item) {
        tmp = item.priority;
        if (tmp == 0) tmp = "Low";
        else if (tmp == 1) tmp = "Medium";
        else if (tmp == 2) tmp = "High";
        else if (tmp == 3) tmp = "Urgent";
        var checkStatus = `<input type="checkbox" id="completed${item.t_id}" ${
          item.completed === 1 ? "checked" : ""
        }>`;
        tid = item.t_id;
        tr = table.row
          .add([
            item.t_id,
            checkStatus,
            item.title,
            item.cname,
            item.assignee,
            tmp,
            item.start_date,
          ])
          .draw(false)
          .node();
        if (item.completed === 1) $(tr).addClass("strikeout");
      });
    },
  });
}

eBtn.addEventListener("click", function () {
  console.log("Edit save clicked");
  $.ajax({
    // Update task by id
    url: "updateTask?tid=" + tid,
    type: "POST",
    data: {
      title: etitle.value,
      completed: ecompleted.checked ? 1 : 0,
      assignee: eassignee.value,
      description: edescription.value,
      priority: epriority.selectedIndex,
      start_date: estartDate.value,
      due_date: edueDate.value,
    },
    // success: function (resp) {
    //   if (resp.success) {
    //     alert("Task updated successfully");
    //     location.reload();
    //   } else {
    //     alert("Task update failed");
    //   }
    // },
  });
});

createBtn.addEventListener("click", function () {
  $.ajax({
    url: "getUsers",
    type: "GET",
    success: function (resp) {
      var users = resp.users;
      $.each(users, function (index, item) {
        var option = document.createElement("option");
        option.text = item.name;
        option.value = item.uid;
        assignee.add(option);
      });
    },
  });

  $.ajax({
    url: "getProjects",
    type: "GET",
    success: function (resp) {
      var projects = resp.projects;
      $.each(projects, function (index, item) {
        var option = document.createElement("option");
        option.text = item.name;
        option.value = item.p_id;
        project.add(option);
      });
    },
  });
});

// Info Modal

function resetTableEdit() {
  $("#tableSize").removeClass("col-md-6").addClass("col-md-12");
  rowEditForm.hidden = true;
}

$("#tasksTable tbody").on("click", 'input[type="checkbox"]', function () {
  row = $(this).parents("tr")[0];
  rowData = table.row(row).data();
  if (document.querySelector(`#completed${rowData[0]}`).checked == true) {
    setTaskStatus(rowData[0], 1);
    $(this).closest("tr").addClass("strikeout");
  } else {
    setTaskStatus(rowData[0], 0);
    $(this).closest("tr").removeClass("strikeout");
  }
});

$("#tasksTable tbody").on("click", "td", function () {
  if ($(this).index() == 0) return;
  row = $(this).parents("tr")[0];
  rowData = table.row(row).data();
  $(this)
    .closest('[class^="col-md"]')
    .removeClass("col-md-12")
    .addClass("col-md-6");
  loadTaskById(rowData[0]);
  if (rowEditForm.hidden) rowEditForm.hidden = false;
});

$(document).ready(function () {
  initializeDatabase();
  loadData();

  const swipeHandler = new SwipeHandler();
  const toastsFactory = new ToastsFactory(swipeHandler);

  // Add task
  $("form[name=add-task]").submit(function (e) {
    e.preventDefault();
    $.ajax({
      url: "AddTask",
      type: "POST",
      data: {
        title: title.value,
        assignee: assignee.value,
        description: description.value,
        priority: priority.selectedIndex,
        start_date: startDate.value,
        due_date: dueDate.value,
        project: project.value,
      },
      success: function (resp) {
        console.log(resp);
        tid++;
        $("#add-task")[0].reset();
        $("#formModal").modal("hide");
        table.row
          .add([
            tid,
            `<input type="checkbox" id="completed${tid}">`,
            resp.title,
            resp.cname,
            resp.assignee,
            resp.priority,
            resp.start_date,
          ])
          .draw(false);
        toastsFactory.createToast("Task added successfully", "success");
      },
      error: function (resp) {
        console.log("err: " + resp);
      },
    });
  });
});

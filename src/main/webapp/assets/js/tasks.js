var createBtn = document.getElementById("createBtn");
var rowEditForm = document.getElementById("rowEditForm");
var tableResize = document.getElementById("tableSize");
var table, row, rowData;
var tid = 0;
// Create form variables
const completed = document.getElementById("completed");
const tasksTable = document.getElementById("tasksTable");
const startDate = document.getElementById("startDate");
const endDate = document.getElementById("endDate");
const project = document.getElementById("project");
const assignee = document.getElementById("assignee");
const priority = document.getElementById("priority");
const description = document.getElementById("description");
const title = document.getElementById("title");
const deleteBtn = document.getElementById("deleteBtn");
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
      { targets: 3, width: "250", className: "text-overflow: ellipsis;" },
    ],
    columns: [
      { width: ".3%" },
      { width: "1px" },
      { width: "600px" },
      { width: "250px" },
      { width: "20px" },
      { width: "20px" },
      { width: "20px" },
      { width: "20px" },
    ],
    autoWidth: false,
    fixedColumns: true,
  });
}

function fillRow(item) {
  tmp = item.priority;
  console.log(item);
  if (tmp == 0) tmp = "Low";
  else if (tmp == 1) tmp = "Medium";
  else if (tmp == 2) tmp = "High";
  else if (tmp == 3) tmp = "Urgent";
  rowData = table.row(row).data();
  var checkStatus = `<input type="checkbox" id="completed${rowData[0]}" ${
    item.completed === 1 ? "checked" : ""
  }>`;
  tid = item.t_id;
  tr = table
    .row(row)
    .data([
      rowData[0],
      checkStatus,
      item.title,
      rowData[3],
      item.cname,
      item.assignee,
      tmp,
      item.start_date,
    ])
    .draw(false)
    .node();
  if (item.completed === 1) $(tr).addClass("strikeout");
  if (item.completed === 0) $(tr).removeClass("strikeout");
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
            item.project,
            item.cname,
            item.assignee,
            tmp,
            item.start_date,
          ])
          .draw()
          .node();
        if (item.completed === 1) $(tr).addClass("strikeout");
        else $(tr).removeClass("strikeout");
      });
    },
  });
}

// Update function
deleteBtn.addEventListener("click", function () {
  $("#deleteModal").modal("show");
});

eBtn.addEventListener("click", function () {
  if (estartDate.value > edueDate.value) {
    toastsFactory.createToast({
      type: "system",
      icon: "exclamation-triangle",
      message: "Start date cannot be after end date",
      duration: 1000,
    });
    return;
  }
  $.ajax({
    url: "updateTask",
    type: "POST",
    data: {
      t_id: rowData[0],
      title: etitle.value,
      completed: ecompleted.checked ? 1 : 0,
      assignee: eassignee.value,
      description: edescription.value,
      priority: epriority.selectedIndex,
      start_date: estartDate.value,
      due_date: edueDate.value,
    },
    success: function (resp) {
      fillRow(resp);
      toastsFactory.createToast({
        type: "system",
        icon: "check-circle",
        message: "Task updated successfully",
        duration: 1000,
      });
    },
  });
});

// Assignee select box
$("#project").change(function () {
  console.log(project.value);
  setAssigneeByProject(project.value);
});

createBtn.addEventListener("click", function () {
  $.ajax({
    url: "getProjects",
    type: "GET",
    success: function (resp) {
      var projects = resp.projects;
      $.each(projects, function (index, item) {
        if (index === 0) setAssigneeByProject(item.p_id);
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
  $("#tableSize").removeClass("col-md-7").addClass("col-md-12");
  rowEditForm.hidden = true;
  table.column(3).visible(true);
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
  // tasksTable.removeClass("col-md-12").addClass("col-md-6");
  $("#tableSize").removeClass("col-md-12").addClass("col-md-7");
  loadTaskById(rowData[0]);
  if (rowEditForm.hidden) {
    table.column(3).visible(false);
    rowEditForm.hidden = false;
    rowEditForm.style.opacity = 0;
    setTimeout(() => {
      rowEditForm.style.opacity = 1;
    }, 400);
  }
});

$(document).ready(function () {
  initializeDatabase();
  loadData();

  // Add task
  $("form[name=add-task]").submit(function (e) {
    e.preventDefault();
    if (startDate.value > endDate.value) {
      toastsFactory.createToast({
        type: "system",
        icon: "exclamation-triangle",
        message: "Start date cannot be after end date",
        duration: 1000,
      });
      return;
    }
    $.ajax({
      url: "AddTask",
      type: "POST",
      data: {
        title: title.value,
        assignee: assignee.value,
        description: description.value,
        priority: priority.selectedIndex,
        start_date: startDate.value,
        end_date: endDate.value,
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
            resp.project,
            resp.cname,
            resp.assignee,
            resp.priority,
            resp.start_date,
          ])
          .draw(false);
        toastsFactory.createToast({
          type: "system",
          icon: "check-circle",
          message: "Task added successfully",
          duration: 1000,
        });
      },
      error: function (resp) {
        console.log("err: " + resp);
      },
    });
  });

  // Delete task
  // Delete User
  $("form[name=delete-task]").submit(function (e) {
    $.ajax({
      type: "GET",
      url: "deleteTask?id=" + rowData[0],
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

window.addEventListener("load", function alertFunc() {
  $("#loader").fadeOut("slow");
});

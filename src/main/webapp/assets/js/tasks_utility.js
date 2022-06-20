var createBtn = document.getElementById("createBtn");
var rowEditForm = document.getElementById("rowEditForm");
var tableResize = document.getElementById("tableSize");
var table, row, rowData;
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

var url_prefix = "User";
// Get user role
if (crole.startsWith("admin")) url_prefix = "Get";

// Initialize tasks table
function initializeTasksTable() {
  table = $("#tasksTable").DataTable({
    columnDefs: [
      { targets: 0, visible: false },
      { targets: 1, width: "10px", orderable: false, className: "dt-center" },
      { targets: 2, width: "600", className: "text-overflow: ellipsis;" },
      { targets: 3, width: "250", className: "text-overflow: ellipsis;" },
      { targets: 6, className: "text-center" },
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
    responsive: true,
  });
}

// Filling DOM elements with data
function fillRow(item) {
  tmp = item.priority;
  if (tmp == 0) tmp = `<span class="priority low">low</span>`;
  else if (tmp == 1) tmp = `<span class="priority medium">medium</span>`;
  else if (tmp == 2) tmp = `<span class="priority high">high</span>`;
  else if (tmp == 3) tmp = `<span class="priority urgent">urgent</span>`;
  rowData = table.row(row).data();
  var checkStatus = `<input type="checkbox" id="completed${rowData[0]}" ${
    item.completed == 1 ? "checked" : ""
  }>`;
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
  if (item.completed == 1) $(tr).addClass("strikeout");
  if (item.completed == 0) $(tr).removeClass("strikeout");
}

function resetTableEdit() {
  $("#tableSize").removeClass("col-md-7").addClass("col-md-12");
  rowEditForm.hidden = true;
  table.column(3).visible(true);
}

// Event listeners

// Update function
// Delete Button
if (deleteBtn instanceof Element) {
  deleteBtn.addEventListener("click", function () {
    $("#deleteModal").modal("show");
  });
}

// Edit Button
eBtn.addEventListener("click", function () {
  editTask();
});
// Create Button
createBtn.addEventListener("click", function () {
  createFormLoader();
});

$(document).ready(function () {
  initializeTasksTable();
  loadData(`${url_prefix}/getTasks`);

  // Add task
  $("form[name=add-task]").submit(function (e) {
    e.preventDefault();
    addTask();
  });

  // Delete task
  $("form[name=delete-task]").submit(function (e) {
    e.preventDefault();
    deleteTask();
  });
});

// Assignee select box
$("#project").change(function () {
  console.log(project.value);
  setAssigneeByProject(project.value);
});

// Document keyup functions
$(document).keyup(function (event) {
  if (event.which === 27) resetTableEdit();
});

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

window.addEventListener("load", function alertFunc() {
  $("#loader").fadeOut("slow");
});

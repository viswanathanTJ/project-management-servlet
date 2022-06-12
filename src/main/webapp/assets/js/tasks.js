var createBtn = document.getElementById("createBtn");
var assignee = document.getElementById("assignee");
var startDate = document.getElementById("startDate");
var endDate = document.getElementById("endDate");
var project = document.getElementById("project");
var rowEditForm = document.getElementById("rowEditForm");
var tableResize = document.getElementById("tableSize");
var table;

function initializeDatabase() {
  table = $("#tasksTable").DataTable({
    columnDefs: [
      { targets: 0, visible: false },
      { targets: 1, width: "10px" },
      { targets: 2, width: "500", className: "text-overflow: ellipsis;" },
      { targets: 3, width: "50" },
      { targets: 4, width: "150" },
      { targets: 4, width: "150" },
    ],
    columns: [
      { width: ".3%" },
      { width: "1px" },
      { width: "600px" },
      { width: "20px" },
      { width: "20px" },
      { width: "20px" },
    ],
    autoWidth: false,
    fixedColumns: true,
  });
}

function loadData() {
  var tasks = [
    {
      t_id: 1,
      end_date: "2022-06-11",
      createdat: "2022-06-10",
      cname: "admin",
      c_id: 1,
      title: "Frontend",
      priority: "High",
      p_id: "1",
      desc: "Use HTML, CSS, JS, Ajax, JQuery",
      start_date: "2022-06-10",
    },
    {
      t_id: 2,
      end_date: "2022-06-13",
      createdat: "2022-06-10",
      cname: "admin",
      c_id: 1,
      title: "Backend",
      priority: "urgent",
      p_id: "1",
      desc: "Java",
      start_date: "2022-06-10",
    },
  ];
  $.each(tasks, function (index, item) {
    table.row
      .add([
        item.t_id,
        '<input type="checkbox">',
        item.title,
        item.priority,
        item.cname,
        item.start_date,
      ])
      .draw(false);
  });
  // $.ajax({
  //   url: "getTasks",
  //   type: "GET",
  //   success: function (resp) {
  //     var tasks = resp.tasks;
  //     console.log(tasks);
  //     $.each(tasks, function (index, item) {
  //       table.row
  //         .add([
  //           item.t_id,
  //           item.title,
  //           item.priority,
  //           item.cname,
  //           item.start_date,
  //         ])
  //         .draw(false);
  //     });
  //   },
  // });
}

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

$("#tasksTable tbody").on("click", "td", function () {
  if ($(this).index() == 0) return;
  const taskDetails = document.getElementById("taskDetails");
  row = $(this).parents("tr")[0];
  $(this)
    .closest('[class^="col-md"]')
    .removeClass("col-md-12")
    .addClass("col-md-6");

  if (rowEditForm.hidden) rowEditForm.hidden = false;
  else {
    $(this)
      .closest('[class^="col-md"]')
      .removeClass("col-md-6")
      .addClass("col-md-12");
    rowEditForm.hidden = true;
  }
  // Find siblings of parent with similar class criteria
  // - if all siblings are the same, you can use ".siblings()"
  // Change class to "col-md-2"
  // .siblings().hidden=false;
  // .removeClass("col-md-3")
  // .addClass("col-md-2");
});

$(document).ready(function () {
  initializeDatabase();
  loadData();

  const swipeHandler = new SwipeHandler();
  const toastsFactory = new ToastsFactory(swipeHandler);

  // Add task
  $("form[name=add-task]").submit(function (e) {
    e.preventDefault();
    var data = $(this).serialize();
    console.log(
      data + "&startDate=" + startDate.value + "&endDate=" + endDate.value
    );
    $.ajax({
      url: "AddTask",
      type: "POST",
      data:
        data + "&startDate=" + startDate.value + "&endDate=" + endDate.value,
      success: function (resp) {
        console.log(resp);
      },
      error: function (resp) {
        console.log("err: " + resp);
      },
    });
  });
});

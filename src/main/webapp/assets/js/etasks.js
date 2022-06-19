const username = getCookie("username");
var table, row, rowData;
// Create form variables
const completed = document.getElementById("completed");
const tasksTable = document.getElementById("tasksTable");
const startDate = document.getElementById("startDate");
const endDate = document.getElementById("endDate");
const priority = document.getElementById("priority");
const description = document.getElementById("description");
const title = document.getElementById("title");
const project = document.getElementById("project");
const assignee = document.getElementById("assignee");
const createBtn = document.getElementById("createBtn");
const edueDate = document.getElementById("dueDate");

var option = document.createElement("option");
option.text = username;
$.ajax({ url: "User/getUserID", type: "GET" }).done(function (data) {
  option.value = data;
});
assignee.add(option);

initializeDatabase();
loadData("User/getTasks");

window.addEventListener("load", function alertFunc() {
  $("#loader").fadeOut("slow");
});

createBtn.addEventListener("click", function () {
  $.ajax({
    url: "User/getProjects",
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

$(document).ready(function () {
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
        var tmp = resp.priority;
        if (tmp == 0) tmp = `<span class="priority low">low</span>`;
        else if (tmp == 1) tmp = `<span class="priority medium">medium</span>`;
        else if (tmp == 2) tmp = `<span class="priority high">high</span>`;
        else if (tmp == 3) tmp = `<span class="priority urgent">urgent</span>`;
        $("#add-task")[0].reset();
        $("#formModal").modal("hide");
        table.row
          .add([
            resp.t_id,
            `<input type="checkbox" id="completed${resp.t_id}">`,
            resp.title,
            resp.project,
            resp.cname,
            resp.assignee,
            tmp,
            resp.start_date,
          ])
          .draw(false);
        toastsFactory.createToast({
          type: "system",
          icon: "check-circle",
          message: "Task added successfully",
          duration: 1000,
        });
        resetTableEdit();
      },
      error: function (resp) {
        console.log("err: " + resp);
      },
    });
  });
});

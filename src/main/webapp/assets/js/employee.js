const projects = document.getElementById("projects");
const tasks = document.getElementById("tasks");
const opentasks = document.getElementById("opentasks");
const rtasks = document.getElementById("recentTasks");

$.ajax({
  url: "Employee/getRecentInfo",
  type: "GET",
  success: function (data) {
    console.log(data);
    projects.innerHTML = data.projects;
    tasks.innerHTML = data.tasks;
    opentasks.innerHTML = data.open;
  },
});

$.ajax({
  url: "Employee/getRecentTasks",
  type: "GET",
  success: function (data) {
    var tasks = data.tasks;
    console.log(tasks);
    // rtasks.innerHTML = "";
    $.each(tasks, function (index, task) {
      rtasks.innerHTML += `
             <tr>
                <td>${task.title}</td>
                <td>${task.project}</td>
                <td>${task.owner}</td>
                <td>${task.assignee}</td>
                <td style="text-align:center"><span class="priority ${task.priority}">${task.priority}</span></td>
                <td>${task.createdat}</td>
            </tr>
            `;
    });
  },
});

window.addEventListener("load", function alertFunc() {
  $("#loader").fadeOut("slow");
});

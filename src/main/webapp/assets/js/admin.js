const projects = document.getElementById("projects");
const users = document.getElementById("users");
const tasks = document.getElementById("tasks");
const opentasks = document.getElementById("opentasks");
const rtasks = document.getElementById("recentTasks");
const rusers = document.getElementById("recentUsers");

$.ajax({
  url: "getAllCounts",
  type: "GET",
  success: function (data) {
    projects.innerHTML = data.projects;
    users.innerHTML = data.users;
    tasks.innerHTML = data.tasks;
    opentasks.innerHTML = data.open;
  },
});
$.ajax({
  url: "getRecentUsers",
  type: "GET",
  success: function (data) {
    var users = data.users;
    console.log(users);
    rusers.innerHTML = "";
    $.each(users, function (index, user) {
      rusers.innerHTML += `
                <tr>
                    <td width="60px"><div class="imgBx">${user.name
                      .charAt(0)
                      .toUpperCase()}</div></td>
                    <td><h4>${user.name} <br> <span>${
        user.role
      }</span></h4></td>
                </tr>
        `;
    });
  },
});

$.ajax({
    url: "getRecentTasks",
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
                <td><span class="priority ${task.priority}">${task.priority}</span></td>
            </tr>
            `;
        });
    }
})

window.addEventListener("load", function alertFunc() {
  $("#loader").fadeOut("slow");
});

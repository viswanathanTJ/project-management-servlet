const projects = document.getElementById("projects");
const users = document.getElementById("users");
const tasks = document.getElementById("tasks");
const opentasks = document.getElementById("opentasks");
const rtasks = document.getElementById("recentTasks");
const rusers = document.getElementById("recentUsers");

function getAllCounts() {
  $.ajax({
    url: "Admin/getAllCounts",
    type: "GET",
    success: function (data) {
      getRecentUsers();
      projects.innerHTML = data.projects;
      users.innerHTML = data.users;
      tasks.innerHTML = data.tasks;
      opentasks.innerHTML = data.open;
    },
  });
}
function getRecentUsers() {
  $.ajax({
    url: "Admin/getRecentUsers",
    type: "GET",
    success: function (data) {
      getRecentTasks();
      var users = data.users;
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
}

function getRecentTasks() {
  $.ajax({
    url: "Admin/getRecentTasks",
    type: "GET",
    success: function (data) {
      var tasks = data.tasks;
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
    },
  });
}

$(document).ready(function () {
  getAllCounts();
});

window.addEventListener("load", function alertFunc() {
  $("#loader").fadeOut("slow");
});

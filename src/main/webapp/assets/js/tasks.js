var createBtn = document.getElementById("createBtn");
var assignee = document.getElementById("assignee");
var startDate = document.getElementById("startDate");
var endDate = document.getElementById("endDate");
var project = document.getElementById("project");

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

$(document).ready(function () {
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

createBtn.addEventListener("click", function () {
  $.ajax({
    url: "User/getProjects",
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


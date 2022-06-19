function loadData() {
  var projects;
  $.ajax({
    url: "User/getProjects",
    type: "GET",
    success: function (resp) {
      projects = resp.projects;
      $.each(projects, function (index, item) {
        addCard(item);
      });
    },
  });
}

loadData();

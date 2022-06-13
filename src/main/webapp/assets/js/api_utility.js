function setAssignedUsers(pid, assignee) {
  $.ajax({
    url: "getUsersInProject?pid=" + pid,
    type: "GET",
    success: function (resp) {
      var users = resp.users;
      $("#eassignee").empty();
      $.each(users, function (index, item) {
        var option = document.createElement("option");
        if (assignee === item.name) option.selected = true;
        option.text = item.name;
        option.value = item.uid;
        eassignee.add(option);
      });
    },
  });
}

function setAssigneeByProject(project) {
  console.log(project);
  $.ajax({
    url: "getUsersInProject",
    type: "GET",
    data: {
      pid: project,
    },
    success: function (resp) {
      console.log(resp);
      $("#assignee").empty();
      var assignees = resp.users;
      $.each(assignees, function (index, item) {
        var option = document.createElement("option");
        option.text = item.name;
        option.value = item.uid;
        assignee.add(option);
      });
    },
  });
}

function loadTaskById(tid) {
  $.ajax({
    url: "getTaskByID?tid=" + tid,
    type: "GET",
    success: function (resp) {
      console.log(resp);
      etitle.value = resp.title;
      if (resp.completed == 0) ecompleted.checked = false;
      else ecompleted.checked = true;
      setAssignedUsers(resp.p_id, resp.assignee);
      epriority.selectedIndex = resp.priority;
      estartDate.value = resp.start_date;
      edescription.value = resp.desc;
      edueDate.value = resp.due_date;
      eassignee.value = resp.assignee;
    },
  });
}

function setTaskStatus(tid, status) {
  $.ajax({
    url: "setTaskStatus?tid=" + tid + "&status=" + status,
    type: "GET",
  });
}

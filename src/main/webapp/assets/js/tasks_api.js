// Fill DOM based on API response
function createFormLoader() {
    $.ajax({
      url: `${url_prefix}/getProjects`,
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
}
function setAssignedUsers(pid, assignee) {
  $.ajax({
    url: "Get/getUsersInProject?pid=" + pid,
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
  $.ajax({
    url: "Get/getUsersInProject",
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
        if (item.name == cookie_username) option.selected = true;
        assignee.add(option);
      });
    },
  });
}

function loadData(url) {
  var tr;
  $.ajax({
    url: url,
    type: "GET",
    success: function (resp) {
      var tasks = resp.tasks;
      var tmp;
      $.each(tasks, function (index, item) {
        tmp = item.priority;
        if (tmp == 0) tmp = `<span class="priority low">low</span>`;
        else if (tmp == 1) tmp = `<span class="priority medium">medium</span>`;
        else if (tmp == 2) tmp = `<span class="priority high">high</span>`;
        else if (tmp == 3) tmp = `<span class="priority urgent">urgent</span>`;
        var checkStatus = `<input type="checkbox" id="completed${item.t_id}" ${
          item.completed == 1 ? "checked" : ""
        }>`;
        tr = table.row
          .add([
            item.t_id,
            checkStatus,
            item.title,
            item.project,
            item.cname,
            item.assignee,
            tmp,
            item.start_date,
          ])
          .draw()
          .node();
        if (item.completed == 1) $(tr).addClass("strikeout");
        else $(tr).removeClass("strikeout");
      });
    },
  });
}

function loadTaskById(tid) {
  $.ajax({
    url: "Get/getTaskByID?tid=" + tid,
    type: "GET",
    success: function (resp) {
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

// Form actions
function setTaskStatus(tid, status) {
  $.ajax({
    url: "setTaskStatus?tid=" + tid + "&status=" + status,
    type: "GET",
  });
}

function addTask() {
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
    url: "Add/addTask",
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
    },
    error: function (resp) {
      console.log("err: " + resp);
    },
  });
}

function editTask() {
  if (estartDate.value > edueDate.value) {
    toastsFactory.createToast({
      type: "system",
      icon: "exclamation-triangle",
      message: "Start date cannot be after end date",
      duration: 1000,
    });
    return;
  }
  $.ajax({
    url: "updateTask",
    type: "POST",
    data: {
      t_id: rowData[0],
      title: etitle.value,
      completed: ecompleted.checked ? 1 : 0,
      assignee: eassignee.value,
      description: edescription.value,
      priority: epriority.selectedIndex,
      start_date: estartDate.value,
      due_date: edueDate.value,
    },
    success: function (resp) {
      fillRow(resp);
      toastsFactory.createToast({
        type: "system",
        icon: "check-circle",
        message: "Task updated successfully",
        duration: 1000,
      });
    },
  });
}

function deleteTask() {
  $.ajax({
    type: "GET",
    url: "deleteTask?id=" + rowData[0],
    success: function (data, status, xhr) {
      table.row(row).remove().draw();
      $("#deleteModal").modal("hide");
      resetTableEdit();
      toastsFactory.createToast({
        type: "system",
        icon: "check-circle",
        message: "Deleted successfully",
        duration: 1000,
      });
    },
    error: function (resp, status, error) {
      toastsFactory.createToast({
        type: "error",
        icon: "info-circle",
        message: resp.responseText,
        duration: 1000,
      });
    },
  });
}

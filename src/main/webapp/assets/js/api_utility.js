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
        if (item.name != cookie_username) {
          var option = document.createElement("option");
          option.text = item.name;
          option.value = item.uid;
          assignee.add(option);
        }
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

function setTaskStatus(tid, status) {
  $.ajax({
    url: "setTaskStatus?tid=" + tid + "&status=" + status,
    type: "GET",
  });
}

function fillRow(item) {
  tmp = item.priority;
  if (tmp == 0) tmp = `<span class="priority low">low</span>`;
  else if (tmp == 1) tmp = `<span class="priority medium">medium</span>`;
  else if (tmp == 2) tmp = `<span class="priority high">high</span>`;
  else if (tmp == 3) tmp = `<span class="priority urgent">urgent</span>`;
  rowData = table.row(row).data();
  var checkStatus = `<input type="checkbox" id="completed${rowData[0]}" ${
    item.completed === 1 ? "checked" : ""
  }>`;
  tid = item.t_id;
  tr = table
    .row(row)
    .data([
      rowData[0],
      checkStatus,
      item.title,
      rowData[3],
      item.cname,
      item.assignee,
      tmp,
      item.start_date,
    ])
    .draw(false)
    .node();
  if (item.completed === 1) $(tr).addClass("strikeout");
  if (item.completed === 0) $(tr).removeClass("strikeout");
}

function initializeDatabase() {
  table = $("#tasksTable").DataTable({
    columnDefs: [
      { targets: 0, visible: false },
      { targets: 1, width: "10px", orderable: false, className: "dt-center" },
      { targets: 2, width: "600", className: "text-overflow: ellipsis;" },
      { targets: 3, width: "250", className: "text-overflow: ellipsis;" },
      { targets: 6, className: "text-center" },
    ],
    columns: [
      { width: ".3%" },
      { width: "1px" },
      { width: "600px" },
      { width: "250px" },
      { width: "20px" },
      { width: "20px" },
      { width: "20px" },
      { width: "20px" },
    ],
    autoWidth: false,
    fixedColumns: true,
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
          item.completed === 1 ? "checked" : ""
        }>`;
        tid = item.t_id;
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
        if (item.completed === 1) $(tr).addClass("strikeout");
        else $(tr).removeClass("strikeout");
      });
    },
  });
}

function resetTableEdit() {
  $("#tableSize").removeClass("col-md-7").addClass("col-md-12");
  rowEditForm.hidden = true;
  table.column(3).visible(true);
}
$(document).keyup(function (event) {
  if (event.which === 27) resetTableEdit();
});

$("#tasksTable tbody").on("click", 'input[type="checkbox"]', function () {
  row = $(this).parents("tr")[0];
  rowData = table.row(row).data();
  if (document.querySelector(`#completed${rowData[0]}`).checked == true) {
    setTaskStatus(rowData[0], 1);
    $(this).closest("tr").addClass("strikeout");
  } else {
    setTaskStatus(rowData[0], 0);
    $(this).closest("tr").removeClass("strikeout");
  }
});

$("#tasksTable tbody").on("click", "td", function () {
  if ($(this).index() == 0) return;
  row = $(this).parents("tr")[0];
  rowData = table.row(row).data();
  // tasksTable.removeClass("col-md-12").addClass("col-md-6");
  $("#tableSize").removeClass("col-md-12").addClass("col-md-7");
  loadTaskById(rowData[0]);
  if (rowEditForm.hidden) {
    table.column(3).visible(false);
    rowEditForm.hidden = false;
    rowEditForm.style.opacity = 0;
    setTimeout(() => {
      rowEditForm.style.opacity = 1;
    }, 400);
  }
});

// Load DOM API's
function loadData() {
  var projects;
  $.ajax({
    url: `${url_prefix}/getProjects`,
    type: "GET",
    success: function (resp) {
      projects = resp.projects;
      $.each(projects, function (index, item) {
        addCard(item);
      });
    },
  });
}

function loadOwner() {
  let users;
  $.ajax({
    url: "Get/getUsers",
    type: "GET",
    success: function (resp) {
      users = resp.users;
      $.each(users, function (index, item) {
        if (item.role == "admin" || item.role == "manager") {
          if (index === 0) {
            eownerList.innerHTML = `<option value="${item.name}" selected>${item.name}</option>`;
          } else {
            eownerList.innerHTML += `<option value="${item.name}">${item.name}</option>`;
          }
        }
      });
    },
  });
}

function loadMembers(pid) {
  listMembers.innerHTML = "";
  $.ajax({
    url: "Get/getProjectMembers?pid=" + pid,
    type: "GET",
    success: function (resp) {
      let members = resp.members;
      $.each(members, function (index, item) {
        if (item.isMember != "" || item.isMember != null)
          jsonObj[item.uid] = item.name;
        listMembers.innerHTML += `
        <label>
          <li class="list-group-item">
              <input class="form-check-input" id="${item.uid}" name="${item.uid}" type="checkbox" ${item.isMember} value="${item.uid}">
              &nbsp;&nbsp;${item.name}
          </li>
        </label>`;
      });
    },
  });
}

function loadAddMemberList() {
  addMemberList.innerHTML = "";
  $.ajax({
    url: "Get/getUsers",
    type: "GET",
    success: function (resp) {
      let users = resp.users;
      $.each(users, function (index, item) {
        addMemberList.innerHTML += `
        <label>
        <li class="list-group-item">
          <input class="form-check-input" id="${item.uid}" name="${item.uid}" type="checkbox" value="${item.uid}">
          &nbsp;&nbsp;${item.name}
        </li>
        </label>
      `;
      });
    },
  });
}


// Form actions
function addProject() {
  // Validation
  if (pname.value.length < 3 || desc.value.length < 20) {
    toastsFactory.createToast({
      type: "error",
      icon: "info-circle",
      message: "Please fill all the fields without error",
      duration: 1000,
    });
    return false;
  }

  var $form = $(this);
  var data = $form.serialize();
  $.ajax({
    url: "Add/addProject",
    type: "POST",
    data: data,
    success: function (resp) {
      [pname, desc].forEach((ele) => ele.style.removeProperty("border"));
      $("#add-project")[0].reset();
      $("#addModal").modal("hide");
      console.log(resp);
      addCardByProject(resp);
      toastsFactory.createToast({
        type: "system",
        icon: "check-circle",
        message: "Added successfully",
        duration: 1000,
      });
      setTimeout(() => {
        initializePopOver();
      }, 1000);
    },
    error: function (resp) {
      toastsFactory.createToast({
        type: "error",
        icon: "info-circle",
        message: resp.responseText,
        duration: 1000,
      });
      $("#formModal").modal("show");
    },
  });
}

function editProject() {
  if (pname.value.length < 3 || desc.value.length < 20) {
    toastsFactory.createToast({
      type: "error",
      icon: "info-circle",
      message: "Please fill all the fields without error",
      duration: 1000,
    });
    return false;
  }
  var $form = $(this);
  var data = $form.serialize();
  console.log(data + "&pid=" + pid);
  $.ajax({
    url: "ModifyMembers",
    type: "POST",
    data: data + "&pid=" + pid,
    success: function (resp) {
      $("#editModal").modal("hide");
      [ename, edesc].forEach((ele) => ele.style.removeProperty("border"));
      toastsFactory.createToast({
        type: "system",
        icon: "check-circle",
        message: "Updated successfully",
        duration: 1000,
      });
      document.getElementById("pname" + pid).innerText = " " + eusername.value;
      document.getElementById("pdesc" + pid).innerHTML = edesc.value;
      document.getElementById("powner" + pid).innerText = eownerList.value;
      document.getElementById("team" + pid).innerText =
        resp.count + " Employee";
      $("#edit-project")[0].reset();
    },
    error: function (resp) {
      console.log("error");
      toastsFactory.createToast({
        type: "error",
        icon: "info-circle",
        message: resp.responseText,
        duration: 1000,
      });
    },
  });
  e.preventDefault();
}

function deleteProject() {
  $.ajax({
    type: "POST",
    url: "deleteProject",
    data: "id=" + pid,
    success: function (data, status, xhr) {
      $("#deleteModal").modal("hide");
      $(`#del${pid}`).remove();
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

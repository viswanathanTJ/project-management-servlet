const username = document.getElementById("name");
const desc = document.getElementById("desc");
const eusername = document.getElementById("ename");
const edesc = document.getElementById("edesc");
const eowner = document.getElementById("eowner");

var sno, addId;
// VALIDATION

function validator(element, count) {
    element.addEventListener("keyup", function() {
        if (element.value.length > count) {
            element.style.borderColor = "green";
        } else {
            element.style.borderColor = "red";
        }
    });
}

["keyup", "focus"].forEach((evt) =>
    username.addEventListener(evt, function() {
        validator(username, 10);
    })
);
["keyup", "focus"].forEach((evt) =>
    eusername.addEventListener(evt, function() {
        validator(username, 10);
    })
);
["keyup", "focus"].forEach((evt) =>
    desc.addEventListener(evt, function() {
        validator(desc, 20);
    })
);
["keyup", "focus"].forEach((evt) =>
    edesc.addEventListener(evt, function() {
        validator(edesc, 20);
    })
);

["keyup", "focus"].forEach((evt) =>
    username.addEventListener(evt, function() {
        username.addEventListener("keyup", function() {
            if (username.value.length > 10) {
                username.style.borderColor = "green";
            } else {
                username.style.borderColor = "red";
            }
        });
    })
);

["keyup", "focus"].forEach((evt) =>
    desc.addEventListener(evt, function() {
        desc.addEventListener("keyup", function() {
            if (desc.value.length > 20) {
                desc.style.borderColor = "green";
            } else {
                desc.style.borderColor = "red";
            }
        });
    })
);

function initializeDatabase() {
    table = $("#projectsTable").DataTable({
        columnDefs: [
            { targets: 0, visible: false },
            { targets: 1, width: "50" },
            { targets: 2, width: "200" },
            { targets: 3, width: "400" },
            { targets: 4, width: "80" },
            {
                className: "text-center",
                targets: -1,
                width: "90",
                defaultContent: [
                    `<a href="#" id="btnAdd"><span><i class="fas fa-plus"></i></span></a>
          <a href="#" id="btnEdit"><span><i class="fas fa-edit"></i></span></a>
            <a href="#" id="btnDelete"><span><i class="fas fa-trash"></i></span></a>`,
                ],
            },
        ],
        autoWidth: false,
        fixedColumns: true,
    });
}

function loadData() {
    let projects;
    let xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:8080/Project_Management/getProjects", true);
    xhr.onload = function() {
        if (this.status === 200) {
            projects = JSON.parse(this.responseText).projects;
            $.each(projects, function(index, item) {
                sno = index + 1;
                addId = item.p_id;
                table.row
                    .add([item.p_id, sno, item.name, item.desc, item.oname])
                    .draw(false);
            });
        }
    };
    xhr.send();
}

// Edit Button
$("#projectsTable").on("click", "#btnAdd", function(e) {
    // alert('add')
    $("#formAddModal").modal("show");
    // $("#formEditModal").modal("show");
    // row = $(this).parents("tr")[0];
    // rowData = table.row(row).data();
    // uid = rowData[0];
    // // Set data
    // eusername.value = rowData[2];
    // edesc.value = rowData[3];
    // eowner.value = rowData[4];
});

// Edit Button
$("#projectsTable").on("click", "#btnEdit", function(e) {
    $("#formEditModal").modal("show");
    row = $(this).parents("tr")[0];
    rowData = table.row(row).data();
    uid = rowData[0];
    // Set data
    eusername.value = rowData[2];
    edesc.value = rowData[3];
    eowner.value = rowData[4];
});

// Delete Button
$("#projectsTable").on("click", "#btnDelete", function(e) {
    $("#deleteModal").modal("show");
    row = $(this).parents("tr")[0];
    rowData = table.row(row).data();
    uid = rowData[0];
});

// Info Modal
// $("#projectsTable tbody").on("click", "tr", function () {
$("#projectsTable tbody").on("click", "td", function() {
    if ($(this).index() == 4) return;
    const projectDetails = document.getElementById("projectDetails");
    row = $(this).parents("tr")[0];
    $("#formShowModal").modal("show");
    var proData = table.row(row).data();
    console.log(proData);
    projectDetails.innerHTML = `<h4>Title: ${proData[2]}</h4>
  <p>Description: ${proData[3]}</p>
  <hr>
  <h5>Members</h5>
  <ul>
    <li>Test</li>
    <li>Another</li>
  </ul>
  `;
});

$(document).ready(function() {
    // Initialize DataTable
    initializeDatabase();
    const swipeHandler = new SwipeHandler();
    const toastsFactory = new ToastsFactory(swipeHandler);
    // Load DataTable
    loadData();

    // Form Handling
    $("form[name=create-project]").submit(function(e) {
        // Validation
        if (username.value.length < 10 || desc.value.length < 20) {
            toastsFactory.createToast({
                type: "error",
                icon: "info-circle",
                message: "Please enter project name and description with atleast 20 characters",
                duration: 1000,
            });
            return false;
        }

        // Ajax Call
        var $form = $(this);
        var data = $form.serialize();
        $.ajax({
            url: "AddProject",
            type: "POST",
            data: data,
            success: function(resp) {
                $("#create-project")[0].reset();
                $("#formModal").modal("hide");
                var item = JSON.parse(resp);
                sno = sno + 1;
                addId += 1;
                table.row
                    .add([addId, sno, item.name, item.desc, item.oname])
                    .draw(false);
                toastsFactory.createToast({
                    type: "system",
                    icon: "check-circle",
                    message: "Added successfully",
                    duration: 1000,
                });
            },
            error: function(resp) {
                toastsFactory.createToast({
                    type: "error",
                    icon: "info-circle",
                    message: resp.responseText,
                    duration: 1000,
                });
                $("#formModal").modal("show");
            },
        });

        e.preventDefault();
    });

    // Update Project
    $("form[name=edit-project]").submit(function(e) {
        var $form = $(this);
        var data = $form.serialize();
        if (
            eusername.value == rowData[2] &&
            edesc.value == rowData[3] &&
            eowner.value == rowData[4]
        ) {
            toastsFactory.createToast({
                type: "system",
                icon: "info-circle",
                message: "Kindly make some changes",
                duration: 1000,
            });
            return false;
        }
        $.ajax({
            type: "POST",
            url: "updateProject",
            data: data + "&id=" + uid,
            success: function(data, status, xhr) {
                table
                    .row(row)
                    .data([uid, rowData[1], eusername.value, edesc.value, eowner.value])
                    .draw(false);
                $("#formEditModal").modal("hide");
                toastsFactory.createToast({
                    type: "system",
                    icon: "check-circle",
                    message: "Updated successfully",
                    duration: 1000,
                });
                $("#edit-project")[0].reset();
            },
            error: function(resp, status, error) {
                toastsFactory.createToast({
                    type: "error",
                    icon: "info-circle",
                    message: resp.responseText,
                    duration: 1000,
                });
            },
        });
        e.preventDefault();
    });

    // Delete Project
    $("form[name=delete-project]").submit(function(e) {
        $.ajax({
            type: "POST",
            url: "deleteProject",
            data: "id=" + uid,
            success: function(data, status, xhr) {
                table.row(row).remove().draw();
                $("#deleteModal").modal("hide");
                toastsFactory.createToast({
                    type: "system",
                    icon: "check-circle",
                    message: "Deleted successfully",
                    duration: 1000,
                });
            },
            error: function(resp, status, error) {
                toastsFactory.createToast({
                    type: "error",
                    icon: "info-circle",
                    message: resp.responseText,
                    duration: 1000,
                });
            },
        });
        e.preventDefault();
    });
});
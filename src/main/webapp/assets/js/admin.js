var table;
var eusername = document.getElementById("ename");
// var epassword = document.getElementById("epassword");
var eemail = document.getElementById("eemail");
var erole = document.getElementById("erole");
var uid, rowData, row;
var addId, sno;

function initializeDatabase() {
    table = $("#usersTable").DataTable({
        columnDefs: [
            { targets: 0, visible: false },
            { targets: 1, width: "50" },
            { targets: 2, width: "150" },
            { targets: 3, width: "200" },
            { targets: 4, width: "100" },
            { targets: 5, width: "120" },
            {
                className: "text-center",
                targets: -1,
                width: "50",
                defaultContent: [
                    `<a href="#" id="btnEdit"><span><i class="fas fa-edit"></i></span></a>
            <a href="#" id="btnDelete"><span><i class="fas fa-trash"></i></span></a>`,
                ],
            },
        ],
        autoWidth: false,
        fixedColumns: true,
    });
}

function loadData() {
    let users;
    $.ajax({
        url: "getUsers",
        type: "GET",
        success: function(resp) {
            users = JSON.parse(resp.responseText).users;
            $.each(users, function(index, item) {
                sno = index + 1;
                addId = item.uid;
                table.row
                    .add([
                        item.uid,
                        sno,
                        item.name,
                        item.email,
                        item.role,
                        item.createdat,
                    ])
                    .draw(false);
            });
        },
    });
}

// Edit Button
$("#usersTable").on("click", "#btnEdit", function() {
    $("#formEditModal").modal("show");
    row = $(this).parents("tr")[0];
    rowData = table.row(row).data();
    uid = rowData[0];
    // Set data
    eusername.setAttribute("value", rowData[2]);
    eemail.setAttribute("value", rowData[3]);
    erole.value = rowData[4];
});

// Delete Button
$("#usersTable").on("click", "#btnDelete", function() {
    $("#deleteModal").modal("show");
    row = $(this).parents("tr")[0];
    rowData = table.row(row).data();
    uid = rowData[0];
});

$(document).ready(function() {
    // Initialize DataTable
    initializeDatabase();
    // Load DataTable
    loadData();

    const swipeHandler = new SwipeHandler();
    const toastsFactory = new ToastsFactory(swipeHandler);

    // Form Handling
    $("form[name=add-user]").submit(function(e) {
        // Validation
        if (!pname.value.match(nameRegex) ||
            !email.value.match(emailRegex) ||
            !password.value.match(passwordRegex)
        ) {
            toastsFactory.createToast({
                type: "error",
                icon: "info-circle",
                message: "Please correct all the fields",
                duration: 1000,
            });
            return false;
        }

        // Ajax Call
        var $form = $(this);
        var data = $form.serialize();
        $.ajax({
            url: "AddUser",
            type: "POST",
            data: data,
            success: function(resp) {
                $("#add-user")[0].reset();
                $("#formModal").modal("hide");
                [pname, email, password].forEach((ele) =>
                    ele.style.removeProperty("border")
                );
                sno += 1;
                addId += 1;
                var item = JSON.parse(resp);
                table.row
                    .add([addId, sno, item.username, item.email, item.role, item.joined])
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

    // Update User
    $("form[name=edit-user]").submit(function(e) {
        var $form = $(this);
        var data = $form.serialize();
        if (
            eusername.value == rowData[2] &&
            eemail.value == rowData[3] &&
            erole.value == rowData[4]
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
            url: "updateUser",
            data: data + "&id=" + uid,
            success: function(data, status, xhr) {
                table
                    .row(row)
                    .data([
                        uid,
                        rowData[1],
                        eusername.value,
                        eemail.value,
                        erole.value,
                        rowData[5],
                    ])
                    .draw(false);
                $("#formEditModal").modal("hide");
                toastsFactory.createToast({
                    type: "system",
                    icon: "check-circle",
                    message: "Updated successfully",
                    duration: 1000,
                });
                $("#edit-user")[0].reset();
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

    // Delete User
    $("form[name=delete-user]").submit(function(e) {
        $.ajax({
            type: "POST",
            url: "deleteUser",
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
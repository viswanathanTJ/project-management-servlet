var low = `<td class="priority-l"><img src="assets/icons/low.png" />&nbsp;Low</td></tr>`;
var med = `<td class="priority-m"><img src="assets/icons/medium.png" />&nbsp;Medium</td></tr>`;
var high = `<td class="priority-h"><img src="assets/icons/high.png" />&nbsp;High</td></tr>`;
function addCard(details) {
  let pri = details.priority;
  if (pri === 0) pri = low;
  else if (pri === 1) pri = med;
  else if (pri === 2) pri = high;
  cards.innerHTML += `<div class="col-xs-12 col-sm-6 col-md-3 col-lg-3">
                        <a pid="${details.p_id}" p-title="${details.name}" data-popover-content="#members" data-toggle="popover" data-trigger="focus">
                            <div class="card-flyer">
                                <div class="text-box">
                                    <div class="text-container" style="margin:5px">
                                        <h4><img width="20px" src="assets/icons/check.png" /> &nbsp;${details.name}
                                        </h4>
                                        <p class="text-muted text-start">${details.tasks} open tasks</p>
                                        <p class="desc">${details.desc}</p>
                                        <br>
                                        <table>
                                            <tr>
                                                <td>Created</td>
                                                <td>${details.createdat}</td>
                                            </tr>
                                            <tr>
                                                <td style="padding-right:40px">Owner</td>
                                                <td>${details.oname}</td>
                                            </tr>
                                            <tr>
                                                <td>Priority:</td>
                                                ${pri}
                                            </tr>
                                            <tr>
                                                <td>Team</td>
                                                <td>${details.team} Employee</td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </a>
                    </div>`;
}

function loadPop(content, pid, title) {
  heading.innerHTML = title;
  memberList.innerHTML = "";
  $.ajax({
    url: "getProjectMembers?pid=" + pid,
    type: "GET",
    async: false,
    success: function (resp) {
      let members = JSON.parse(resp).members;
      if (members.length === 0) {
        popMembers.hidden = true;
      } else {
        popMembers.hidden = false;
      }
      $.each(members, function (index, item) {
        if (item.isMember === "checked")
          memberList.innerHTML += `<li class="member">${item.name}</li>`;
      });
    },
  });
}

function updateClick() {

}

function addClick() {

}

function productList() {
  $.ajax({
    url: '/api/Product/',
    type: 'GET',
    dataType: 'json',
    success: function (products) {
      productListSuccess(products);
    },
    error: function (request, message, error) {
      handleException(request, message, error);
    }
  });
}


function productListSuccess(products) {
  $.each(products, function (index, product) {
    productAddRow(product);
  });
}

function productAddRow(product) {
 // Check if <tbody> tag exists, add one if not
  if ($("#productTable tbody").length == 0) {
   $("#productTable").append("<tbody></tbody>");
  }
  // Append row to <table>
  $("#productTable tbody").append(
    productBuildTableRow(product));
}

function productBuildTableRow(product) {
  var ret =
    "<tr>" +
     "<td>" + product.ProductName + "</td>" +
     "<td>" + product.IntroductionDate + "</td>"
      + "<td>" + product.Url + "</td>" +
    "</tr>";
  return ret;
}

function handleException(request, message, error) {
  var msg = "";
  msg += "Code: " + request.status + "\n";
  msg += "Text: " + request.statusText + "\n";
  if (request.responseJSON != null) {
    msg += "Message" + request.responseJSON.Message + "\n";
  }
  alert(msg);
}

$(document).ready(function () {
  productList();
});
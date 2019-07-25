$(function () {
    $("#alert-success").hide();
    $("#alert-danger").hide();
    $("input").keyup(function () {
        var pwd1 = $("#pwd1").val();
        var pwd2 = $("#pwd2").val();
        if (pwd1 != "" || pwd2 != "") {
            if (pwd1 == pwd2) {
                $("#alert-success").show();
                $("#alert-danger").hide();
                $("#submit").removeAttr("disabled");
                $("#modify").removeAttr("disabled");
                $("#remove").removeAttr("disabled");
            } else {
                $("#alert-success").hide();
                $("#alert-danger").show();
                $("#submit").attr("disabled", "disabled");
                $("#modify").attr("disabled", "disabled");
                $("#remove").attr("disabled", "disabled");
            }
        }
    });
});
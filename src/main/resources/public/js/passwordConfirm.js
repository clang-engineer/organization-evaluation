$(function () {
    $("#alert-success").hide();
    $("#alert-danger").hide();
    $("#pwd1,#pwd2").keyup(function () {
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

function checkPassword(password, id) {

    // if (!/^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$/.test(password)) {
    //     alert('숫자+영문자+특수문자 조합으로 8자리 이상 사용해야 합니다.');
    //     $('#pwd1').val('').focus();
    //     $('#pwd2').val('');
    //     return false;
    // }

    if (password.length<6) {
        alert('6자리 이상의 문자+숫자로 설정해주세요.');
        $('#pwd1').val('').focus();
        $('#pwd2').val('');
        return false;
    }

    var checkNumber = password.search(/[0-9]/g);
    var checkEnglish = password.search(/[a-z]/ig);
    if (checkNumber < 0 || checkEnglish < 0) {
        alert("숫자와 영문자를 혼용하여야 합니다.");
        $('#pwd1').val('').focus();
        $('#pwd2').val('');
        return false;
    }
    if (/(\w)\1\1\1/.test(password)) {
        alert('같은 문자를 4번 이상 사용하실 수 없습니다.');
        $('#pwd1').val('').focus();
        $('#pwd2').val('');
        return false;
    }

    if (password.search(id) > -1) {
        alert("비밀번호에 아이디가 포함되었습니다.");
        $('#pwd1').val('').focus();
        $('#pwd2').val('');
        return false;
    }
    return true;
}
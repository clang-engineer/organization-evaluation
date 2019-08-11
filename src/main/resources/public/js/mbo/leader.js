var leaderService = (function () {

    function read(dno, callback, error) {
        console.log("read");
        $.get("../../objects/leaders/" + dno,
            function (data) {
                if (callback) {
                    callback(data);
                }
            }).fail(function (xhr, status, err) {
                if (error) {
                    error();
                }
            });
    }

    function modify(param, callback, error) {
        $.ajax({
            type: 'put',
            url: '../../objects/leaders/' + param.dno,
            data: JSON.stringify(param),
            // data: param.content,
            contentType: "application/json; charset:utf-8",
            beforeSend: function (xhr) {
                xhr.setRequestHeader(param.csrf.headerName, param.csrf.token)
            },
            success: function (result, status, shr) {
                if (callback) {
                    callback(result);
                }
            },
            error: function (xhr, status, er) {
                if (error) {
                    error(er);
                }
            }
        });
    }

    return {
        read: read,
        modify: modify
    };
})();
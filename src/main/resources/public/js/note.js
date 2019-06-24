var noteService = (function () {
    function register(param, callback, error) {
        console.log("add.....");
        $.ajax({
            type: 'post',
            url: '/mbo/note/' + param.rno + "/" + param.step,
            //문자열을 전송 받기 위해 json parse와 contenttype지정을 제거 했다.
            // contentType: "application/json; charset:utf-8",
            data: 'note=' + param.note,
            beforeSend: function (xhr) {
                xhr.setRequestHeader(param.csrf.headerName, param.csrf.token)
            },
            success: function (result, status, xhr) {
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

    function read(param, callback, error) {
        console.log("read");
        $.get("/mbo/note/" + param.rno + "/" + param.step,
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


    return {
        register: register,
        read: read
    };
})();
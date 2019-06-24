var objectService = (function () {
    function register(param, callback, error) {
        console.log("add.....");
        $.ajax({
            type: 'post',
            url: '/object',
            data: JSON.stringify(param),
            contentType: "application/json; charset:utf-8",
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
        $.get("/object/" + param.mno,
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
            url: '/object/' + param.step + '/' + param.mno,
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

    function remove(param, callback, error) {
        $.ajax({
            type: 'delete',
            url: '/object/' + param.mno,
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
        register: register,
        read: read,
        modify: modify,
        remove: remove
    };
})();
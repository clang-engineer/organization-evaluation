var contentService = (function () {
    function register(param, callback, error) {
        console.log("add.....");
        $.ajax({
            type: 'post',
            url: '/contents/' + param.bno,
            data: JSON.stringify(param.content),
            contentType: "application/json; charset:utf-8",
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
        $.get("/contents/" + param.bno + "/" + param.idx,
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
            url: '/contents/' + param.bno + "/" + param.idx,
            data: JSON.stringify(param.content),
            contentType: "application/json; charset:utf-8",
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
            url: '/contents/' + param.bno + "/" + param.idx,
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

    function getList(bno, callback, error) {
        $.getJSON("/contents/" + bno,
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
        read: read,
        modify: modify,
        remove: remove,
        getList: getList
    };
})();
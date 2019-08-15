console.log("Relation Staff Module...")

var relationService = (function () {

    function getEvaluatedList(callback, error) {
        $.getJSON("evaluated", function (data) {
            if (callback) {
                callback(data);
            }
        }).fail(function (xhr, status, err) {
            if (error) {
                error();
            }
        });
    };

    function getEvaluatorList(param, callback, error) {
        $.getJSON("evaluators/" + param.sno, function (data) {
            if (callback) {
                callback(data);
            }
        }).fail(function (xhr, status, err) {
            if (error) {
                error();
            }
        });
    };

    function deleteRelation(param, callback, error) {
        $.ajax({
            type: 'delete',
            url: param.idx,
            beforeSend: function (xhr) {
                xhr.setRequestHeader(param.csrf.headerName, param.csrf.token)
            },
            success: function (result, status, shr) {
                alert("삭제 되었습니다.");
                window.location.reload();
            },
            error: function (xhr, status, er) {
                if (error) {
                    error(er);
                }
            }
        });
    }

    function deleteEvaluators(param, callback, error) {
        $.ajax({
            type: 'delete',
            url: 'evaluators/' + param.idx,
            beforeSend: function (xhr) {
                xhr.setRequestHeader(param.csrf.headerName, param.csrf.token)
            },
            success: function (result, status, shr) {
                alert("삭제 되었습니다.");
                window.location.reload();
            },
            error: function (xhr, status, er) {
                if (error) {
                    error(er);
                }
            }
        });
    }
    return {
        getEvaluatedList: getEvaluatedList,
        getEvaluatorList: getEvaluatorList,
        deleteRelation: deleteRelation,
        deleteEvaluators: deleteEvaluators
    };
})();
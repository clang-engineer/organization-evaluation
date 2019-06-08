console.log("Relation Staff Module...")

var relationService = (function () {

    function getEvaluatedList(tno, callback, error) {
        $.getJSON("/staff/evaluated/" + tno, function (data) {
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
        $.getJSON("/staff/evaluator/" + param.tno + "/" + param.sno, function (data) {
            if (callback) {
                callback(data);
            }
        }).fail(function (xhr, status, err) {
            if (error) {
                error();
            }
        });
    };


    return {
        getEvaluatedList: getEvaluatedList,
        getEvaluatorList: getEvaluatorList
    };
})();
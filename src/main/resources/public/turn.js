var turnManager = (function() {
    var getAll = function(obj, callback) {
	console.log("get All...");
	$.getJSON('/turns/' + obj, callback);
    };

    var add = function(obj, callback) {
	$.ajax({
	    type:'post',
	    url:'/turns/'+obj.cno,
	    data:JSON.stringify(obj),
	    datatype:'json',
	    contentType:"application/json",
	    success:callback
	});
    };

    var update = function(obj, callback) {
	console.log("update...");
    };

    var remove = function(obj, callback) {
	console.log("remove...");
    };

    return {
	getAll : getAll,
	add : add,
	update : update,
	remove : remove
    }
})();
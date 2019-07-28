var common = (function () {
    //시간 표시 함수
    function displayTime(timeValue) {
        var today = new Date();

        var dateObj = timeValue;

        var gap = today.getTime() - dateObj;

        var str = "";

        if (gap < (1000 * 60 * 60 * 24)) {
            var hh = dateObj.getHours();
            var mi = dateObj.getMinutes();
            var ss = dateObj.getSeconds();

            return [(hh > 9 ? '' : '0') + hh, ':', (mi > 9 ? '' : '0') + mi, ':', (ss > 9 ? '' : '0') + ss].join('');
        } else {
            var yy = dateObj.getFullYear();
            var mm = dateObj.getMonth() + 1;
            var dd = dateObj.getDate();

            return [yy, '/', (mm > 9 ? '' : '0') + mm, '/', (dd > 9 ? '' : '0') + dd].join('');
        }
    };

    return {
        displayTime: displayTime
    };
})();
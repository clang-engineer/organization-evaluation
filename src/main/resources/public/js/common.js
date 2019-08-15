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

    //파일 사이즈 및 형식 제한.
    var regex = new RegExp("(.*?)\.(xlsx)$");
    var maxSize = 5242880;//5MB

    function checkExtension(fileName, fileSize) {
        if (fileSize >= maxSize) {
            alert("파일 사이즈 초과");
            return false;
        }

        if (!regex.test(fileName)) {
            alert("xlsx 파일만 업로드 가능.")
            return false;
        }
        return true;
    }

    function historyMessage(history, msg) {
        if (history.state == null) {
            if (msg == 'register') {
                alert("등록 되었습니다.");
            } else if (msg == 'modify') {
                alert("수정 되었습니다.");
            } else if (msg == 'remove') {
                alert("삭제 되었습니다.");
            }
        }

        history.replaceState({}, null, null);
    }
    return {
        displayTime: displayTime,
        checkExtension: checkExtension,
        historyMessage: historyMessage
    };
})();
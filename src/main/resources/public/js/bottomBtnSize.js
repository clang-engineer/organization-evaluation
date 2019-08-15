function bottomBtnSize() {
    if ($(window).width() < 768) {
        $('#bottomBtn').addClass('btn-group-sm');
        $('#bottomBtn').removeClass('btn-group-xs');
        $('#bottomBtn').removeClass('btn-group-lg');
        $('#bottomBtn').removeClass('btn-group-vertical');

    } else if (($(window).width() < 1200)) {
        $('#bottomBtn').removeClass('btn-group-sm');
        $('#bottomBtn').addClass('btn-group-xs');
        $('#bottomBtn').removeClass('btn-group-lg');
        $('#bottomBtn').removeClass('btn-group-vertical');

    } else {
        $('#bottomBtn').removeClass('btn-group-sm');
        $('#bottomBtn').removeClass('btn-group-xs');
        $('#bottomBtn').addClass('btn-group-lg');
        $('#bottomBtn').addClass('btn-group-vertical');
    }
}
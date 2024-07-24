$(document).ready(function() {
    $('.menu-toggle').on('click', function() {
        $('.sidebar').toggleClass('open');
    });

    $(document).on('click', function(event) {
        if (!$(event.target).closest('.sidebar, .menu-toggle').length) {
            $('.sidebar').removeClass('open');
        }
    });
});

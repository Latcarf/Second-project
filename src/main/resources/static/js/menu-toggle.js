$(document).ready(function() {
    $('.menu-toggle').on('click', function() {
        $('.sidebar').toggleClass('open');
    });

    // Закрытие панели при клике вне ее области
    $(document).on('click', function(event) {
        if (!$(event.target).closest('.sidebar, .menu-toggle').length) {
            $('.sidebar').removeClass('open');
        }
    });
});

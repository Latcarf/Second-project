$(document).ready(function() {
    $('.opener').on('click', function() {
        $(this).next('.sub-menu').slideToggle();
        $(this).toggleClass('open');
    });
});

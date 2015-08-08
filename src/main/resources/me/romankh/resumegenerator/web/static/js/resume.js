$(function () {
    var $body = $(document.body);

    var navHeight = $('.navbar').outerHeight(true) + 10;
    //console.log(navHeight);
    navHeight = 10;

    $body.scrollspy({
        target: '.bs-sidebar',
        offset: navHeight
    });

    $('.bs-docs-container [href=#]').click(function (e) {
        e.preventDefault();
    });

    $body.on('activate.bs.scrollspy', function (e) {
    });

    $('.nav.bs-sidenav').on('click', function (e) {
        var href = e.target.href;
        var idx = href.lastIndexOf('#');
        if (idx > -1) {
            var id = href.substr(idx);
            var $el = $(id);
            $el.animate({
                'border-color': 'black'
            }, 200, function () {
                $el.animate({
                    'border-color': 'white'
                }, 400, function () {
                });
            });
        }
    });
});
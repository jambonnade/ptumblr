
jQuery(function ($) {
    $('.image-link').magnificPopup({ 'type': 'image' });
    
    $('button[data-url]').click(function (e) {
        var url = $(this).data('url');
        if (!url || url === '') {
            return;
        }
        
        var form = $('<form></form>', {
            'action': url,
            'method': 'post'
        });
        form.appendTo(document.body);
        form.submit();
    });
});

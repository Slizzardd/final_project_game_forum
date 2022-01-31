$(function () {

  $(".fw-placeholder").each(function () {
    $(this).find('textarea[value], input[value]').each(function () {
      $(this).val() ? $(this).parents('.fw-placeholder').addClass('active') : false
    });
  });

  $(".fw-placeholder .fw-input").on('focusout keyup', function () {
    if ($(this).val() === "") {
      $(this).parent().removeClass('active');
    } else {
      $(this).parent().addClass('active');
    }
  });


  var tempScrollTop, currentScrollTop = $(window).scrollTop();

  $(window).on('scroll load', function () {
    currentScrollTop = $(window).scrollTop();
    if (currentScrollTop > $('header').height() && !$('body').hasClass('hidden')) {
      $('body').addClass('fixed-header');
      if (tempScrollTop < currentScrollTop) {
        $('header').removeClass('show');
      } else if (tempScrollTop > currentScrollTop) {
        $('header').addClass('show');
      }
    } else {
      $('body').removeClass('fixed-header');
      $('header').removeClass('show');
    }
    tempScrollTop = currentScrollTop;
  });

  $('body').on('click', '[class *= "load--"]', function (evt) {
    let loadBtn = $(this);

    if (loadBtn.hasClass('disabled')) {
      return false;
    }

    const loadFile = String(loadBtn.attr('class').split(' ').filter(function (e) {
      return e.indexOf('load--') !== -1;
    })).replace("load--", "");

    $.ajax({
      url: '/includes/' + loadFile + '.html',
      type: "GET",
      contentType: false,
      processData: false,
      dataType: 'html',
      beforeSend: function () {
        loadBtn.addClass('disabled');
      },
      success: function (data) {
        $('body').append('<section class="float-box ' + loadFile + '"><div class="float-box_inset">' + data + '</div><div class="exit"></div></section>');

        setTimeout(function () {
          $('section.float-box').addClass('show');
        }, 50);
      },
      complete: function () {
        loadBtn.removeClass('disabled');
      }
    });
    return false;
  });


  $('body').click(function (e) {
    if ($(e.target).hasClass('float-box')) {
      let $floatbox = $('body').find('.float-box');

      $floatbox.removeClass('show');

      setTimeout(function () {
        $floatbox.remove();
      }, 500);
    }
  });


  $('header .login').click(function () {
    if ($(this).hasClass('true')) {
      document.location = 'lk.html';
      return false;
    }
  });
  $('#imageName').autocomplete({
      source: function (request, response) {
        $.get('http://' + document.location.hostname + '/suggestions?', {query: request.term}, function (data, status) {
          if (status === 'success') {
            response(data);
          }
        });
      }
    }
  );

  $('img[data-img]').click(function () {
    let link = document.location.protocol + '//' + document.location.hostname + ':' + document.location.port + '/' + $(this).attr('data-img');
    window.open(link);
  });

});
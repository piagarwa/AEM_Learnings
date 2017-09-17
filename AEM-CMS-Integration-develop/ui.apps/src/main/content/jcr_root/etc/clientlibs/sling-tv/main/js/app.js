$('.slider-for').slick({
    dots: false,
    slidesToShow: 7,
    slidesToScroll: 1,
    infinite: false,
    asNavFor: '.one-time',
    prevArrow: false,
    nextArrow: false,
    touchMove: false,
    responsive: [
        {
            breakpoint: 1024,
            settings: {
                slidesToShow: 5,
                slidesToScroll: 1
            }
        },
        {
            breakpoint: 768,
            settings: {
                slidesToShow: 4,
                slidesToScroll: 3
            }
        }
    ]
});
$('.one-time').slick({
    dots: false,
    slidesToShow: 7,
    slidesToScroll: 1,
    infinite: false,
    asNavFor: '.slider-for',
    prevArrow: '<div class="chevron-container-left"><i class="fa fa-chevron-left chevron"></i></div>',
    nextArrow: '<div class="chevron-container-right slick-next"><i class="fa fa-chevron-right slick-next chevron"></i></div>',
    touchMove: true,
    responsive: [
        {
            breakpoint: 1024,
            settings: {
                slidesToShow: 5,
                slidesToScroll: 1
            }
        },
        {
            breakpoint: 768,
            settings: {
                slidesToShow: 4,
                slidesToScroll: 3
            }
        }
    ]
});
$('.one-time').on('afterChange', function() {
    var width = Math.max(document.documentElement.clientWidth, window.innerWidth || 0);
    var currentSlide = $('.one-time').slick('slickCurrentSlide');
    if (currentSlide == 0) {
        $('.slick-prev, .chevron-container-left').hide();
        $('.slick-next').show();
    } else if (currentSlide > 0 ) {
    	$('.slick-prev, .chevron-container-left').show();
    }
});


$(document).ready(function() {
	//international Carousal
	for (i = 0; i < $("#count").val(); i++) {
        id = "#img"+i;
        $(id).click(function(e) {
            imgUrl = $(this).data("imagurl");
    		if(imgUrl.indexOf("/content/") >= 0) {
    			imgUrl = imgUrl+".html";
    		}
            window.open(imgUrl,'_blank');
        });
    }

	//Programming Carousel
	 $(".carousel-component .center").slick({
	        dots: false,
	        infinite: true,
	        centerMode: false,
	        slidesToShow: 6,
	        slidesToScroll: 1,
			autoplay: true, responsive: [
	    {
	      breakpoint: 1080,
	      settings: {
	        slidesToShow: 4,
	        slidesToScroll: 1,
	        infinite: true,
	        dots: false
	      }
	    },
	    {
	      breakpoint: 1024,
	      settings: {
	        slidesToShow: 4,
	        slidesToScroll: 1,
	        infinite: true,
	        dots: false
	      }
	    },
	    {
	      breakpoint: 600,
	      settings: {
	        slidesToShow: 3,
	        slidesToScroll: 1
	      }
	    },
	    {
	      breakpoint: 480,
	      settings: {
	        slidesToShow: 2,
	        slidesToScroll: 1
	      }
	    }
	  ]
	      });
	//End of Programming Carousel

    var data = sessionStorage.getItem('newco.auth_token');
    var activeBilledUser = sessionStorage.getItem('newco.activeBilledUser');
    if ((data && data.length > 0)) {
        $('.signOut').show();
        $('.signIn').hide();
        $('.my-account-btn').show();
    } else {
        $('.signOut').hide();
        $('.my-account-btn').hide();
        $('.signIn').show();
    }
    if(activeBilledUser=="true"&&data){
        $('.referaFriend').show();
    }else{
        $('.referaFriend').hide();
    }


    var data = sessionStorage.getItem('newco.auth_token')
    if ((data && data.length > 0)) {
        $('.signOut').show();
        $('.signIn').hide();
        $('.my-account-btn').show();
	    $('.referaFriend').show();

    } else {
        $('.signOut').hide();
        $('.my-account-btn').hide();
        $('.signIn').show();
		$('.referaFriend').hide();

    }
    $('.rich-text').find('.js-offset-center').parents('ul').addClass('offset-center');
    $('#signout-link').click(function() {
        sessionStorage.removeItem('newco.auth_token');
        sessionStorage.clear();
        $('.signOut').hide();
        $('.my-account-btn').hide();
        $('.signIn').show();
    })


    var currentSlide = $('.one-time').slick('slickCurrentSlide');
    if (currentSlide == 0) {
        $('.slick-prev, .chevron-container-left').hide();
    } else if (currentSlide == 3) {
        $('.slick-next').hide();
    }
    $('.carousel').carousel();

    $('.carousel-graph').carousel();
    $('.carousel-indicators li').on('click', function(e) {
        e.stopPropagation();
        var goTo = $(this).data('slide-to');
        $('.carousel-inner .item').each(function(index) {
            if ($(this).data('id') == goTo) {
                goTo = index;
                return false;
            }
        });
        $('.js-carouselgraph').carousel(goTo);
		$('#carousel-example-generic').carousel(goTo);
    });
    $('.js-feedbackModel').on('click', function() {
        $('.modal-backdrop').removeClass('hide').addClass('in');
    })
    $('.js-close').on('click', function() {
        $('.modal-backdrop').removeClass('in').addClass('hide');
    })
    $('#myTabs > div').on('click', function(e) {
        e.preventDefault()
            //  alert('helo');
        $(this).tab('show');
        $(this).addClass('active-tab');
        $(this).siblings().removeClass('active-tab');
    });
    $('.js-addChannels').on('click', function() {
        $(this).find('span').toggleClass('hide');
        $('#extras_table').fadeIn().toggleClass('hide');
    });
    $('#hp-content-ribbon_ul').slick({
        dots: false,
        slidesToShow: 5,
        slidesToScroll: 1,
        infinite: false,
        prevArrow: '<div id="#slider-next" class="content-arrow left"><span class="fa"></span></div>',
        nextArrow: '<div id="#slider-prev" class="content-arrow right"><span class="fa"></span></div>',
        touchMove: false
    });
    $('.js-sublink').on('click', function() {
        $(this).parent('.sublink').siblings('.container').removeClass('hide');
    })
    $('.js-exit').on('click', function() {
         $(this).parents('.js-offer-details-container').addClass('hide');
    })
    $('.js-morelink').on('click', function() {
        $('.js-morelink i').toggleClass('fa-minus');
        $('.js-morelink i').toggleClass('fa-plus');
        $('#genre-window').toggleClass('not-expanded');
        $('.js-morelink > span').toggleClass('hide');
        //$('.js-morelink').addClass('js-less-txt').removeClass('js-morelink');
    });
    $(window).scroll(function() {
       /* $(".genre-ribbon").stickem({
            item: '.outer__list',
            container: '.genre-ribbon',
            offset: 100
        });
        */
        if($("#genre-container").length){
	        if (($(window).scrollTop()+$(".homepage-header").height()) >= $("#genre-container").offset().top) {
	            $(".outer__list").css('top', (($(window).scrollTop()+$(".homepage-header").height())-$("#genre-container").offset().top));
	        } else {
	            $(".outer__list").css('top', "");
	        }
    	}
    });
    $('.js-services-link').on('click', function() {
        $('.js-sling-modal').removeClass('hide').addClass('in');
    })
    $('.close-modal').on('click', function() {
        $('.js-sling-modal').removeClass('in').addClass('hide');
    });
	try {
        SlingTVUtils.conditionalRedirectToBrowserPlayer();
    } catch(e) {
        console.log( e );
    }

    /* Hunt down appropriate @media rules and replace them */
    var mobileHeight = $('.genre-ribbon').data('slingMobileHeight');
    var desktopHeight = $('.genre-ribbon').data('slingDesktopHeight');
    if(desktopHeight || mobileHeight) {
      for(var i=0; i<document.styleSheets.length; i++) {
        var sheet = document.styleSheets[i];
        if(sheet.ownerNode.tagName === "LINK") {
          for(j in sheet.cssRules) {
            var rule = sheet.cssRules[j];
            if(rule.cssRules) { /* Is @media rule */
              for(k in rule.cssRules) {
                var subRule = rule.cssRules[k];
                if(subRule.selectorText === ".genre-ribbon .not-expanded") {
                  if(rule.conditionText.indexOf('max') > -1 && mobileHeight) {
                    subRule.style.height = mobileHeight + "px";
                  }
                  if(rule.conditionText.indexOf('min') > -1 && desktopHeight) {
                    subRule.style.height = desktopHeight + "px";
                  }
                }
              }
            }
          }
        }
      }
    }
	

    $('.genre-ribbon #genre-images.list-inline li.slick-slide').matchHeight({byRow: false});
    $('.deals-item').matchHeight({byRow: false});
    $('.offers-deals-component').parent('.row').removeClass('container center-block');
    $('.columncontrol').parent('.row').removeClass('center-block container');
});

function validateZip(e) {
  var key = e.keyCode || e.which;

  if((key > 64 && key < 91) || key > 106 || e.shiftKey || (e.target.value.length >= 5 && key > 47 && key < 58)) {
    e.preventDefault();
  }
}

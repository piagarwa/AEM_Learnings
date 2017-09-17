(function countdownConstructor() {
  var MILLISECONDS_IN_SECONDS = 1000, SECONDS_IN_MINUTES = 60, MINUTES_IN_HOURS = 60, HOURS_IN_DAY = 24;
  var SECONDS_IN_DAY = SECONDS_IN_MINUTES * MINUTES_IN_HOURS * HOURS_IN_DAY;
  var SECONDS_IN_HOUR = SECONDS_IN_MINUTES * MINUTES_IN_HOURS;
  var data = {
    initial: {
      element: null,
      start: null,
      showDays: true,
      showTime: true,
      tempLength: 0
    },
    countdowns: []
  };

  function leftPad(i) {
    while(i.toString().length < 2) {
      i = '0' + i;
    }
    return i;
  }

  function go() {
    $.each(data.countdowns, function(i, elem) {
      var countdown = data.countdowns[i];
      var now = new Date();
      var delta = parseInt((countdown.start - now) / MILLISECONDS_IN_SECONDS);
      if(delta < 0) {
        if(countdown.element.find('.countdown-end').text() && -delta < countdown.tempLength * SECONDS_IN_MINUTES) {
          countdown.element.addClass('countdown-finished');
          countdown.element.removeClass('countdown-datetime-only');
        }
        else {
          countdown.element.addClass('countdown-datetime-only');
          countdown.element.removeClass('countdown-finished');
        }
        return;
      }
      if(delta > countdown.secondsBeforeSwitch) {
        countdown.element.addClass('countdown-datetime-only');
      }
      else {
        countdown.element.removeClass('countdown-datetime-only');
      }
      if((delta > SECONDS_IN_DAY && countdown.showDays) || !countdown.showTime) {
        var days = parseInt(delta / SECONDS_IN_DAY);
        countdown.element.find('.countdown-days').find('.countdown-counter').text(days);
        delta -= days * SECONDS_IN_DAY;
      }
      else {
        countdown.element.find('.countdown-days').hide();
        countdown.element.find('.countdown-days-separator').hide();
      }
      if(delta > SECONDS_IN_HOUR) {
        var hours = parseInt(delta / SECONDS_IN_HOUR);
        countdown.element.find('.countdown-hours').find('.countdown-counter').text(days || !countdown.showTime ? leftPad(hours) : hours);
        delta -= hours * SECONDS_IN_HOUR;
      }
      if(delta > SECONDS_IN_MINUTES) {
        var minutes = parseInt(delta / SECONDS_IN_MINUTES);
        countdown.element.find('.countdown-minutes').find('.countdown-counter').text(leftPad(minutes));
        delta -= minutes * SECONDS_IN_MINUTES;
      }
      countdown.element.find('.countdown-seconds').find('.countdown-counter').text(leftPad(delta));
    });
  }

  function onReady() {
    var countdownElements = $('.countdown-container');
    countdownElements.each(function(){
      var countdown = $.extend({}, data.initial);
      countdown.element = $(this);
      countdown.start = new Date(countdown.element.data('slingCountdownStart').split(' ').join('T') + '-02:00');
      countdown.showDays = (countdown.element.data('slingCountdownDays') + '').toLowerCase() == "true";
      countdown.showTime = (countdown.element.data('slingCountdownTime') + '').toLowerCase() == "true";
      countdown.tempLength = countdown.element.data('slingCountdownTempLength');
      countdown.secondsBeforeSwitch = countdown.element.data('slingCountdownDaysSwitch') *
        SECONDS_IN_MINUTES * MINUTES_IN_HOURS * HOURS_IN_DAY;
      data.countdowns.push(countdown);
    });
    if(countdownElements.length) {
      setInterval(go, 1000);
    }
  }

  $(document).ready(onReady);
})();
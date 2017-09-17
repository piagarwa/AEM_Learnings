/**
 * Gamefinder
 */
(function gamefinderConstructor() {

  /**
   * Grabbed from AEM properties
   */
  var data = {
    baseURL: '/bin/MatchInfoServlet?filePath=/content/dam/sling-tv/misc/gamefinder/gamefinder.xlsx',
    seeAllHref: "./gamefinder",
    dateNames: {},
    defaultExpiration: 270,
    gameHorizon: 7,
    collapsed: false,
    paged: true,
    rowsPerPage: 3,
    stickyHeaders: false,

    /* Not from AEM */
    keyTimeout: 0,
    page: 0,
    affiliationPage: 0,
    raw: null,
    affiliations: null
  };

  /**
   * Pretty print for date
   */
  function dateFormat(dateTime) {
    var day = new Date(dateTime);
    return (day.getMonth() + 1) + '/' + day.getDate() + '/' + day.getFullYear().toString().substr(2);
  }

  /**
   * Grab properties
   */
  function onReady() {
    var gamefinder = $('.gamefinder-component');
    if(!gamefinder.length) {
      return;
    }
    data.collapsed = (gamefinder.data('slingGamefinderCollapsed') + '').toLowerCase() == "true";
  }

  /**
   * Pretty print for time
   */
  function timeFormat(dateTime) {
    var time = new Date(dateTime);
    var hours = time.getHours();
    var minutes = time.getMinutes();
    return (hours == 0 ? '12' : (hours > 12 ? hours - 12 : hours)) + ':' + (minutes > 9 ? minutes : '0' + minutes) + ' ' + (hours > 11 ? 'PM' : 'AM');
  }

  /**
   * Close or open drawer
   */
  function toggleDrawer(e) {
    var target = (e instanceof jQuery) ? e : $(this);
    var parentRow, detailRow;
    if(target.hasClass('gamefinder-results-collapsed') || target.hasClass('gamefinder-results-expanded')) {
      parentRow = target.prev();
      detailRow = target;
    }
    else {
      detailRow = target.next();
      parentRow = target;
    }
    detailRow.toggleClass('gamefinder-results-expanded').toggleClass('gamefinder-results-collapsed');
    parentRow.find('.gamefinder-results-expand').toggleClass('fa-chevron-down').toggleClass('fa-chevron-up');
  }



  /**
   * Error Handler
   */
  var error = (function errorConstructor() {
    function show(nodeName) {
      $('.gamefinder-results-row').not('.gamefinder-results-collapsed').hide();
      $('.gamefinder-results-' + nodeName).show();
      $('.gamefinder-results-prev-btn').hide();
      $('.gamefinder-results-next-btn').hide();
    }
    return {
      noAffiliation: function () {
        show('no-affiliation');
      },
      noResults: function () {
        show('none');
      },
      server: function () {
        show('server-error');
      },
      zip: function () {
        show('zip-error');
      }
    }
  })();


  /**
   * Handle affiliations (Paginate sports checkboxes)
   */
  var affiliation = (function() {
    var lines = [[]];
    var affiliationMap = {};
    var resizeTimeout = false;
    var transitionDefault = {
      in: {
        start: {
          position: 'relative',
          left: 1000
        },
        target: {
          left: 0
        },
        clear: {}
      },
      out: {
        start: {
          position: 'relative',
          left: 0
        },
        target: {
          left: 1000
        }
      },
      clear: {
        position: '',
        left: '',
        top: ''
      }
    };


    /**
     * Animate the transition between sets of affiliations
     *
     * @param {int} pageNum - The target page #
     */
    function animateTransition(pageNum) {
      var parent = lines[0][0].parent();
      var transition = $.extend(true, {}, transitionDefault);
      if(data.affiliationPage < pageNum) {
        transition.out.target.left *= -1;
      }
      if(data.affiliationPage > pageNum) {
        transition.in.start.left *= -1;
      }
      parent.height(parent.height());
      $.each(lines, function(lineNum, line) {
        $.each(line, function(idx, elem) {
          var $this = $(elem);
          //Terminate any existing animations
          $this.finish();
          if($this.height()) {
            if(transition.in.start.left > 0) {
              transition.in.start.top = -$this.height();
            }
            else {
              transition.out.start.top = -$this.height();
            }
          }
          if(lineNum == pageNum) {
            $this.css(transition.in.start).show()
              .animate(transition.in.target, {done: function(){$this.css(transition.clear);parent.css('height', '')}});
          }
          else {
            $this.css(transition.out.start)
              .animate(transition.out.target, {done: function(){$this.css(transition.clear).hide()}});
          }
        });
      });
    }

    /**
     * Change page
     *
     * @param {Event} e - The click event
     */
    function changePage(e) {
      showPage($(e.target).hasClass('gamefinder-sports-prev-btn') ? data.affiliationPage - 1 : data.affiliationPage + 1, true);
    }

    /**
     * Divide affiliations based on which line
     */
    function divide() {
      var nextTop = data.affiliations.first().parent().find('label').position().top;
      var i = 0;
      lines = [[]];
      resizeTimeout = false;
      data.affiliations.each(function(){
        var affiliationWrapper = $(this).parent();
        var currentTop = affiliationWrapper.find('label').position().top;
        if(currentTop == nextTop) {
          lines[i].push(affiliationWrapper);
        }
        else {
          nextTop = currentTop;
          i++;
          lines.push([]);
          lines[i].push(affiliationWrapper);
        }
      });
      if(lines.length < 2) {
        $('.gamefinder-sports-page-btn').hide();
        return;
      }
      console.log(lines);
      for(i=1;i<lines.length;i++) {
        $.each(lines[i], function() {
          $(this).hide();
        });
      }
      showPage(0);
    }

    /**
     * Get the label for a given affiliation
     *
     * "Lazy fetch" only searches when the key does not already exist
     *
     * @param {string} key - The key in the data set
     * @returns {string} - The text value of the label (Pretty printed version)
     */
    function getLabel(key) {
      var label;
      if(affiliationMap[key]) {
        label = affiliationMap[key];
      }
      else {
        data.affiliations.each(function (idx, elem) {
          elem = $(elem);
          if(elem.val() == key) {
            label = elem.parent().find('label').text();
          }
          return elem.val() != key;
        });
        if(!label) {
          throw new Error('Unknown affiliation!');
        }
        affiliationMap[key] = label;
      }
      return label;
    }

    /**
     * Set up click events and such
     */
    function onReady() {
      var gamefinder = $('.gamefinder-component');
      if(!gamefinder.length) {
        return;
      }
      data.affiliations = $('.gamefinder-sport-container').find('input');
      data.affiliations.each(function() {
        var elem = $(this);
        elem.val(elem.val().toLowerCase());
        elem.change(filter.go());
      });
      divide();

      $('.gamefinder-sports-page-btn').click(changePage);
      $(window).resize(onResize);
    }

    /**
     * Recalculate "pages"
     */
    function onResize() {
      if($.cookie('wcmmode')) {
        console.log('Skipping resize while in author mode');
        return;
      }
      $('.gamefinder-sport-wrapper').show();
      /* As timeout because the UI needs to update before we can recalculate */
      if(!resizeTimeout) {
        resizeTimeout = setTimeout(divide, 100);
      }
    }

    /**
     * Show a given page (hide the others)
     *
     * @param {int} pageNum - The page # to show
     * @param {boolean=false} animate - Whether or not to animate the transition
     */
    function showPage(pageNum, animate) {
      pageNum = pageNum < 0 ? 0 : (pageNum > lines.length - 1 ? lines.length - 1 : pageNum);
      if(animate) {
        animateTransition(pageNum)
      }
      else {
        $.each(lines, function(lineNum, line) {
          $.each(line, function(idx, elem) {
            var $this = $(elem);
            //Terminate any existing animations
            $this.finish();
            if(lineNum == pageNum) {
              $this.show();
            }
            else {
              $this.hide();
            }
          });
        });
      }
      //Hide/show buttons
      if(pageNum == 0) {
        $('.gamefinder-sports-prev-btn').hide();
      }
      else {
        $('.gamefinder-sports-prev-btn').show();
      }
      if(pageNum == lines.length - 1) {
        $('.gamefinder-sports-next-btn').hide();
      }
      else {
        $('.gamefinder-sports-next-btn').show();
      }
      data.affiliationPage = pageNum;
    }


    $(document).ready(onReady);

    return {
      getLabel: getLabel
    }
  })();


  /**
   * Date Selector
   */
  var dateSelector = (function dateSelectorConstructor() {

    /**
     * Populate data node w/ authored day && month names from DOM
     */
    function grabDateNames() {
      var todayMobile = $('#today-mob').val();
      var today = $('#today').val();
      var weekMobile = $('#week-mob').val();
      var week = $('#week').val();
      data.dateNames = {
        days: grabDayNames(),
        months: grabMonthNames(),
        current: {
          today: {
            mobile: todayMobile ? todayMobile : 'Today',
            desktop: today ? today : 'TODAY'
          },
          thisWeek: {
            mobile: weekMobile ? weekMobile : 'View This Week',
            desktop: week ? week : 'THIS<br/>WEEK'
          }
        }
      }
    }

    /**
     * Grab authored day names from DOM
     */
    function grabDayNames() {
      var sundayMobile = $('#sunday-mob').val();
      var mondayMobile = $('#monday-mob').val();
      var tuesdayMobile = $('#tuesday-mob').val();
      var wednesdayMobile = $('#wednesday-mob').val();
      var thursdayMobile = $('#thursday-mob').val();
      var fridayMobile = $('#friday-mob').val();
      var saturdayMobile = $('#saturday-mob').val();
      var sunday = $('#sunday').val();
      var monday = $('#monday').val();
      var tuesday = $('#tuesday').val();
      var wednesday = $('#wednesday').val();
      var thursday = $('#thursday').val();
      var friday = $('#friday').val();
      var saturday = $('#saturday').val();
      return {
        mobile: [
          sundayMobile ? sundayMobile : 'Sunday',
          mondayMobile ? mondayMobile : 'Monday',
          tuesdayMobile ? tuesdayMobile : 'Tuesday',
          wednesdayMobile ? wednesdayMobile : 'Wednesday',
          thursdayMobile ? thursdayMobile : 'Thursday',
          fridayMobile ? fridayMobile : 'Friday',
          saturdayMobile ? saturdayMobile : 'Saturday'
        ],
        desktop: [
          sunday ? sunday : 'SUN',
          monday ? monday : 'MON',
          tuesday ? tuesday : 'TUES',
          wednesday ? wednesday : 'WED',
           thursday ? thursday : 'THUR',
          friday ? friday : 'FRI',
          saturday ? saturday : 'SAT'
        ]
      }
    }

    /**
     * Grab authored month names from DOM
     */
    function grabMonthNames() {
      var janMobile = $('#january-mob').val();
      var febMobile = $('#february-mob').val();
      var marMobile = $('#march-mob').val();
      var aprMobile = $('#april-mob').val();
      var mayMobile = $('#may-mob').val();
      var junMobile = $('#june-mob').val();
      var julMobile = $('#july-mob').val();
      var augMobile = $('#august-mob').val();
      var sepMobile = $('#september-mob').val();
      var octMobile = $('#october-mob').val();
      var novMobile = $('#november-mob').val();
      var decMobile = $('#december-mob').val();
      var jan = $('#january').val();
      var feb = $('#february').val();
      var mar = $('#march').val();
      var apr = $('#april').val();
      var may = $('#may').val();
      var jun = $('#june').val();
      var jul = $('#july').val();
      var aug = $('#august').val();
      var sep = $('#september').val();
      var oct = $('#october').val();
      var nov = $('#november').val();
      var dec = $('#december').val();
      return {
        mobile: [
          janMobile ? janMobile : 'January',
          febMobile ? febMobile : 'February',
          marMobile ? marMobile : 'March',
          aprMobile ? aprMobile : 'April',
          mayMobile ? mayMobile : 'May',
          junMobile ? junMobile : 'June',
          julMobile ? julMobile : 'July',
          augMobile ? augMobile : 'August',
          sepMobile ? sepMobile : 'September',
          octMobile ? octMobile : 'October',
          novMobile ? novMobile : 'November',
          decMobile ? decMobile : 'December'
        ],
        desktop: [
          jan ? jan : 'Jan',
          feb ? feb : 'Feb',
          mar ? mar : 'Mar',
          apr ? apr : 'Apr',
          may ? may : 'May',
          jun ? jun : 'Jun',
          jul ? jul : 'Jul',
          aug ? aug : 'Aug',
          sep ? sep : 'Sep',
          oct ? oct : 'Oct',
          nov ? nov : 'Nov',
          dec ? dec : 'Dec'
        ]
      }
    }

    /**
     * Set up click events and such
     */
    function onReady() {
      var gamefinder = $('.gamefinder-component');
      if(!gamefinder.length) {
        return;
      }
      $('.gamefinder-date-button').click(toggle);
      $('.gamefinder-date-pseudo-select').click(select);
      $('.gamefinder-date-pseudo-select-overlay').click(toggle);
      grabDateNames();
      populateDates();
    }

    /**
     * Set correct dates in HTML
     */
    function populateDates() {
      var names = data.dateNames;
      var children = $('.gamefinder-date-pseudo-select').children();

      $(children[0]).html(
        '<div class="gamefinder-date-mobile">' + names.current.thisWeek.mobile + '</div>' +
        '<div class="gamefinder-date-desktop">' + names.current.thisWeek.desktop + '</div>'
      );

      var today = new Date();
      $(children[1]).html(
        '<div class="gamefinder-date-mobile" data-sling-date="' + today + '">' + names.current.today.mobile
        + ',&nbsp;' + names.months.mobile[today.getMonth()] + ".&nbsp;" + today.getDate() + '</div>' +
        '<div class="gamefinder-date-desktop" data-sling-date="' + today + '">' + names.current.today.desktop
        + '<br/><small>' + names.months.desktop[today.getMonth()] + "&nbsp;" + today.getDate() + '</small></div>'
      );

      //Skip "This Week" and "Today"
      for(var i=2;i<8;i++) {
        var day = new Date();
        day.setDate(day.getDate() + i - 1);
        $(children[i]).html(
          '<div class="gamefinder-date-mobile" data-sling-date="' + day + '">' + names.days.mobile[day.getDay()] +
          ",&nbsp;" + names.months.mobile[day.getMonth()] + "&nbsp;" + day.getDate() + '</div>' +
          '<div class="gamefinder-date-desktop" data-sling-date="' + day + '">' + names.days.desktop[day.getDay()] +
          "<br/><small>" + names.months.desktop[day.getMonth()] + "&nbsp;" + day.getDate() + '</small></div>'
        );
      }
    }

    /**
     * Handles click event and stores selected date
     *
     * @param {Event} e Click Event
     */
    function select(e) {
      var select = $('.gamefinder-date-pseudo-select');
      $('.gamefinder-date-pseudo-select-overlay').hide();
      select.slideUp();
      $('.gamefinder-date-button').addClass('fa-chevron-down').removeClass('fa-chevron-up');
      select.find('.gamefinder-date-selected').removeClass('gamefinder-date-selected');
      var target = e.target.tagName === 'SMALL' ? $(e.target).parent() : $(e.target);
      target.parent().addClass('gamefinder-date-selected');

      if(target.parent().hasClass('gamefinder-date-all')) {
        data.date = false;
      }
      else {
        data.date = new Date(target.data('sling-date'));
      }
      filter.go();
    }

    /**
     * Hide/Show date selector
     */
    function toggle() {
      var select = $('.gamefinder-date-pseudo-select');
      if(select.is(':hidden')) {
        $('.gamefinder-date-button').removeClass('fa-chevron-down').addClass('fa-chevron-up');
        $('.gamefinder-date-pseudo-select-overlay').show();
        select.slideDown();
      }
      else {
        $('.gamefinder-date-button').addClass('fa-chevron-down').removeClass('fa-chevron-up');
        $('.gamefinder-date-pseudo-select-overlay').hide();
        select.slideUp();
      }
    }

    $(document).ready(onReady);
  })();



  /**
   * Filters
   */
  var filter = (function filterConstructor() {

    /**
     * Do AJAX call
     */
    function ajax() {
      var URL = data.baseURL;
      $.ajax(URL, {
        error: function(xhr) {
          console.log(xhr);
          error.server();
        },
        success: function (resp) {
          handleResponse(resp);
        }
      });
    }

    /**
     * Do the thing
     */
    function apply(rows, searchText, zipText) {
      return rows.filter(function(idx, elem) {
        var $this = $(elem);
        var id = $this.data('slingGamefinderGameId');
        if(data.date) {
          if(!$this.data('slingGamefinderDate')) {
            return false;
          }
          var gameDay = new Date($this.data('slingGamefinderDate'));
          if(gameDay.getDate() != data.date.getDate() ||
            gameDay.getMonth() != data.date.getMonth()) {
            return false;
          }
        }
        if(!$this.data('slingGamefinderAffiliation')) {
          return false;
        }
        var affiliated = true;
        data.affiliations.each(function() {
          if(!this.checked && this.value == $this.data('slingGamefinderAffiliation')) {
            affiliated = false;
            return false;
          }
        });
        if(!affiliated) {
          return false;
        }
        var game = data.games[id];
        if(zipText) {
          if(game.zip_list && game.zip_list.trim().toUpperCase().indexOf('ALL') == -1
            && game.zip_list.indexOf(zipText.trim()) == -1) {
            return false;
          }
        }
        if(searchText) {
          var searchRegex = new RegExp('(?=.*' + searchText.trim().split(' ').join(')(?=.*') + ')', 'i');
          if(game.search_data && !searchRegex.test(game.search_data)) {
            return false;
          }
        }
        return true;
      });
    }

    /**
     * Discard expired games
     *
     * @param {Object[]} games - The array of games
     * @param {string} games[].game_datetime - The date & time the game will start. (YYYY-MM-DD HH:ii:ss)
     * @param {string} games[].affiliation - The sport/league
     * @param {int=} games[].expiration - The # of minutes for the game to persist beyond the start time
     */
    function discard(games) {
      var affiliationKeys = data.affiliations.get().map(function(elem){return $(elem).val()});
      return games.filter(function(game, idx) {
        var gameDate = new Date(game.game_datetime);
        var horizonDate = (new Date().setDate((new Date()).getDate() + data.gameHorizon));
        var expireDate = new Date(gameDate.getTime() + (game.expiration ? game.expiration : data.defaultExpiration) * 60000);
        expireDate.setHours(expireDate.getHours() + 4);
        expireDate.setMinutes(expireDate.getMinutes() - (new Date()).getTimezoneOffset());
        return (new Date()) < expireDate && gameDate < horizonDate && affiliationKeys.indexOf(game.affiliation) != -1;
      });
    }

    /**
     * Apply filters
     */
    function go() {
      var zipText = $('#gamefinder-zip').val();
      var searchText = $('#gamefinder-search').val();

      clearTimeout(data.keyTimeout);
      reset();

      if(data.seeAllHref) {
        var href = data.seeAllHref;
        if (searchText || zipText) {
          href += '?' + (searchText ? 'gamefinder-search=' + searchText : '') + (searchText && zipText ? '&' : '') +
            (zipText ? 'gamefinder-zip=' + zipText : '');
        }
        $('.gamefinder-see-all').attr('href', href);
      }

      if(zipText && (zipText != ~~zipText || zipText.length != 5)) {
        error.zip();
        return;
      }
      if(zipText) {
        $('.gamefinder-results-details-no-zip').hide();
        $('.gamefinder-results-details-with-zip').show();
      }
      else {
        $('.gamefinder-results-details-with-zip').hide();
        $('.gamefinder-results-details-no-zip').show();
      }
      var resultsRows = $('.gamefinder-results-row');
      var rows = apply(resultsRows, searchText, zipText);
      if(!data.affiliations.filter(':checked').length) {
        error.noAffiliation();
        return;
      }
      resultsRows.not('.gamefinder-results-collapsed').not(rows).hide();
      rows.addClass('gamefinder-results-filtered');
      if(!rows.length) {
        error.noResults();
      }
      var isOdd = false;
      rows.each(function(idx, elem) {
        isOdd = !isOdd;
        if(!isOdd) {
          return;
        }
        $(elem).addClass('gamefinder-results-odd-row').next().addClass('gamefinder-results-odd-row');
      });
      pages.mark();
    }

    /**
     * Handle the response from AJAX
     *
     * @param {string} resp - The server response (JSON)
     */
    function handleResponse(resp) {
      try {
        data.raw = JSON.parse(resp);
        data.games = JSON.parse(resp).games;
      }
      catch(e) {
        error.server();
        return;
      }
      data.games.sort(function(a,b) {
        if(a.sort_order === "" && b.sort_order !== "") {
          return 1;
        }
        if(a.sort_order !== "" && b.sort_order === "") {
          return -1;
        }
        if(a.sort_order > b.sort_order) {
          return 1;
        }
        if(a.sort_order < b.sort_order) {
          return -1;
        }
        return (a.game_datetime > b.game_datetime ? 1 : -1);
      });
      $.each(data.games, function(idx, game) {
        var utcTime = new Date(game.game_datetime.split(' ').join('T') + 'Z');
        utcTime.setSeconds(utcTime.getSeconds() + 30);
        try {
          game.game_datetime = new Date(utcTime.toLocaleString("en-US", {timeZone: "America/New_York"}));
        }
        catch(e) {
          /* If IE */
          utcTime.setHours(utcTime.getHours() - 4);
          utcTime.setMinutes(utcTime.getMinutes() + (new Date()).getTimezoneOffset());
          game.game_datetime = utcTime;
        }
      });
      data.games = discard(data.games);
      injectRows(data.games);
      go();
    }

    /**
     * Remove existing table rows and replace with info in {games}
     *
     * @param {Object[]} games - The list of games
     * @param {string} games[].affiliation - The sport/league
     * @param {string} games[].game_datetime - The start date & time (YYYY-MM-DD HH:ii:ss)
     * @param {string} games[].network - The network showing the game
     * @param {string=} games[].channel_link - A URL associated w/ the network
     * @param {string} games[].display_data_mobile - A description of the game (Short)
     * @param {string} games[].display_data_desktop - A description of the game (Long)
     * @param {string} games[].drawer_display_nozip - Further details of the game (Global viewers)
     * @param {string} games[].drawer_display_zip - Further details of the game (Local viewers)
     */
    function injectRows(games) {
      $('.gamefinder-results-row').remove();
      var table = $('.gamefinder-results-container').find('table');
      $.each(games, function(idx, game) {
        table.append('<tr class="gamefinder-results-row" data-sling-gamefinder-affiliation="' + game.affiliation +
          '" data-sling-gamefinder-date="' + game.game_datetime + '" data-sling-gamefinder-game-id="' + idx + '">' +
          '<td>' + dateFormat(game.game_datetime) + '</td>' +
          '<td>' + timeFormat(game.game_datetime) + '</td>' +
          '<td>' + (game.channel_link ? '<a href="' + game.channel_link + '" target="_blank">' + game.network + '</a>' : game.network) +
          '</td><td><span class="gamefinder-results-display-mobile">' + game.display_data_mobile +
          '</span><span class="gamefinder-results-display-desktop">' + game.display_data_desktop + '</span></td>' +
          '<td class="fa fa-chevron-down gamefinder-results-expand"></td></tr>' +
          '<tr class="gamefinder-results-collapsed gamefinder-results-row">' +
          '<td class="gamefinder-results-affiliation">' + affiliation.getLabel(game.affiliation) + '</td>' +
          '<td colspan="4" class="gamefinder-results-details"><div class="gamefinder-results-details-with-zip">' + game.drawer_display_zip +
          '</div><div class="gamefinder-results-details-no-zip">' + game.drawer_display_nozip + '</div></td></td>'
        );
      });
      $('.gamefinder-results-row').click(toggleDrawer);
    }

    /**
     * On Key Function for search && zip
     *
     * @param {Event} e
     */
    function onKeyDown(e) {
      var key = e.keyCode || e.which;
      
      if(key == 13) {
        go();
        return;
      }
      if(e.target.id == 'gamefinder-zip') {
        validateZip(e);
      }
      clearTimeout(data.keyTimeout);
      data.keyTimeout = setTimeout(go, 2500);
    }

    /**
     * Set up click events and such and make initial AJAX request
     */
    function onReady() {
      var gamefinder = $('.gamefinder-component');
      if(!gamefinder.length) {
        return;
      }
      data.stickyHeaders = $('.gamefinder-component').data('slingGamefinderSticky');
      data.seeAllHref = $('.gamefinder-see-all').attr('href');
      $('#gamefinder-search').keydown(onKeyDown).blur(go);
      $('#gamefinder-zip').keydown(onKeyDown).blur(go);
      reset();
      ajax();
    }

    /**
     * Reset the table to default (unfiltered) so that filters can be applied
     */
    function reset() {
      $('.gamefinder-results-row').removeClass('gamefinder-results-odd-row').each(function() {
        var $this = $(this);
        if($this.hasClass('gamefinder-results-expanded')) {
          toggleDrawer($this);
        }
        else if(!$this.hasClass('gamefinder-results-collapsed')) {
          $this.removeClass('gamefinder-results-filtered').show();
        }
      });
      $('.gamefinder-results-zip-error').hide();
      $('.gamefinder-results-none').hide();
      $('.gamefinder-results-no-affiliation').hide();
      $('.gamefinder-results-server-error').hide();
    }

    $(document).ready(onReady);

    return {
      go: go
    };
  })();



  /**
   * Pagination
   */
  var pages = (function paginationConstructor() {

    /**
     * Change page
     *
     * @param {Event} e - The click event
     */
    function changePage(e) {
      showPage($(e.target).hasClass('gamefinder-results-prev-btn') ? data.page - 1 : data.page + 1);
    }

    /**
     * Mark rows w/ page number
     */
    function mark() {
      var i = 0, page = 0;
      if(!data.paged) {
        $('.gamefinder-results-filtered').each(function(idx, elem) {
          var row = $(elem);
          if(i >= data.rowsPerPage) {
            row.hide();
          }
          else if(!data.collapsed) {
            toggleDrawer(row);
          }
          i++;
        });
        $('.gamefinder-results-page-btn').hide();
      }
      else {
        $('.gamefinder-results-filtered').each(function(idx, elem) {
          if(i >= data.rowsPerPage) {
            i = 0;
            page++;
          }
          $(elem).data('sling-gamefinder-results-page', page);
          i++;
        });
        data.maxPage = page;
        showPage(0);
      }
    }

    /**
     * Set up click events and such
     */
    function onReady() {
      var gamefinder = $('.gamefinder-component');
      if(!gamefinder.length) {
        return;
      }
      data.rowsPerPage = gamefinder.data('slingGamefinderMaxRows');
      data.rowsPerPage = data.rowsPerPage ? data.rowsPerPage : 1000000;
      data.paged = (gamefinder.data('slingGamefinderPaged') + '').toLowerCase() == "true";
      $('.gamefinder-results-page-btn').click(changePage);
    }

    /**
     * Show a given page (hide the others)
     *
     * @param {int} pageNum - The page # to show
     */
    function showPage(pageNum) {
      pageNum = pageNum < 0 ? 0 : (pageNum > data.maxPage ? data.maxPage : pageNum);
      $('.gamefinder-results-filtered').each(function(idx, elem) {
        var $this = $(elem);
        if($this.data('sling-gamefinder-results-page') == pageNum) {
          $this.show();
          if(!data.collapsed) {
            toggleDrawer($this);
          }
        }
        else {
          $this.hide();
          if($this.next().hasClass('gamefinder-results-expanded')) {
            toggleDrawer($this);
          }
        }
      });
      //Hide/show buttons
      if(pageNum == 0) {
        $('.gamefinder-results-prev-btn').hide();
      }
      else {
        $('.gamefinder-results-prev-btn').show();
      }
      if(pageNum == data.maxPage) {
        $('.gamefinder-results-next-btn').hide();
      }
      else {
        $('.gamefinder-results-next-btn').show();
      }
      data.page = pageNum;
    }

    $(document).ready(onReady);

    return {
      mark: mark
    };
  })();


  var stickyHeaders = (function stickyHeadersConstructor() {

    var top, headerHeight;

    /**
     * Stick/Unstick table headers, etc.
     */
    function alignHeaders() {
      var searchHeight, sportHeight, fakeHeaderHeight, pastIt, magicPadding;
      var resultsContainer = $('.gamefinder-results-container');
      var searchContainer = $('#gamefinder-search-container');
      var sportsContainer = $('#gamefinder-sport-container');
      var falseHeader = $('.gamefinder-results-false-header');
      var datePicker = $('.gamefinder-date-pseudo-select');
      var bottom = resultsContainer.offset().top + resultsContainer.outerHeight();
      var inIt = $(document).scrollTop() > (top - (headerHeight));
      if(inIt) {
        falseHeader.append(datePicker);
        searchContainer.addClass('gamefinder-search-fixed');
        searchContainer.css('top', headerHeight);
        //Must be calculated after adding fixed class
        searchHeight = searchContainer.outerHeight();
        sportsContainer.addClass('gamefinder-sport-fixed');
        sportsContainer.css('top', headerHeight + searchHeight);
        //Must be calculated after adding fixed class
        sportHeight = sportsContainer.outerHeight();
        resultsContainer.addClass('gamefinder-results-fixed');
        falseHeader.css('top', headerHeight + searchHeight + sportHeight);
        fakeHeaderHeight = falseHeader.outerHeight();
        pastIt = $(document).scrollTop() > (bottom - (headerHeight + fakeHeaderHeight + sportHeight + searchHeight));
        if(pastIt) {
          magicPadding = headerHeight + 1;
          searchContainer.css('top', -($(document).scrollTop() - (bottom - (headerHeight + fakeHeaderHeight + sportHeight + searchHeight - magicPadding))));
          sportsContainer.css('top', -($(document).scrollTop() - (bottom - (sportHeight + fakeHeaderHeight + headerHeight - magicPadding))));
          falseHeader.css('top', -($(document).scrollTop() - (bottom - (headerHeight + fakeHeaderHeight - magicPadding))));
        }
      }
      else {
        datePicker.insertBefore($('.gamefinder-date-pseudo-select-overlay'));
        searchContainer.removeClass('gamefinder-search-fixed');
        sportsContainer.removeClass('gamefinder-sport-fixed');
        resultsContainer.removeClass('gamefinder-results-fixed');
      }
      clearTimeout(data.scrollTimeout);
      data.scrollTimeout = false;
    }

    /**
     * Set up scroll event
     */
    function onReady() {
      var gamefinder = $('.gamefinder-component');
      if(!gamefinder.length) {
        return;
      }
      data.stickyHeaders = (gamefinder.data('slingGamefinderSticky') + '').toLowerCase() == "true";
      if(data.stickyHeaders) {
        $(document).scroll(scrollDebounce);
      }
      top = $('#gamefinder-search-container').offset().top;
      headerHeight = $('header').children().first().outerHeight();
      $(window).on('resize', function() {
        top = $('#gamefinder-search-container').offset().top;
        headerHeight = $('header').children().first().outerHeight();
      })
    }


    /**
     * Prevent scroll event from firing more than every 5 milliseconds
     *
     * @param {Event} e - The scroll event
     */
    function scrollDebounce(e) {
      if(!data.scrollTimeout) {
        data.scrollTimeout = setTimeout(alignHeaders, 5);
      }
    }

    $(document).ready(onReady);

  })();


  $(document).ready(onReady);

})();

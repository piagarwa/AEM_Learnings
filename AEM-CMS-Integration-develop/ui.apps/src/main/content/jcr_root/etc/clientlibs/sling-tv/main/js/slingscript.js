function loadChannels(addArr) {
    var allChannels = $('#allChannels').val().split(",");
    if(allChannels != "") {
    	$.each(allChannels,function(i){
            var quotedVar = '#'+allChannels[i];
            if($(quotedVar).is(':visible')){
    	$(quotedVar).removeClass('active');
            } else {
                return true;
            }
    	});
    }
    if(addArr != "") {
    	$.each(addArr,function(i){
            var quotedVar = '#'+addArr[i];
            if($(quotedVar).is(':visible')){
    	$(quotedVar).addClass('active');
            } else {
                return true;
            }
    	});
    }
}

function callAjax(extrasId) {
	extraPagePath = $('#extraPagePath').val();
	
	 $.ajax({
         type: 'GET',    
         url:'/bin/ExtrasServlet',
         data:'extrasId='+extrasId+'&extraPagePath='+extraPagePath,
         success: function(response) {
         	data = JSON.parse(response);
         	var extraItems = data.extras;
            if(extraItems.length==0){ 
                alert("Extras Not Found");
            } else {  
                $('#extras').html('');
    	var extraDiv = '<div class="row light-gradient">'+
                      	'<div class="col-md-10 col-md-offset-1">'+
                            '<div class ="row">'+
                        	'<div class="text-center ng-scope" >'+
                       			 '<div class="col-xs-12 text-center pointer" id= "morechannels" >'+
                       				 	'<h6 class="extras-headline extra-bold text-uppercase ng-binding">'+data.heading+'</h6>'+
                       					 '<p class="bold extras-subheadline">'+data.subHeading+'</p>'+
                       					 '<h1 class="extras-btn bold text-uppercase"><span class="fa fa-plus" id="plusminus" ></span></h1>'+
                       				'</div>'+
                       			'</div>'+
                               '</div>'+     
    							'<div id="extra_content" style="display:none">';
                $.each(extraItems, function (i) {
                    var extraDivItems;
                    var extraItem = extraItems[i];
                    extraDivItems = '<div class="row ng-scope ng-hide" id="extras_table">'+
										'<div class="col-md-12 extras-title visible-sm visible-xs">'+
											'<h5 class="text-uppercase ng-binding">'+extraItem.title+'</h5>'+
										'</div>'+
										'<div>'+
                                            '<div class="extras-title-desktop hidden-sm hidden-xs">'+
                                                '<div class="row">'+
                                                    '<div class="extras-table-col-40">'+
                                                        '<hr>'+
                                                     '</div>'+
                                                     '<div class="extras-table-col-20">'+
                                                       	'<h4 class="text-uppercase text-center ng-binding">'+extraItem.title+'</h4>'+
													'</div>'+
                                                    '<div class="extras-table-col-40">'+
                                                       	'<hr>'+
                                                    '</div>'+
												'</div>'+
                                             '</div>'+
                                         '</div>'+ 
                                         '<div class="extras_box">';
											$.each(extraItem.subheadings, function (j) {
											var subHeadingItem = extraItem.subheadings[j];
											extraSubHeading = '<div class="extra_box ng-scope">'+
												'<div class="row">'+
													'<h5 class="extra-name extra-bold text-uppercase col-md-12 ng-binding">'+subHeadingItem.title+'</h5>'+
												'</div>'+
												'<div class="row">';
                                                 extraSubHeading = extraSubHeading+'<ul class="extra-channels-images col-sm-12 hidden-xs">';
												if(subHeadingItem.hasOwnProperty('logos')){
													$.each(subHeadingItem.logos, function (k) {
													var logos = subHeadingItem.logos[k];
													var logoPath = logos.path.replace(/\s/g,"%20");
													extraSubHeading = extraSubHeading +'<li  class="ng-scope"><img alt='+logos.altText+' src='+logoPath+'></li>';
													});
												}
                                                extraSubHeading = extraSubHeading+'</ul>';
												extraSubHeading= extraSubHeading+'<ul class="extra-channels-list">';
												 if ($(window).width()<700) {
													 if(subHeadingItem.hasOwnProperty('logos')){
														 $.each(subHeadingItem.logos, function (a) {
														var logos = subHeadingItem.logos[a];
														extraSubHeading = extraSubHeading +'<li class="col-sm-12 col-xs-6 channel visible-xs ng-scope" >'+logos.altText+'</li>';
													});
													}
												 }
												if(subHeadingItem.hasOwnProperty('channelNames')){
												$.each(subHeadingItem.channelNames, function (l) {
												var chName = subHeadingItem.channelNames[l];
                                                    if ($(window).width()<700) {
														extraSubHeading = extraSubHeading +'<li class="col-sm-12 col-xs-6 channel visible ng-scope" >'+chName.channelName+'</li>';
                                                    } else{ 
														extraSubHeading = extraSubHeading +'<li class="col-xs-6 col-sm-4 col-md-6 channel hidden-xs bold fine-print ng-scope" >'+chName.channelName+'</li>';
                                                    }
												});
												}
                                                extraSubHeading = extraSubHeading+'</ul>';
												extraSubHeading = extraSubHeading+'</div>'; // this is row div
												extraSubHeading = extraSubHeading+'</div>'; // this is extra_box ng-scope
												extraDivItems = extraDivItems+extraSubHeading;
												extraSubHeading = "";
																					});
																	extraDivItems = extraDivItems+'</div></div>'; //first is extra box, second row ng-scope ng-hide
																	extraDiv = extraDiv+extraDivItems;
															 });
													extraDiv = extraDiv+'</div></div></div>'; //first is extra_content, second is col-md-10 col-md-offset-1, third is row light-gradient
												$('#extras').append(extraDiv);
														}
													 },
													 error: function(jqXHR, textStatus, errorThrown) {
												console.log(textStatus, errorThrown);
												}
									   });	
							}

$(document).ready(function() {

      if ($.cookie('wcmmode') == "edit") {
       // alert("Edit Mode");
    } else {
		$("#cta2").hide();
		$("#cta3").hide();
		$("#rte-int2").hide();
		$("#rte-int3").hide();
		$("#cta-int2").hide();
		$("#cta-int3").hide();
        $("#sixmonthlycarousal").hide();
        $("#yearlycarousal").hide();
    }

    if($(".fadein.slower").is(':visible')){ //if the container is visible on the page
        var array25 = $('#m25Channels').val().split(",");
        loadChannels(array25);
        callAjax("m25");
  	}
    var currentHtml = "";
	if($("#oneCol").is(':visible')) {
	currentHtml = $("#oneCol").html();
    } else if($("#twoCol").is(':visible')) {
	currentHtml = $("#twoCol").html();
    } else if($("#threeCol").is(':visible')) {
	currentHtml = $("#threeCol").html();
    }
    $("#monthAnchor").click(function() {

        $("#cta1").show();
        $("#cta2").hide();
        $("#cta3").hide();
        $("#rte-int1").show();
		$("#rte-int2").hide();
		$("#rte-int3").hide();
        $("#cta-int1").show();
		$("#cta-int2").hide();
		$("#cta-int3").hide();
		$("#monthlycarousal").show();
        $("#sixmonthlycarousal").hide();
        $("#yearlycarousal").hide();

        $("#sixMonthAnchor").removeClass("active-tab");
    	$("#yearAnchor").removeClass("active-tab");
        $(this).addClass("active-tab");
        if($("#oneCol").is(':visible')){
			$("#oneCol").html(currentHtml);
       	 } else if($("#twoCol").is(':visible')){
			$("#twoCol").html(currentHtml);
        } else if($("#threeCol").is(':visible')){
			$("#threeCol").html(currentHtml);
        }
        var array25 = $('#m25Channels').val().split(",");
	loadChannels(array25);
	callAjax("m25");
	});
    $("#sixMonthAnchor").click(function() {

        $("#cta1").hide();
        $("#cta2").show();
        $("#cta3").hide();
        $("#rte-int1").hide();
		$("#rte-int2").show();
		$("#rte-int3").hide();
        $("#cta-int1").hide();
		$("#cta-int2").show();
		$("#cta-int3").hide();
        $("#monthlycarousal").hide();
        $("#sixmonthlycarousal").show();
        $("#yearlycarousal").hide();

        	$("#monthAnchor").removeClass("active-tab");
        	$("#yearAnchor").removeClass("active-tab");
        	$(this).addClass("active-tab");
        	if($("#oneCol").is(':visible')) {
                if($("#s25ChannelTitle").val() != '' && $("#s30ChannelTitle").val() == '' && $("#s60ChannelTitle").val() == '') {
	var tab1 = "<div class='mss-packages-tab float-left ng-scope active-tab' id='rowTwoTab1' style='width: 100%;'>"+
            	"<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#s25ChannelTitle').val()+"</h1>"+
            	"<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#s25ChannelSubTitle').val()+"</h6>"+
            	"<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
            	"<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
            	"<hr class='mss-packages-hr'>"+
            	"<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='25Price'>"+$('#s25ChannelPrice').val()+"</p>"+
            	"</div>";
	$("#oneCol").html(tab1); 	
	var array25 = $('#s25Channels').val().split(",");
            	loadChannels(array25);	
            	callAjax("s25");
                } else if($("#s25ChannelTitle").val() != '' && $("#s30ChannelTitle").val() != '' && $("#s60ChannelTitle").val() == '') {
                    var tab1 = "<div class='mss-packages-tab float-left ng-scope active-tab' id='rowTwoTab1' style='width: 50%;'>"+
            	"<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#s25ChannelTitle').val()+"</h1>"+
            	"<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#s25ChannelSubTitle').val()+"</h6>"+
            	"<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
            	"<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
            	"<hr class='mss-packages-hr'>"+
            	"<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='25Price'>"+$('#s25ChannelPrice').val()+"</p>"+
            	"</div>";
	var tab2 = "<div class='mss-packages-tab float-left ng-scope' id='rowTwoTab2' style='width: 50%;'>"+
            	"<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#s30ChannelTitle').val()+"</h1>"+
            	"<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#s30ChannelSubTitle').val()+"</h6>"+
            	"<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
            	"<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
            	"<hr class='mss-packages-hr'>"+
            	"<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='30Price'>"+$('#s30ChannelPrice').val()+"</p>"+
            	"</div>";
                    $("#oneCol").html(tab1+tab2); 
                    var array25 = $('#s25Channels').val().split(",");
            	loadChannels(array25);
            	callAjax("s25");
                } else if($("#s25ChannelTitle").val() != '' && $("#s30ChannelTitle").val() != '' && $("#s60ChannelTitle").val() != '') {
                    var tab1 = "<div class='mss-packages-tab float-left ng-scope active-tab' id='rowTwoTab1' style='width: 33.33%;'>"+
                    	           "<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#s25ChannelTitle').val()+"</h1>"+
                                   "<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#s25ChannelSubTitle').val()+"</h6>"+
                                   "<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
                                   "<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
                                   "<hr class='mss-packages-hr'>"+
                                   "<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='25Price'>"+$('#s25ChannelPrice').val()+"</p>"+
                                "</div>";
                    var tab2 = "<div class='mss-packages-tab float-left ng-scope' id='rowTwoTab2' style='width: 33.33%;'>"+
                    	           "<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#s30ChannelTitle').val()+"</h1>"+
                                   "<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#s30ChannelSubTitle').val()+"</h6>"+
                                   "<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
                                   "<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
                                   "<hr class='mss-packages-hr'>"+
                                   "<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='30Price'>"+$('#s30ChannelPrice').val()+"</p>"+
                                "</div>";
                    var tab3 = "<div class='mss-packages-tab float-left ng-scope' id='rowTwoTab3' style='width: 33.33%;'>"+
                                    "<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#s60ChannelTitle').val()+"</h1>"+
                                    "<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#s60ChannelSubTitle').val()+"</h6>"+
                                    "<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
                                    "<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
                                    "<hr class='mss-packages-hr'>"+
                                    "<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='60Price'>"+$('#s60ChannelPrice').val()+"</p>"+
                                 "</div>";
                    $("#oneCol").html(tab1+tab2+tab3);
                    var array25 = $('#s25Channels').val().split(",");
            	loadChannels(array25);
            	callAjax("s25");
                } 
            } else if($("#twoCol").is(':visible')) {
            	if($("#s25ChannelTitle").val() != '' && $("#s30ChannelTitle").val() == '' && $("#s60ChannelTitle").val() == '') {
            	var tab1 = "<div class='mss-packages-tab float-left ng-scope active-tab' id='rowTwoTab2' style='width: 100%;'>"+
                    	"<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#s25ChannelTitle').val()+"</h1>"+
                    	"<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#s25ChannelSubTitle').val()+"</h6>"+
                    	"<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
                    	"<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
                    	"<hr class='mss-packages-hr'>"+
                    	"<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='25Price'>"+$('#s25ChannelPrice').val()+"</p>"+
                    	"</div>";
            	$("#twoCol").html(tab1);
            	var array25 = $('#s25Channels').val().split(",");
            	loadChannels(array25);
            	callAjax("s25");
                } else if($("#s25ChannelTitle").val() != '' && $("#s30ChannelTitle").val() != '' && $("#s60ChannelTitle").val() == '') {
	var tab1 = "<div class='mss-packages-tab float-left ng-scope active-tab' id='rowTwoTab1' style='width: 50%;'>"+
	"<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#s25ChannelTitle').val()+"</h1>"+
	"<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#s25ChannelSubTitle').val()+"</h6>"+
	"<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
	"<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
	"<hr class='mss-packages-hr'>"+
	"<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='25Price'>"+$('#s25ChannelPrice').val()+"</p>"+
	"</div>";
                	var tab2 = "<div class='mss-packages-tab float-left ng-scope' id='rowTwoTab2' style='width: 50%;'>"+
	"<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#s30ChannelTitle').val()+"</h1>"+
	"<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#s30ChannelSubTitle').val()+"</h6>"+
	"<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
	"<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
	"<hr class='mss-packages-hr'>"+
	"<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='30Price'>"+$('#s30ChannelPrice').val()+"</p>"+
	"</div>";
                	$("#twoCol").html(tab1+tab2);
	var array25 = $('#s25Channels').val().split(",");
            	loadChannels(array25);
            	callAjax("s25");
                } else if($("#s25ChannelTitle").val() != '' && $("#s30ChannelTitle").val() != '' && $("#s60ChannelTitle").val() != '') {
                	var tab1 = "<div class='mss-packages-tab float-left ng-scope active-tab' id='rowTwoTab1' style='width: 33.33%;'>"+
     	           	"<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#s25ChannelTitle').val()+"</h1>"+
     	           	"<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#s25ChannelSubTitle').val()+"</h6>"+
     	           	"<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
     	           	"<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
     	           	"<hr class='mss-packages-hr'>"+
     	           	"<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='25Price'>"+$('#s25ChannelPrice').val()+"</p>"+
     	           	"</div>";
                	var tab2 = "<div class='mss-packages-tab float-left ng-scope' id='rowTwoTab2' style='width: 33.33%;'>"+
     	           	"<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#s30ChannelTitle').val()+"</h1>"+
     	           	"<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#s30ChannelSubTitle').val()+"</h6>"+
     	           	"<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
     	           	"<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
     	           	"<hr class='mss-packages-hr'>"+
     	           	"<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='30Price'>"+$('#s30ChannelPrice').val()+"</p>"+
     	           	"</div>";
                	var tab3 = "<div class='mss-packages-tab float-left ng-scope' id='rowTwoTab3' style='width: 33.33%;'>"+
                	"<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#s60ChannelTitle').val()+"</h1>"+
                	"<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#s60ChannelSubTitle').val()+"</h6>"+
                	"<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
                	"<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
                	"<hr class='mss-packages-hr'>"+
                	"<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='60Price'>"+$('#s60ChannelPrice').val()+"</p>"+
                	"</div>";
                	$("#twoCol").html(tab1+tab2+tab3);
                	var array25 = $('#s25Channels').val().split(",");
            	loadChannels(array25);
            	callAjax("s25");
                }	
            } else if($("#threeCol").is(':visible')) {
            	if($("#s25ChannelTitle").val() != '' && $("#s30ChannelTitle").val() == '' && $("#s60ChannelTitle").val() == '') {
            	var tab1 = "<div class='mss-packages-tab float-left ng-scope active-tab' id='rowTwoTab1' style='width: 100%;'>"+
    	"<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#s25ChannelTitle').val()+"</h1>"+
    	"<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#s25ChannelSubTitle').val()+"</h6>"+
    	"<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
    	"<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
    	"<hr class='mss-packages-hr'>"+
    	"<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='25Price'>"+$('#s25ChannelPrice').val()+"</p>"+
    	"</div>";
            	$("#threeCol").html(tab1);
            	var array25 = $('#s25Channels').val().split(",");
            	loadChannels(array25);
            	callAjax("s25");
                } else if($("#s25ChannelTitle").val() != '' && $("#s30ChannelTitle").val() != '' && $("#s60ChannelTitle").val() == '') {
                	var tab1 = "<div class='mss-packages-tab float-left ng-scope active-tab' id='rowTwoTab1' style='width: 50%;'>"+
	"<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#s25ChannelTitle').val()+"</h1>"+
	"<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#s25ChannelSubTitle').val()+"</h6>"+
	"<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
	"<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
	"<hr class='mss-packages-hr'>"+
	"<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='25Price'>"+$('#s25ChannelPrice').val()+"</p>"+
	"</div>";
                	var tab2 = "<div class='mss-packages-tab float-left ng-scope' id='rowTwoTab2' style='width: 50%;'>"+
	"<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#s30ChannelTitle').val()+"</h1>"+
	"<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#s30ChannelSubTitle').val()+"</h6>"+
	"<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
	"<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
	"<hr class='mss-packages-hr'>"+
	"<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='30Price'>"+$('#s30ChannelPrice').val()+"</p>"+
	"</div>";
                	$("#threeCol").html(tab1+tab2);
                	var array25 = $('#s25Channels').val().split(",");
            	loadChannels(array25);
            	callAjax("s25");
                } else if($("#s25ChannelTitle").val() != '' && $("#s30ChannelTitle").val() != '' && $("#s60ChannelTitle").val() != '') {
                	var tab1 = "<div class='mss-packages-tab float-left ng-scope active-tab' id='rowTwoTab1' style='width: 33.33%;'>"+
	    	"<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#s25ChannelTitle').val()+"</h1>"+
	    	"<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#s25ChannelSubTitle').val()+"</h6>"+
	    	"<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
	    	"<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
	    	"<hr class='mss-packages-hr'>"+
	    	"<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='25Price'>"+$('#s25ChannelPrice').val()+"</p>"+
	    	"</div>";
	var tab2 = "<div class='mss-packages-tab float-left ng-scope' id='rowTwoTab2' style='width: 33.33%;'>"+
	    	"<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#s30ChannelTitle').val()+"</h1>"+
	    	"<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#s30ChannelSubTitle').val()+"</h6>"+
	    	"<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
	    	"<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
	    	"<hr class='mss-packages-hr'>"+
	    	"<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='30Price'>"+$('#s30ChannelPrice').val()+"</p>"+
	    	"</div>";
	var tab3 = "<div class='mss-packages-tab float-left ng-scope' id='rowTwoTab3' style='width: 33.33%;'>"+
	"<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#s60ChannelTitle').val()+"</h1>"+
	"<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#s60ChannelSubTitle').val()+"</h6>"+
	"<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
	"<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
	"<hr class='mss-packages-hr'>"+
	"<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='60Price'>"+$('#s60ChannelPrice').val()+"</p>"+
	"</div>";
	$("#threeCol").html(tab1+tab2+tab3);
	var array25 = $('#s25Channels').val().split(",");
            	loadChannels(array25);
            	callAjax("s25");
            	}
        	}
	});
    
    $("#yearAnchor").click(function() {

        $("#cta1").hide();
        $("#cta2").hide();
        $("#cta3").show();
 		$("#rte-int1").hide();
		$("#rte-int2").hide();
		$("#rte-int3").show();
        $("#cta-int1").hide();
		$("#cta-int2").hide();
		$("#cta-int3").show();
        $("#monthlycarousal").hide();
        $("#sixmonthlycarousal").hide();
        $("#yearlycarousal").show();

    	$("#monthAnchor").removeClass("active-tab");
    	$("#sixMonthAnchor").removeClass("active-tab");
    	$(this).addClass("active-tab");
    	if($("#oneCol").is(':visible')) {
            if($("#y25ChannelTitle").val() != '' && $("#y30ChannelTitle").val() == '' && $("#y60ChannelTitle").val() == '') {
	var tab1 = "<div class='mss-packages-tab float-left ng-scope active-tab' id='rowTwoTab1' style='width: 100%;'>"+
        	"<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#y25ChannelTitle').val()+"</h1>"+
        	"<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#y25ChannelSubTitle').val()+"</h6>"+
        	"<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
        	"<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
        	"<hr class='mss-packages-hr'>"+
        	"<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='25Price'>"+$('#y25ChannelPrice').val()+"</p>"+
        	"</div>";
	$("#oneCol").html(tab1);	
	var array25 = $('#y25Channels').val().split(",");
        	loadChannels(array25);
        	callAjax("y25");
            } else if($("#y25ChannelTitle").val() != '' && $("#y30ChannelTitle").val() != '' && $("#y60ChannelTitle").val() == '') {
                var tab1 = "<div class='mss-packages-tab float-left ng-scope active-tab' id='rowTwoTab1' style='width: 50%;'>"+
        	"<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#y25ChannelTitle').val()+"</h1>"+
        	"<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#y25ChannelSubTitle').val()+"</h6>"+
        	"<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
        	"<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
        	"<hr class='mss-packages-hr'>"+
        	"<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='25Price'>"+$('#y25ChannelPrice').val()+"</p>"+
        	"</div>";
	var tab2 = "<div class='mss-packages-tab float-left ng-scope' id='rowTwoTab2' style='width: 50%;'>"+
        	"<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#y30ChannelTitle').val()+"</h1>"+
        	"<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#y30ChannelSubTitle').val()+"</h6>"+
        	"<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
        	"<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
        	"<hr class='mss-packages-hr'>"+
        	"<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='30Price'>"+$('#y30ChannelPrice').val()+"</p>"+
        	"</div>";
                $("#oneCol").html(tab1+tab2); 
                var array25 = $('#y25Channels').val().split(",");
        	loadChannels(array25);
        	callAjax("y25");
            } else if($("#y25ChannelTitle").val() != '' && $("#y30ChannelTitle").val() != '' && $("#y60ChannelTitle").val() != '') {
                var tab1 = "<div class='mss-packages-tab float-left ng-scope active-tab' id='rowTwoTab1' style='width: 33.33%;'>"+
                	           "<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#y25ChannelTitle').val()+"</h1>"+
                               "<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#y25ChannelSubTitle').val()+"</h6>"+
                               "<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
                               "<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
                               "<hr class='mss-packages-hr'>"+
                               "<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='25Price'>"+$('#y25ChannelPrice').val()+"</p>"+
                            "</div>";
                var tab2 = "<div class='mss-packages-tab float-left ng-scope' id='rowTwoTab2' style='width: 33.33%;'>"+
                	           "<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#y30ChannelTitle').val()+"</h1>"+
                               "<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#y30ChannelSubTitle').val()+"</h6>"+
                               "<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
                               "<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
                               "<hr class='mss-packages-hr'>"+
                               "<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='30Price'>"+$('#y30ChannelPrice').val()+"</p>"+
                            "</div>";
                var tab3 = "<div class='mss-packages-tab float-left ng-scope' id='rowTwoTab3' style='width: 33.33%;'>"+
                                "<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#y60ChannelTitle').val()+"</h1>"+
                                "<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#y60ChannelSubTitle').val()+"</h6>"+
                                "<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
                                "<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
                                "<hr class='mss-packages-hr'>"+
                                "<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='60Price'>"+$('#y60ChannelPrice').val()+"</p>"+
                             "</div>";
                $("#oneCol").html(tab1+tab2+tab3); 
                var array25 = $('#y25Channels').val().split(",");
        	loadChannels(array25);
        	callAjax("y25");
            } 
        } else if($("#twoCol").is(':visible')) {
        	if($("#y25ChannelTitle").val() != '' && $("#y30ChannelTitle").val() == '' && $("#y60ChannelTitle").val() == '') {
        	var tab1 = "<div class='mss-packages-tab float-left ng-scope active-tab' id='rowTwoTab2' style='width: 100%;'>"+
                	"<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#y25ChannelTitle').val()+"</h1>"+
                	"<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#y25ChannelSubTitle').val()+"</h6>"+
                	"<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
                	"<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
                	"<hr class='mss-packages-hr'>"+
                	"<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='25Price'>"+$('#y25ChannelPrice').val()+"</p>"+
                	"</div>";
        	$("#twoCol").html(tab1);
        	var array25= $('#y25Channels').val().split(",");
        	loadChannels(array25);
        	callAjax("y25");
            } else if($("#y25ChannelTitle").val() != '' && $("#y30ChannelTitle").val() != '' && $("#y60ChannelTitle").val() == '') {
	 var tab1 = "<div class='mss-packages-tab float-left ng-scope active-tab' id='rowTwoTab1' style='width: 50%;'>"+
        	"<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#y25ChannelTitle').val()+"</h1>"+
        	"<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#y25ChannelSubTitle').val()+"</h6>"+
        	"<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
        	"<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
        	"<hr class='mss-packages-hr'>"+
        	"<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='25Price'>"+$('#y25ChannelPrice').val()+"</p>"+
        	"</div>";
	var tab2 = "<div class='mss-packages-tab float-left ng-scope' id='rowTwoTab2' style='width: 50%;'>"+
        	"<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#y30ChannelTitle').val()+"</h1>"+
        	"<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#y30ChannelSubTitle').val()+"</h6>"+
        	"<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
        	"<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
        	"<hr class='mss-packages-hr'>"+
        	"<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='30Price'>"+$('#y30ChannelPrice').val()+"</p>"+
        	"</div>";
                $("#twoCol").html(tab1+tab2); 
			var array25 = $('#y25Channels').val().split(",");
        	loadChannels(array25);
        	callAjax("y25");
            } else if($("#y25ChannelTitle").val() != '' && $("#y30ChannelTitle").val() != '' && $("#y60ChannelTitle").val() != '') {
            	var tab1 = "<div class='mss-packages-tab float-left ng-scope active-tab' id='rowTwoTab1' style='width: 33.33%;'>"+
 	           	"<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#y25ChannelTitle').val()+"</h1>"+
 	           	"<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#y25ChannelSubTitle').val()+"</h6>"+
 	           	"<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
 	           	"<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
 	           	"<hr class='mss-packages-hr'>"+
 	           	"<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='25Price'>"+$('#y25ChannelPrice').val()+"</p>"+
 	           	"</div>";
            	var tab2 = "<div class='mss-packages-tab float-left ng-scope' id='rowTwoTab2' style='width: 33.33%;'>"+
 	           	"<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#y30ChannelTitle').val()+"</h1>"+
 	           	"<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#y30ChannelSubTitle').val()+"</h6>"+
 	           	"<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
 	           	"<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
 	           	"<hr class='mss-packages-hr'>"+
 	           	"<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='30Price'>"+$('#y30ChannelPrice').val()+"</p>"+
 	           	"</div>";
            	var tab3 = "<div class='mss-packages-tab float-left ng-scope' id='rowTwoTab3' style='width: 33.33%;'>"+
            	"<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#y60ChannelTitle').val()+"</h1>"+
            	"<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#y60ChannelSubTitle').val()+"</h6>"+
            	"<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
            	"<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
            	"<hr class='mss-packages-hr'>"+
            	"<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='60Price'>"+$('#y60ChannelPrice').val()+"</p>"+
            	"</div>";
            	$("#twoCol").html(tab1+tab2+tab3);
            	var array25 = $('#y25Channels').val().split(",");
        	loadChannels(array25);
        	callAjax("y25");
            }	
        } else if($("#threeCol").is(':visible')) {
        	if($("#y25ChannelTitle").val() != '' && $("#y30ChannelTitle").val() == '' && $("#y60ChannelTitle").val() == '') {
        	var tab1 = "<div class='mss-packages-tab float-left ng-scope active-tab' id='rowTwoTab1' style='width: 100%;'>"+
	"<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#y25ChannelTitle').val()+"</h1>"+
	"<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#y25ChannelSubTitle').val()+"</h6>"+
	"<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
	"<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
	"<hr class='mss-packages-hr'>"+
	"<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='25Price'>"+$('#y25ChannelPrice').val()+"</p>"+
	"</div>";
        	$("#threeCol").html(tab1);
        	var array25 = $('#y25Channels').val().split(",");
        	loadChannels(array25);
        	callAjax("y25");
            } else if($("#y25ChannelTitle").val() != '' && $("#y30ChannelTitle").val() != '' && $("#y60ChannelTitle").val() == '') {
            	var tab1 = "<div class='mss-packages-tab float-left ng-scope active-tab' id='rowTwoTab1' style='width: 50%;'>"+
	"<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#y25ChannelTitle').val()+"</h1>"+
	"<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#y25ChannelSubTitle').val()+"</h6>"+
	"<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
	"<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
	"<hr class='mss-packages-hr'>"+
	"<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='25Price'>"+$('#y25ChannelPrice').val()+"</p>"+
	"</div>";
            	var tab2 = "<div class='mss-packages-tab float-left ng-scope' id='rowTwoTab2' style='width: 50%;'>"+
	"<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#y30ChannelTitle').val()+"</h1>"+
	"<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#y30ChannelSubTitle').val()+"</h6>"+
	"<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
	"<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
	"<hr class='mss-packages-hr'>"+
	"<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='30Price'>"+$('#y30ChannelPrice').val()+"</p>"+
	"</div>";
            	$("#threeCol").html(tab1+tab2);
            	var array25 = $('#y25Channels').val().split(",");
        	loadChannels(array25);
        	callAjax("y25");
            } else if($("#y25ChannelTitle").val() != '' && $("#y30ChannelTitle").val() != '' && $("#y60ChannelTitle").val() != '') {
	var tab1 = "<div class='mss-packages-tab float-left ng-scope active-tab' id='rowTwoTab1' style='width: 33.33%;'>"+
 	           	"<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#y25ChannelTitle').val()+"</h1>"+
 	           	"<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#y25ChannelSubTitle').val()+"</h6>"+
 	           	"<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
 	           	"<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
 	           	"<hr class='mss-packages-hr'>"+
 	           	"<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='25Price'>"+$('#y25ChannelPrice').val()+"</p>"+
 	           	"</div>";
            	var tab2 = "<div class='mss-packages-tab float-left ng-scope' id='rowTwoTab2' style='width: 33.33%;'>"+
 	           	"<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#y30ChannelTitle').val()+"</h1>"+
 	           	"<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#y30ChannelSubTitle').val()+"</h6>"+
 	           	"<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
 	           	"<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
 	           	"<hr class='mss-packages-hr'>"+
 	           	"<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='30Price'>"+$('#y30ChannelPrice').val()+"</p>"+
 	           	"</div>";
            	var tab3 = "<div class='mss-packages-tab float-left ng-scope' id='rowTwoTab3' style='width: 33.33%;'>"+
            	"<h1 class='text-uppercase h-tag-spacing ng-binding'>"+$('#y60ChannelTitle').val()+"</h1>"+
            	"<h6 class='text-uppercase h-tag-spacing channel-bottom-margin ng-binding'>"+$('#y60ChannelSubTitle').val()+"</h6>"+
            	"<p class='fine-print extra-bold p-tag-spacing hidden-xs ng-binding'></p>"+
            	"<p class='fine-print extra-bold p-tag-spacing visible-xs ng-binding'></p>"+
            	"<hr class='mss-packages-hr'>"+
            	"<p class='fine-print extra-bold text-uppercase p-tag-spacing ng-binding' id='60Price'>"+$('#y60ChannelPrice').val()+"</p>"+
            	"</div>";
            	$("#threeCol").html(tab1+tab2+tab3);
	var array25 = $('#y25Channels').val().split(",");
        	loadChannels(array25);
        	callAjax("y25");
        	}
    	}
    });

	$('#extras').on('click', '#plusminus', function() {
        if($(this).hasClass('fa-plus')){
        	$(this).removeClass('fa-plus');
        	$(this).addClass('fa-minus');
        	$('#extra_content').show();
        }else{
        	$(this).removeClass('fa-minus');
        	$(this).addClass('fa-plus');
        	$('#extra_content').hide();
        }
    });

	$('#oneCol').on('click', '#rowTwoTab1', function() {
        $('#rowTwoTab2').removeClass('active-tab');
        $('#rowTwoTab3').removeClass('active-tab');
        $('#rowTwoTab1').addClass('active-tab');
	var array25;
	var extraId;
        if($('#monthAnchor').hasClass('active-tab')) {
	array25 = $('#m25Channels').val().split(",");
	extraId = "m25";
        } else if($('#sixMonthAnchor').hasClass('active-tab')) {
	array25 = $('#s25Channels').val().split(",");
	extraId = "s25";
        } else if($('#yearAnchor').hasClass('active-tab')) {
	array25 = $('#y25Channels').val().split(",");
	extraId = "y25";
        }
        loadChannels(array25);
        callAjax(extraId);
	});

	$('#oneCol').on('click', '#rowTwoTab2', function() {
	$('#rowTwoTab1').removeClass('active-tab');
        $('#rowTwoTab3').removeClass('active-tab');
        $('#rowTwoTab2').addClass('active-tab');
        var array30;
        var extraId;
        if($('#monthAnchor').hasClass('active-tab')) {
        	array30 = $('#m30Channels').val().split(",");
        	extraId = "m30";
        } else if($('#sixMonthAnchor').hasClass('active-tab')) {
        	array30 = $('#s30Channels').val().split(",");
        	extraId = "s30";
        } else if($('#yearAnchor').hasClass('active-tab')) {
        	array30 = $('#y30Channels').val().split(",");
        	extraId = "y30";
        }
        loadChannels(array30);
        callAjax(extraId);
	});

	$('#oneCol').on('click', '#rowTwoTab3', function() {
        $('#rowTwoTab1').removeClass('active-tab');
        $('#rowTwoTab2').removeClass('active-tab');
        $('#rowTwoTab3').addClass('active-tab');
        var array60;
        var extraId;
	if($('#monthAnchor').hasClass('active-tab')) {
        	array60 = $('#m60Channels').val().split(",");
        	extraId = "m60";
        } else if($('#sixMonthAnchor').hasClass('active-tab')) {
        	array60 = $('#s60Channels').val().split(",");
        	extraId = "s60";
        } else if($('#yearAnchor').hasClass('active-tab')) {
        	array60 = $('#y60Channels').val().split(",");
        	extraId = "y60";
        }
        loadChannels(array60);
        callAjax(extraId);
	});


	$('#twoCol').on('click', '#rowTwoTab1', function() {
        $('#rowTwoTab2').removeClass('active-tab');
        $('#rowTwoTab3').removeClass('active-tab');
        $('#rowTwoTab1').addClass('active-tab');
        var array25;
        var extraId;
	if($('#monthAnchor').hasClass('active-tab')) {
	array25 = $('#m25Channels').val().split(",");
	extraId = "m25";
        } else if($('#sixMonthAnchor').hasClass('active-tab')) {
	array25 = $('#s25Channels').val().split(",");
	extraId = "s25";
        } else if($('#yearAnchor').hasClass('active-tab')) {
	array25 = $('#y25Channels').val().split(",");
	extraId = "y25";
        }
        loadChannels(array25);
        callAjax(extraId);
    });

	$('#twoCol').on('click', '#rowTwoTab2', function() {
        $('#rowTwoTab1').removeClass('active-tab');
        $('#rowTwoTab3').removeClass('active-tab');
        $('#rowTwoTab2').addClass('active-tab');
        var array30;
        var extraId;
	if($('#monthAnchor').hasClass('active-tab')) {
        	array30 = $('#m30Channels').val().split(",");
        	extraId = "m30";
        } else if($('#sixMonthAnchor').hasClass('active-tab')) {
        	array30 = $('#s30Channels').val().split(",");
        	extraId = "s30";
        } else if($('#yearAnchor').hasClass('active-tab')) {
        	array30 = $('#y30Channels').val().split(",");
        	extraId = "y30";
        }
        loadChannels(array30);
        callAjax(extraId);
	});

	$('#twoCol').on('click', '#rowTwoTab3', function() {
        $('#rowTwoTab1').removeClass('active-tab');
        $('#rowTwoTab2').removeClass('active-tab');
        $('#rowTwoTab3').addClass('active-tab');
        var array60;
        var extraId;
	if($('#monthAnchor').hasClass('active-tab')) {
        	array60 = $('#m60Channels').val().split(",");
        	extraId = "m60";
        } else if($('#sixMonthAnchor').hasClass('active-tab')) {
        	array60 = $('#s60Channels').val().split(",");
        	extraId = "s60";
        } else if($('#yearAnchor').hasClass('active-tab')) {
        	array60 = $('#y60Channels').val().split(",");
        	extraId = "y60";
        }
	loadChannels(array60);
	callAjax(extraId);
	});

    $('#threeCol').on('click', '#rowTwoTab1', function() {
        $('#rowTwoTab2').removeClass('active-tab');
        $('#rowTwoTab3').removeClass('active-tab');
        $('#rowTwoTab1').addClass('active-tab');
        var array25;
        var extraId;
	if($('#monthAnchor').hasClass('active-tab')) {
	array25 = $('#m25Channels').val().split(",");
	extraId = "m25";
        } else if($('#sixMonthAnchor').hasClass('active-tab')) {
	array25 = $('#s25Channels').val().split(",");
	extraId = "s25";
        } else if($('#yearAnchor').hasClass('active-tab')) {
	array25 = $('#y25Channels').val().split(",");
	extraId = "y25";
        }
        loadChannels(array25);
        callAjax(extraId);
	});

	$('#threeCol').on('click', '#rowTwoTab2', function() {
        $('#rowTwoTab1').removeClass('active-tab');
        $('#rowTwoTab3').removeClass('active-tab');
        $('#rowTwoTab2').addClass('active-tab');
        var array30;
        var extraId;
        if($('#monthAnchor').hasClass('active-tab')) {
        	array30 = $('#m30Channels').val().split(",");
        	extraId = "m30";
        } else if($('#sixMonthAnchor').hasClass('active-tab')) {
        	array30 = $('#s30Channels').val().split(",");
        	extraId = "s30";
        } else if($('#yearAnchor').hasClass('active-tab')) {
        	array30 = $('#y30Channels').val().split(",");
        	extraId = "y30";
        }
        loadChannels(array30);
        callAjax(extraId);
	});

	$('#threeCol').on('click', '#rowTwoTab3', function() {
        $('#rowTwoTab1').removeClass('active-tab');
        $('#rowTwoTab2').removeClass('active-tab');
        $('#rowTwoTab3').addClass('active-tab');
        var array60;
        var extraId;
        if($('#monthAnchor').hasClass('active-tab')) {
        	array60 = $('#m60Channels').val().split(",");
        	extraId = "m60";
        } else if($('#sixMonthAnchor').hasClass('active-tab')) {
        	array60 = $('#s60Channels').val().split(",");
        	extraId = "s60";
        } else if($('#yearAnchor').hasClass('active-tab')) {
        	array60 = $('#y60Channels').val().split(",");
        	extraId = "y60";
        }
	loadChannels(array60);
	callAjax(extraId);
	});
});

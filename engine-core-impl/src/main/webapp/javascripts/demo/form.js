$().ready(function(){

	var itemId = $.getQueryParam("worklistitemId");
	var participantId = $.Storage.get("participantUUID");

	$.ajax({
        type: 'GET',
        url: '/api/worklist/items/' + itemId + '/form?participantId=' + participantId,
        success: function(data) {
        	$("#formContent").append(data);
        	console.log(window.location.pathname + window.location.search);
        	$("div form").attr("action","/api/worklist/items/" + itemId + "/form/?participantId=" + participantId);
        }
	});





});


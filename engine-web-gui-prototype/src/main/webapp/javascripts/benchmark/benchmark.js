// This is the benchmark javascript which will simulate a participant

/*****************************************************
*													 *
*													 *
*					Function land					 *
*													 *
*		Here some general functions are provided	 *
*													 *
******************************************************/

// constants
JODA_ENGINE_ADRESS = '';
PARTICIPANTS_PER_ROLE = 5;
NUMBER_OF_INSTANCES = 1;

ITEM_STATE = {
    "offered" : "OFFERED",
    "allocated" : "ALLOCATED",
    "executing" : "EXECUTING",
    "completed" : "COMPLETED"
}


// globals
// holds the UUId of the currently logged in participant
participantUUID = undefined;

/*
 * GET Land
 */

// gets all the available roles and returns them
function getRoles(func) {
	$.ajax({
        type: 'GET',
        url: JODA_ENGINE_ADRESS + '/api/identity/roles',
        success: function(data) {
            func(data);
        },
        dataType: "json"
    });
}

// get all the participants and return them
function getParticipants(func) {
	$.ajax({
        type: 'GET',
        url: JODA_ENGINE_ADRESS + '/api/identity/participants',
        success: function(data) {
        	func(data);
        },
        dataType: "json"
    });
}

// get all the deployed process definitions and return them
function getProcessDefinitions(func) {
	$.ajax({
        type: 'GET',
        url: JODA_ENGINE_ADRESS + '/api/repository/process-definitions',
        success: function(data) {
        	func(data);
        },
        dataType: "json"
    });
}

// get all the running instances and return them and return them
function getRunningProcessInstances(func) {
	$.ajax({
        type: 'GET',
        url: JODA_ENGINE_ADRESS + '/api/navigator/status/running-instances',
        success: function(data) {
        	func(data);
        },
        dataType: "json"
    });
}

/*
 *  General Utility helpers
 */

// return a random element from the collection
function getRandomElementFrom(collection) {

     var itemNumber = Math.floor(Math.random() * collection.length);
     return collection[itemNumber];
}

/*
 *	Participant creation helpers!
 */

function generateParticipantname() {
	time = new Date().getTime();
	return "participant: " + time;
}

// create participant with the role
function createParticipantWithRole(roleId) {
	$.ajax({
		type: 'POST',
		url: JODA_ENGINE_ADRESS + '/api/identity/roles/' + roleId + '/participants',
		data: generateParticipantname()
	});
}

// Creates some participants that will be used by the benchmark users
function createParticipantsFromRoles(roles) {
    $.each(roles, function(i, role) {
        for (var i = 0; i < PARTICIPANTS_PER_ROLE; i++) {
            createParticipantWithRole(role.id);
        }
    });
}


/*
 * Process creation helpers
 */

// start a specific number of process instances for each definition
function startProcessInstancesFromDefinitions(definitions) {
    $.each(definitions, function(i, definition) {
        for (var i = 0; i < NUMBER_OF_INSTANCES; i++) {
            startProcessInstance(definition);
        }
    });
}

// starts a process instance of the given definition
function startProcessInstance(definition) {
    $.ajax({
		type: 'POST',
		url: JODA_ENGINE_ADRESS + '/api/navigator/process-definitions/' + definition.id + '/start'
	});
}

/*
 *  Worklist helpers (many!)
 */



// get all offered worklist items and start to work on a random one
function getOfferedWorklistItems() {
    $.ajax({
        type: 'GET',
        url: '/api/worklist/items?id=' + participantUUID + '&itemState=' + ITEM_STATE.offered,
        success: function(data) {
            var worklistItems = data;
            if !($.isEmptyObject(worklistItems)) {
                beginRandomWorklistItem(worklistItems);
            }
        },
        dataType: "json" // we expect json
    });
}

// get all executing worklist items and end some of them
function getExecutingWorklistItems() {
    $.ajax({
        type: 'GET',
        url: '/api/worklist/items?id=' + participantUUID + '&itemState=' + ITEM_STATE.executing,
        success: function(data) {
            var worklistItems = data;
            if !($.isEmptyObject(worklistItems)) {
                endRandomWorklistItem(worklistItems);
            }
        },
        dataType: "json" // we expect json
    });
}

// change the item state of the item to a given state
function changeItemState(itemId, state) {

    $.ajax({
	    	type: 'PUT',
	    	url: '/api/worklist/items/' + itemId + '/state?participantId=' + participantUUID,
	    	data: state,
	    	success: function(data) {
	    	    // TODO log succcesfull answers?!
	    	}
	});
}

// Begins the work on a random worklist item (it's a callback of the GetWorklistItems function)
function beginRandomWorklistItem (worklistitems) {
    var item = getRandomElementFrom(worklistItems);
    changeItemState(item.id, ITEM_STATE.executing);
}

function endRandomWorklistItem (worklistItems) {
    var item = getRandomElementFrom(worklistItems);
    changeItemState(item.d, ITEM_STATE.completed);
}

// log in as a random participant
function logInAsRandomParticipant(participantList) {

    var participant = getRandomElementFrom(participantList)
    // we are well aware that this is a global variable (as we are logged in and want to use it elsewhere)
    participantUUID = participant.id;
}

/**********************************
 *                                *
 *      DSL functions             *
 *                                *
 **********************************/

// start instances of the deployed process definitions
function startProcessInstances() {
    getProcessDefinitions(startProcessInstancesFromDefinitions);
}

// creates participants and assigns them to the roles
function createParticipants() {
    getRoles(createParticipantsFromRoles);
}

function logMeIn() {
    getParticipants(logInAsRandomParticipant);
}

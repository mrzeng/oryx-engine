//
// This script depends on
//   1) navigator-crud.js
//   3) jquery.timers-1.2.js (optional)
//

$().ready(function() {
    // 
    // refresh the running instances table
    // 
    loadRunningProcessInstancesOverview();
    
    //
    // enable continues refresh, if available
    //
    if ($(document).everyTime) {
        $(document).everyTime(2500, function(i) {
            loadRunningProcessInstancesOverview();
        }, 0); // 0 = unbound
    }
    
    //
    // register the refresh overview handler
    //
    $('#running-instances-overview-refresh').click(function(event) {
        event.preventDefault();
        loadRunningProcessInstancesOverview();
    });
});


/**
 * Loads the running process instances table and clear any old entries.
 */
function loadRunningProcessInstancesOverview() {
    var tableBody = $('table#running-instances-overview tbody');
    
    if (tableBody.length != 0) {
        loadRunningProcessInstances(function(instances) {
            tableBody.empty();
            $(instances).each(function(index, instance) {
                var definition = instance.definition;
                tableBody.append(
                    '<tr instance-id="' + instance.id + '" definition-id="' + definition.id + '" class="instance clickable">'
                        + '<td>' + instance.id + '</td>'
                        + '<td>' + definition.name + '</td>'
                        + '<td>' + definition.description + '</td>'
                    + '</tr>'
                );
                
                var tokenTable = '<tr style="display: none;" id="' + instance.id + '-tokenTable">'
                                + '<td colspan="3">'
                                    + '<div style="margin-left: 30px;">'
                                        + '<table width="100%">'
                                            + '<tr><th>Token UUID</th><th>State</th><th>Activity</th><th>Node</th></tr>';
                
                // Add for each process instance a table which contains all available tokens.
                $.each(instance.assignedTokens, function(n, token) {
                    tokenTable += '<tr token-id="' + token.id + '">'
                                    + '<td>' + token.id + '</td>'
                                    + '<td>' + (token.currentActivityState ? token.currentActivityState : 'None') + '</td>'
                                    // TODO currentActivity is no longer available
                                    + '<td>' + (token.currentActivity ? token.currentActivity['@classifier'] : 'None') + '</td>'
                                    + '<td>' + token.currentNode.id + '</td>'
                                + '</tr>';
                });
                tokenTable += '</div></table></td></tr>'
                
                var instanceRow = $('tr[instance-id=' + instance.id + ']', tableBody);
                instanceRow.after(tokenTable);
                instanceRow.click(function() {
                    $('#' + instance.id + '-tokenTable', tableBody).toggle();
                });
            });
            tableBody.parent().find('th.loading-data').removeClass('loading-data');
        });
    }
};

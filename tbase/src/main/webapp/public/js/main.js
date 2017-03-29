/*********************************************************************************/
/**		AJAX RELATED METHODS													 */
/*********************************************************************************/ 
/*
 * Delays the display of the ajax loading indicator for 1 second to prevent showing on short running requests
 */
var loadingTimeout;

/*
 * Used to display a model loading indicator during AJAX requests
 */
$(document).ajaxSend(function () {
	loadingTimeout = setTimeout(function() {
		$("body").addClass("loading");
	}, 500);
});

/*
 * Used to hide the model loading indicator after an AJAX request completes
 */
$(document).ajaxComplete(function () {
	clearTimeout(loadingTimeout);
	
	$("body").removeClass("loading");
});

/*********************************************************************************/
/**		FORM/INPUT RELATED METHODS												 */
/*********************************************************************************/

/*
 * groupVal: Get the selected value from a radio button group.  Returns empty string if no value is selected.
 * 
 * Usage: 
 * 	$("input[name='myradiobuttongroupname']").groupVal();
 */
jQuery.fn.extend({
    groupVal: function() {
        var selectedRB = $(this).filter(':checked');
        
        var rbValStr = "";
        
        if(selectedRB) {
        	rbValStr = selectedRB.val();
        }
        
        return rbValStr;
    }
});

/*********************************************************************************/
/**		RESTful Web Services													 */
/*********************************************************************************/

/*********************************************************************************/
/**		General Functions   													 */
/*********************************************************************************/
function isAlpha(str)  {  
	var letters = /^[a-zA-Z]+$/;  
 
	if(str.match(letters)) {
		return true;
	}
	else {
		return false;
	}
}

/*********************************************************************************/
/**		Responsive Table fix for DropDowns										 */
/*********************************************************************************/
$(document).ready(function() {
	jQuery.fn.hasHScrollBar = function(){
		  return this.get(0).scrollWidth > this.innerWidth();
		}

		$('.table-responsive .dropdown').on('show.bs.dropdown', function (e) {
		  var $table = $(this).closest('.table-responsive');
		  if(!$table.hasHScrollBar()){
		    $('.table-responsive').css("overflow", "visible");
		  }
		});

		$('.table-responsive .dropdown').on('shown.bs.dropdown', function (e) {
		  var $table = $(this).closest('.table-responsive');

		  if($table.hasHScrollBar()){
		    var $menu = $(this).find('.dropdown-menu'),
		    tableOffsetHeight = $table.offset().top + $table.height(),
		    menuOffsetHeight = $menu.offset().top + $menu.outerHeight(true);

		    if (menuOffsetHeight > tableOffsetHeight)
		      $table.css("padding-bottom", menuOffsetHeight - tableOffsetHeight + 15);
		  }

		});

		$('.table-responsive .dropdown').on('hide.bs.dropdown', function () {
		  $(this).closest('.table-responsive').css({"padding-bottom":"", "overflow":""});
		})
}); 
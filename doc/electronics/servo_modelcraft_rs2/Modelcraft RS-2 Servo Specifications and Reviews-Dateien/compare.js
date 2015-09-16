function compare_count() {
	if(get_compare_cookie() != false) {
		cookieString = get_compare_cookie();
		numServos = cookieString.split(",").length + 1; //plus one because current servo hasn't been added to cookie yet
		return numServos;
	}
	else return 1;
}


function update_counter(count) {
	if(count=='') count = compare_count() - 1;
	document.getElementById('counter').innerHTML = count;
}


function compare_add(servo) {
	numServos = compare_count();
	if (numServos <= 25) {
		var divID = "comp-" + servo;
		var compareDiv = document.getElementById(divID);
		add_servo_to_cookie(servo);
		compareDiv.className = "remove";
		update_counter(numServos);
	}
	else {
		alert("Your comparison engine is full. The maximum is 25 servos.");
	}
	//alert(get_compare_cookie());  for testing
}


function compare_remove(servo) {
	var divID = "comp-" + servo;
	var compareDiv = document.getElementById(divID);
	remove_servo_from_cookie(servo);
	compareDiv.className = "add";
	numServos = compare_count();
	update_counter(numServos - 1);
	//alert(get_compare_cookie());  for testing
}


function add_servo_to_cookie(servo) {
	servoString = get_compare_cookie();
	if(servoString) servoString += ",";
		else servoString = "";
	servoString += servo;
	set_compare_cookie(servoString);
}


function remove_servo_from_cookie(servo) {
	servoString = get_compare_cookie();
	if(servoString) {
		var reg = new RegExp("^" + servo + ","); // first item in list
		servoString = servoString.replace(reg, "");
		var reg = new RegExp("^" + servo + "$"); // only iten in list
		servoString = servoString.replace(reg, "");
		var reg = new RegExp("," + servo + "$"); // last item in list
		servoString = servoString.replace(reg, "");
		var reg = new RegExp("," + servo + ","); // middle item in list
		servoString = servoString.replace(reg, ",");
	}
	set_compare_cookie(servoString);
}


function set_compare_cookie(servoString) {
	var exdate=new Date();
	exdate.setDate(exdate.getDate()+30);
	document.cookie = "comparison=" + escape(servoString) + "; expires=" + exdate.toGMTString() + "; path=/";
}


function unset_compare_cookie() {
	var exdate=new Date();
	exdate.setDate(exdate.getDate()-30);
	document.cookie = "comparison=" + "; expires=" + exdate.toGMTString() + "; path=/";	
}	


function get_compare_cookie() {
	c_name = "comparison"
	if (document.cookie.length>0) {
		c_start=document.cookie.indexOf(c_name + "=");
		if (c_start!=-1) {
			c_start=c_start + c_name.length+1;
			c_end=document.cookie.indexOf(";",c_start);
			if (c_end==-1) c_end=document.cookie.length;
				return unescape(document.cookie.substring(c_start,c_end));
		}
	}
	return false;
}






function show_settings() {
	lnk = document.getElementById('collapse');
	list = document.getElementById('settings');
	if (lnk.innerHTML == 'Show search criteria') {
		lnk.innerHTML = 'Hide search criteria';
		list.setAttribute("class", "clear");
		list.setAttribute("className", "clear"); // IE hack
		lnk.setAttribute("class", "up");
		lnk.setAttribute("className", "up"); // IE hack
	}
	else {
		lnk.innerHTML = 'Show search criteria';
		list.setAttribute("class", "collapsed");
		list.setAttribute("className", "collapsed"); // IE hack
		lnk.setAttribute("class", "down");
		lnk.setAttribute("className", "down"); // IE hack
	}
}


function show_brands() {
	lnk = document.getElementById('morebrands');
	list = document.getElementById('secondary');
	if (lnk.innerHTML == 'More') {
		lnk.innerHTML = 'Less';
		list.setAttribute("class", "clear");
		list.setAttribute("className", "clear"); // IE hack
		lnk.setAttribute("class", "up");
		lnk.setAttribute("className", "up"); // IE hack
	}
	else {
		lnk.innerHTML = 'More';
		list.setAttribute("class", "collapsed");
		list.setAttribute("className", "collapsed"); // IE hack
		lnk.setAttribute("class", "down");
		lnk.setAttribute("className", "down"); // IE hack
	}
}


function toggle_makes(direction) {
	list = document.getElementById('allmakes');
	if (direction == 'show') {
		list.setAttribute("class", "clear");
		list.setAttribute("className", "clear"); // IE hack
		access = false;
	}
	if (direction == 'hide') {
		list.setAttribute("class", "collapsed");
		list.setAttribute("className", "collapsed"); // IE hack
		access = true;
	}
	group = document.forms['filter'].elements['make[]'];
	for (i = 0; i < group.length; i++) {
		group[i].disabled = access;
	}
}


function set_checked(val) {
	group = document.forms['filter'].elements['make[]'];
	for (i = 0; i < group.length; i++) {
		group[i].checked = val;
	}
}
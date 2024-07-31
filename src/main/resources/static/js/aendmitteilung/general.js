const _dateOptions = {
	year: 'numeric',
	month: '2-digit',
	day: '2-digit'
};
/**
 * change telelist to current list of selected location
 */
function changeTeleList() {
	//var location_header = document.getElementById("location_header");
	var location = document.getElementById("option_location").value;
	document.getElementById("prename").value = "";
	document.getElementById("name").value = "";
	switch (location) {
		case "kall":
			_persons = _telelist_kall;
			break;
		case "khm":
			_persons = _telelist_khm;
			break;
		case "uelp":
			_persons = _telelist_uelp;
			break;
		case "zhm":
			_persons = _telelist_zhm;
			break;
		case "qubi":
			_persons = _telelist_qubi;
			break;
		case "zvw":
			_persons = _telelist_zvw;
			break;
		default:
			_persons = _telelist_zvw;
	}
}
/**
 * set the field dateCreate to today date
 */
function setDateToday() {
	var date = new Date();

	var day = date.getDate();
	var month = date.getMonth() + 1;
	var year = date.getFullYear();
	
	if (month < 10) month = "0" + month;
	if (day < 10) day = "0" + day;
	
	var today = year + "-" + month + "-" + day;
	document.getElementById("dateCreate").value = today;
}
/**
 * returns current date as locale date string in format dd.mm.yyyy (with starting zeros)
 * @param {string} date 
 *
 */
function GetLocaleDateString(date) {
	var str_date = "";
	if (date !== "") {
		var _date = new Date(date);
		str_date = _date.toLocaleDateString('de-DE', _dateOptions);
	}
	return str_date;
}
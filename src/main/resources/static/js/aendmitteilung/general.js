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
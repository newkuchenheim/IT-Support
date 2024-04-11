window.onload = init;
function init() {
	// reset chosen location
	document.getElementById("option_location").value = "";
	document.getElementById("change_notice").reset();
	var _persIndex = -2;
	var _email_to = "aenderungsmitteilung@new-eu.de";
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
	// Set Date for first load
	setDateToday();
	function changeTeleList() {
		//var location_header = document.getElementById("location_header");
		var location = document.getElementById("option_location").value;
		document.getElementById("prename").value = "";
		document.getElementById("name").value = "";
		switch (location) {
			case "kall":
				_persons = _telelist_kall;
				//location_header.innerHTML = "NEW Kall";
				break;
			case "khm":
				_persons = _telelist_khm;
				//location_header.innerHTML = "NEW Kuchenheim";
				break;
			case "uelp":
				_persons = _telelist_uelp;
				//location_header.innerHTML = "NEW Ülpenich";
				break;
			case "zhm":
				_persons = _telelist_zhm;
				//location_header.innerHTML = "NEW Zingsheim";
				break;
			case "zvw":
				_persons = _telelist_zvw;
				//location_header.innerHTML = "Zentrale Verwaltung";
				break;
			default:
				_persons = _telelist_zvw;
				//location_header.innerHTML = "Zentrale Verwaltung";
		}
	}
	function validateName(prename, name) {
		var _prename = prename;
		var _name = name;
		var i;
		if (_prename == null || _prename === "") _prename = document.getElementById("prename").value;
		if (_name == null || _name === "") _name = document.getElementById("name").value;
		if (_prename === "" || _name === "") i = -2;
		else {
			var _fullname_upp = (_name + " " + _prename).toUpperCase();
			var _pers_fullname_upp;
			for (i = 0; i < _persons.length; i++) {
				if (_persons[i] !== undefined) {
					_pers_fullname_upp = (_persons[i]["Name"].replaceAll("\"","") + " " + _persons[i]["Vorname"].replaceAll("\"","")).toUpperCase();
					if (_fullname_upp == _pers_fullname_upp) {
						break;
					}
				}
			}
			if (i == _persons.length) i = -1;
		}
		
		return i;
	}
	function GetLocaleDateString(date) {
		/*Format Date string yyyy-mm-dd to dd.mm.yyyy*/
		var str_date = "";
		if (date !== "") {
			var _date = new Date(date);
			str_date = _date.toLocaleDateString();
		}
		return str_date;
	}
	
	function validateHousingType(changed, housingType) {
		var valid = false;
		if ((changed && housingType !== "") || !changed) {
			valid = true;
		}
		return valid;
	}
	
	function sendEmail() {
		// get form data
		var _prename_elem = document.getElementById("prename");
		var _name_elem = document.getElementById("name");
		var _location_elem = document.getElementById("option_location");
		var _location_text = _location_elem.options[_location_elem.selectedIndex].text;
		var _dateValidFrom = document.getElementById("dateValidFrom").value;
		var _dateCreate = document.getElementById("dateCreate").value;
		var _comment = document.getElementById("comment_area").value.replaceAll("\n", "\r\n\t\t\t\t\t\t\t\t\t  ");
		var _prename = _prename_elem.value;
		var _name = _name_elem.value;
		var _changeHousingType = document.getElementById("changeHousingType").checked;
		var changedHousingTypeText = (_changeHousingType ? "ja" : "nein");
		var _opt_housingtype_val = document.getElementById("option_housingtype").value;
		var _createdBy = document.getElementById("createdBy").value;
		//var _ccEmail = document.getElementById("cc_email").value;
		// create email parts
		var subject = "Änderungsmitteilung Anschrift ";
		var ccPart = "";
		//if (_ccEmail !== null && _ccEmail !== "") ccPart = "&cc=" + _ccEmail;
		var body;
		if (_prename !== "" && _name !== "" && _location_text !== "" && _dateValidFrom !== "" && validateHousingType(_changeHousingType, _opt_housingtype_val)) {
			var fullname = _name + ", " + _prename;
			// show success Messages
			var _form_success = document.getElementById("form_success");
			var form_success_bs = new bootstrap.Alert(_form_success);
			form_success_bs.show;
			_form_success.classList.remove("visually-hidden");
			// build subject
			subject += fullname;
			var housingTypePart = "";
			if (_changeHousingType) {
				housingTypePart = ", " + _opt_housingtype_val;
			}
			document.getElementById("change_notice").submit();
			// build body
			body = "\t• Zweigstelle:\t\t\t\t\t\t" + _location_text + "\r\n"
				+ "\t• Name, Vorname:\t\t\t\t" + fullname + "\r\n"
				+ "\t• Gültig ab:\t\t\t\t\t\t " + GetLocaleDateString(_dateValidFrom) + "\r\n"
				+ "\t• Änderung der Wohnform:\t\t" + changedHousingTypeText + housingTypePart + "\r\n"
				+ "\t• Bemerkung:\t\t\t\t\t    " + _comment + "\r\n"
				+ "\t• Erstellt durch:\t\t\t\t\t" + _createdBy + "\r\n"
				+ "\t• Erstellt am:\t\t\t\t\t\t  " + GetLocaleDateString(_dateCreate) + "\r\n";
			var mailToLink = "mailto:" + _email_to + "?subject=" + encodeURIComponent(subject) + "&body=" + encodeURIComponent(body) + ccPart;
			window.location.href = mailToLink;
		}
	}
	
	// add change event to location
	document.getElementById("option_location").addEventListener("change", changeTeleList);
	
	document.getElementById("changeHousingType").addEventListener("change", function() {
		if (this.checked) {
			document.getElementById("div_housing_type").hidden = false;
			document.getElementById("option_housingtype").required = true;
		} else {
			var _opt_housingtype = document.getElementById("option_housingtype");
			_opt_housingtype.value = "";
			_opt_housingtype.required = false;
			document.getElementById("div_housing_type").hidden = true;
		}
	});
	
	document.getElementById("change_notice").addEventListener("submit", (e) => {
		e.preventDefault();
		sendEmail();
	});
}
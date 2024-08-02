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
		var _comment = document.getElementById("comment_area").value.replaceAll("\n", "\r\n\t\t\t\t");
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
			body = "\t• Zweigstelle: " + _location_text + "\r\n"
				+ "\t• Name, Vorname: " + fullname + "\r\n"
				+ "\t• Gültig ab: " + GetLocaleDateString(_dateValidFrom) + "\r\n"
				+ "\t• Änderung der Wohnform: " + changedHousingTypeText + housingTypePart + "\r\n"
				+ "\t• Bemerkung: " + _comment + "\r\n"
				+ "\t• Erstellt durch: " + _createdBy + "\r\n"
				+ "\t• Erstellt am: " + GetLocaleDateString(_dateCreate) + "\r\n";
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
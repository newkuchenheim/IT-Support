window.onload = init;
function init() {
	// reset chosen location
	document.getElementById("option_location").value = "";
	document.getElementById("change_notice").reset();
	//var _persIndex = -2;
	var _email_to = "fahrtendienst@new-eu.de";
	/**
	 * set the field dateCreate to today date
	 */
	function setDateCreateToToday() {
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
	setDateCreateToToday();
	
	function addBusline(parent, BusLine, first) {
		var child = document.createElement("option");
		if (first) {
			child.innerHTML = "Bitte auswählen";
			child.value = "";
			child.selected = true;
			child.hidden = true;
		} else {
			child.innerHTML = BusLine["text"];
			child.value = BusLine["value"];
		}
		parent.appendChild(child);
	}
	
	/**
	 * fill busline selection for given location
	 * 
	 * @param location
	 */
	function changeBuslines(location) {
		var _busline_sel = document.getElementById("option_busline");
		// clear elements
		while (_busline_sel.firstChild) {
			_busline_sel.removeChild(_busline_sel.firstChild);
		}
		// add default option
		addBusline(_busline_sel, null, true);
		_busline_sel.value = "";
		// get only busline for given location
		for (var i = 0; i < _BusLines.length; i++) {
			if (_BusLines[i]["description"].includes(location) || _BusLines[i]["value"].startsWith("C") 
				|| _BusLines[i]["value"].startsWith("H") || _BusLines[i]["value"].startsWith("Ef")) {
				addBusline(_busline_sel, _BusLines[i], false)
			}
		}
	}
	
	/**
	 * set person list to choosen location
	 */
	function changeTeleList() {
		//var location_header = document.getElementById("location_header");
		var location = document.getElementById("option_location").value;
		document.getElementById("prename").value = "";
		document.getElementById("name").value = "";
		switch (location) {
			case "kall":
				_persons = _telelist_kall;
				changeBuslines("Kall");
				//location_header.innerHTML = "NEW Kall";
				break;
			case "khm":
				_persons = _telelist_khm;
				changeBuslines("Kuchenheim");
				//location_header.innerHTML = "NEW Kuchenheim";
				break;
			case "uelp":
				_persons = _telelist_uelp;
				changeBuslines("Ülpenich");
				//location_header.innerHTML = "NEW Ülpenich";
				break;
			case "zhm":
				_persons = _telelist_zhm;
				changeBuslines("Zingsheim");
				//location_header.innerHTML = "NEW Zingsheim";
				break;
			case "zvw":
				_persons = _telelist_zvw;
				changeBuslines("Verwaltung");
				//location_header.innerHTML = "Zentrale Verwaltung";
				break;
			default:
				_persons = _telelist_zvw;
				changeBuslines("Verwaltung");
				//location_header.innerHTML = "Zentrale Verwaltung";
		}
	}
	
	/**
	 * reset Div fields
	 * 
	 * @param {boolean} selfdriver - true: reset self driver fields, false: reset shuttle fields
	 */
	function resetDivFields(selfdriver) {
		if (selfdriver) {
			document.getElementById("option_drivertype").value = "";
			var division = document.querySelector("input[type='radio'][name=divisions]:checked");
			if (divison != null) division.checked = false;
		} else {
			var meetingpoint = document.querySelector("input[type='radio'][name=meetingpoints]:checked");
			var escort = document.querySelector("input[type='radio'][name=escorts]:checked");
			var costacceptance = document.querySelector("input[type='radio'][name=costacceptance]:checked");
			if (meetingpoint != null) meetingpoint.checked = false;
			if (escort != null) escort.checked = false;
			if (costacceptance != null) costacceptance.checked = false;
			document.getElementById("option_withdrawal_reason").value = "";
			document.getElementById("option_busline").value = "";
			document.getElementById("wheelchair").checked = false;
			document.getElementById("height").value = "";
			document.getElementById("width").value = "";
			document.getElementById("depth").value = "";
		}
	}
	/*function validateName(prename, name) {
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
	}*/
	
	/**
	 * @returns current date as locale date string in format dd.mm.yyyy (without starting zeros)
	 */
	function GetLocaleDateString(date) {
		/*Format Date string yyyy-mm-dd to dd.mm.yyyy*/
		var str_date = "";
		if (date !== "") {
			var _date = new Date(date);
			str_date = _date.toLocaleDateString();
		}
		return str_date;
	}
	
	/**
	 * creates a email template with the form data and send this to local email client
	 */
	function sendEmail() {
		// get form data
		var _prename_elem = document.getElementById("prename");
		var _name_elem = document.getElementById("name");
		var _location_elem = document.getElementById("option_location");
		var _location_text = _location_elem.options[_location_elem.selectedIndex].text;
		var _dateCreate = document.getElementById("dateCreate").value;
		var _dateFrom = document.getElementById("dateFrom").value;
		var _newIntake = document.getElementById("newIntake").checked;
		var newIntake_text = (_newIntake ? "ja" : "nein");
		var _street = document.getElementById("street").value;
		var _zipcode = document.getElementById("zipcode").value;
		var _city = document.getElementById("city").value;
		var _phone = document.getElementById("phone").value;
		var _changes_elem = document.querySelector("input[type='radio'][name=changes]:checked");
		var _changes_val = "";
		var _changes_text = "";
		if (_changes_elem != null) {
			_changes_val = _changes_elem.value;
			_changes_text = document.querySelector("label[for='" + _changes_elem.id + "']").innerText;
		}
		var _selfdriver = document.getElementById("selfdriver").checked;
		var selfdriver_text = (_selfdriver ? "ja" : "nein");
		var opt_withdrawal_reason = document.getElementById("option_withdrawal_reason").value;
		var _optbusline = document.getElementById("option_busline").value;
		var _meetingpoint = "";
		var _escort = "";
		if (!_selfdriver) {
			_meetingpoint = document.querySelector("input[type='radio'][name=meetingpoints]:checked").value;
			_escort = document.querySelector("input[type='radio'][name=escorts]:checked").value;
		}
		var _wheelchair = document.getElementById("wheelchair").checked;
		var wheelchair_text = (_wheelchair ? "ja" : "nein");
		var _height = document.getElementById("height").value;
		var _width = document.getElementById("width").value;
		var _depth = document.getElementById("depth").value;
		var _costacceptance_elem = document.querySelector("input[type='radio'][name=costacceptance]:checked");
		var _costacceptance = "";
		if (_costacceptance_elem != null) _costacceptance = _costacceptance_elem.value;
		var _optdrivertype = document.getElementById("option_drivertype").value;
		var _division_elem = document.querySelector("input[type='radio'][name=divisions]:checked");
		var _division = "";
		if (_division_elem != null) _division = _division_elem.value;
		var _comment = document.getElementById("comment_area").value.replaceAll("\n", "\r\n\t\t\t\t\t\t\t\t     ");
		var _prename = _prename_elem.value;
		var _name = _name_elem.value;
		var _createdBy = document.getElementById("createdBy").value;
		var _dateDoneBy = document.getElementById("dateDoneBy").value;
		var _doneby = document.getElementById("doneby").value;
		var _km = document.getElementById("km").value;
		var _dailyrate = document.getElementById("dailyrate").value;
		//var _ccEmail = document.getElementById("cc_email").value;
		// create email parts
		var subject = "Änderungsmitteilung Fahrtendienst ";
		var ccPart = "";
		//if (_ccEmail !== null && _ccEmail !== "") ccPart = "&cc=" + _ccEmail;
		var body;
		if (_prename !== "" && _name !== "" && _location_text !== ""  && _createdBy !== "" && _dateFrom !== "" 
			&& ((_selfdriver && _optdrivertype !== "" && _division !== "") ||  _meetingpoint !== "" && _escort !== "")) {
			var fullname = _name + ", " + _prename;
			// show success Messages
			var _form_success = document.getElementById("form_success");
			var form_success_bs = new bootstrap.Alert(_form_success);
			form_success_bs.show;
			_form_success.classList.remove("visually-hidden");
			// build subject
			subject += fullname;
			document.getElementById("change_notice").submit();
			// build body
			var selfdriver_part = "\t• Selbstfahrer:\t\t\t\t\t" + _optdrivertype + ", " + _division + "\r\n"
					+ "\t• Änderung von Verwaltung:\t  " + _km + " Kilometer bis zur NEW, Tagessatz in Euro: " + _dailyrate + "\r\n";
			var shuttle_part = "\t• Austritt:\t\t\t\t\t   " + opt_withdrawal_reason + ", Linie: " + _optbusline + "\r\n"
					+ "\t• Abholpunkt:\t\t\t\t\t" + _meetingpoint + "\r\n"
					+ "\t• Begleitperson:\t\t\t\t" + _escort + "\r\n"
					+ "\t• Rollstuhl:\t\t\t\t\t    " + wheelchair_text;
			if (_wheelchair) {
				shuttle_part += ", Höhe (cm): " + _height + " x Breite (cm): " + _width + " x Tiefe (cm): " + _depth + "\r\n";
			}
			shuttle_part += "\t• Bemerkung / Sonstige Info:\t     " + _comment + "\r\n";
			body = "\t• Zweigstelle:\t\t\t\t\t" + _location_text + "\r\n"
				+ "\t• Erstellt durch:\t\t\t\t" + _createdBy + ", am " + GetLocaleDateString(_dateCreate) + "\r\n"
				+ "\t• Name, Vorname:\t\t\t " + fullname + "\r\n"
				+ "\t• Gütig Ab:\t\t\t\t\t  " + GetLocaleDateString(_dateFrom) + "\r\n"
				+ "\t• Neuaufnahme:\t\t\t\t   " + newIntake_text + "\r\n"
				+ "\t• Adresse:\t\t\t\t\t  " + _street + "\r\n\t\t\t\t\t\t\t\t   " + _zipcode + " " + _city + "\r\n\t\t\t\t\t\t\t\t   Telefon: " + _phone + "\r\n"
				+ "\t• Wechsel:\t\t\t\t\t  " + _changes_text + "\r\n";
			if (_selfdriver) {
				body += selfdriver_part;
			} else {
				body += shuttle_part;
			}
			body += "\t• Kostenzusage / Genehmigung:   " + _costacceptance + "\r\n"
				 + "\t• Erledigt durch:\t\t\t\t  " + _doneby + "\r\n"
				 + "\t• Erledigt am:\t\t\t\t\t    " + GetLocaleDateString(_dateDoneBy) + "\r\n";
			var mailToLink = "mailto:" + _email_to + "?subject=" + encodeURIComponent(subject) + "&body=" + encodeURIComponent(body) + ccPart;
			window.location.href = mailToLink;
		}
	}
	
	// add change event to location
	document.getElementById("option_location").addEventListener("change", changeTeleList);
	// show and reset needed form fields
	document.getElementById("selfdriver").addEventListener("change", function() {
		if (this.checked) {
			document.getElementById("div_selfdriver").hidden = false;
			document.getElementById("div_shuttle").hidden = true;
			document.getElementById("bbb").required = true;
			document.getElementById("addresspoint").required = false;
			document.getElementById("escortsyes").required = false;
			resetDivFields(false);
		} else {
			document.getElementById("div_selfdriver").hidden = true;
			document.getElementById("div_shuttle").hidden = false;
			document.getElementById("bbb").required = false;
			document.getElementById("addresspoint").required = true;
			document.getElementById("escortsyes").required = true;
			resetDivFields(true);
		}
	});
	
	// show or hide wheelchair fields
	document.getElementById("wheelchair").addEventListener("change", function() {
		if (this.checked) {
			document.getElementById("div_wheelchair_size").hidden = false;
			document.getElementById("height").required = true;
			document.getElementById("width").required = true;
			document.getElementById("depth").required = true;
		} else {
			document.getElementById("div_wheelchair_size").hidden = true;
			var _height_elem = document.getElementById("height");
			_height_elem.value = "";
			_heigth_elem.required = false;
			var _width_elem = document.getElementById("width");
			_width_elem.value = "";
			_width_elem.required = false;
			var _depth_elem = document.getElementById("depth");
			_depth_elem.value = "";
			_depth_elem.required = false;
		}
	});
	document.getElementById("change_notice").addEventListener("submit", (e) => {
		e.preventDefault();
		sendEmail();
	});
}
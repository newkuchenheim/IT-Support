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
	
	function addCostCentre(parent, CostCentre, first) {
		var child = document.createElement("option");
		if (first) {
			child.innerHTML = "Bitte die Kostenstelle auswählen";
			child.value = "";
			child.selected = true;
			child.hidden = true;
		} else {
			var text = CostCentre["number"] + " - " + CostCentre["label"];
			child.innerHTML = text;
			child.value = text;
		}
		parent.appendChild(child);
	}
	
	function changeCostCentres(location) {
		_opt_oldcostcentre = document.getElementById("option_oldcostcentre");
		_opt_newcostcentre = document.getElementById("option_newcostcentre");
		// clear option lists
		while (_opt_oldcostcentre.firstChild) {
			_opt_oldcostcentre.removeChild(_opt_oldcostcentre.firstChild);
		}
		while (_opt_newcostcentre.firstChild) {
			_opt_newcostcentre.removeChild(_opt_newcostcentre.firstChild);
		}
		_opt_oldcostcentre.value = "";
		_opt_newcostcentre.value = "";
		// add first option
		addCostCentre(_opt_oldcostcentre, null, true);
		addCostCentre(_opt_newcostcentre, null, true);
		for (var i = 0; i < _CostCentres.length; i++) {
			if (_CostCentres[i]["location"] !== "" && _CostCentres[i]["location"] === location) {
				addCostCentre(_opt_oldcostcentre, _CostCentres[i], false);
				addCostCentre(_opt_newcostcentre, _CostCentres[i], false);
			} else if (_CostCentres[i]["number"] < "17910") {
				addCostCentre(_opt_newcostcentre, _CostCentres[i], false);
			}
		}
		
	}
	
	
	function changeTeleList() {
		//var location_header = document.getElementById("location_header");
		var location_elem = document.getElementById("option_location");
		var location_val = location_elem.value;
		var location_text = location_elem.options[location_elem.selectedIndex].text;
		document.getElementById("prename").value = "";
		document.getElementById("name").value = "";
		switch (location_val) {
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
		changeCostCentres("NEW " + location_text);
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
	
	function sendEmail() {
		// get form data
		var _prename_elem = document.getElementById("prename");
		var _name_elem = document.getElementById("name");
		var _location_elem = document.getElementById("option_location");
		var _location_text = _location_elem.options[_location_elem.selectedIndex].text;
		var _dateChange = document.getElementById("dateChange").value;
		var _optoldcostcentre = document.getElementById("option_oldcostcentre").value;
		var _optnewcostcentre = document.getElementById("option_newcostcentre").value;
		var _dateCreate = document.getElementById("dateCreate").value;
		var _comment = document.getElementById("comment_area").value.replaceAll("\n", "\r\n\t\t\t\t\t\t   ");
		var _prename = _prename_elem.value;
		var _name = _name_elem.value;
		var _createdBy = document.getElementById("createdBy").value;
		//var _ccEmail = document.getElementById("cc_email").value;
		// create email parts
		var subject = "Änderungsmitteilung Wechsel Kostenstelle ";
		var ccPart = "";
		//if (_ccEmail !== null && _ccEmail !== "") ccPart = "&cc=" + _ccEmail;
		var body;
		if (_prename !== "" && _name !== "" && _location_text !== "" && _dateChange !== "" && _optoldcostcentre !== "" && _optnewcostcentre !== "" && _createdBy !== "") {
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
			body = "\t• Zweigstelle:\t\t\t" + _location_text + "\r\n"
				+ "\t• Name, Vorname:\t " + fullname + "\r\n"
				+ "\t• Wechseltag:\t\t\t" + GetLocaleDateString(_dateChange) + "\r\n"
				+ "\t• Alte Kostenstelle:\t    " + _optoldcostcentre + "\r\n"
				+ "\t• Neue Kostenstelle:\t  " + _optnewcostcentre + "\r\n"
				+ "\t• Bemerkung:\t\t      " + _comment + "\r\n"
				+ "\t• Erstellt durch:\t\t" + _createdBy + "\r\n"
				+ "\t• Erstellt am:\t\t\t  " + GetLocaleDateString(_dateCreate) + "\r\n";
			var mailToLink = "mailto:" + _email_to + "?subject=" + encodeURIComponent(subject) + "&body=" + encodeURIComponent(body) + ccPart;
			window.location.href = mailToLink;
		}
	}
	
	// add change event to location
	document.getElementById("option_location").addEventListener("change", changeTeleList);
	
	document.getElementById("change_notice").addEventListener("submit", (e) => {
		e.preventDefault();
		sendEmail();
	});
}
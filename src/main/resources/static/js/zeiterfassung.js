window.onload = init;
function init() {
	// reset chosen location
	document.getElementById("zvw_location").checked = true;
	var general_email_to = "";
	var cc_email = "";
	var persIndex = -2;
	function changeTeleList() {
		var location_header = document.getElementById("location_header");
		var location_warn = document.getElementById("location_warn");
		var location = document.querySelector("input[type='radio'][name=location]:checked").value;
		document.getElementById("name_warning").classList.add("visually-hidden");
		document.getElementById("prename").value = "";
		document.getElementById("name").value = "";
		switch (location) {
			case "kall":
				persons = telelist_kall;
				location_header.innerHTML = "Werkstatt Kall";
				location_warn.innerText = "Kall";
				general_email_to = "h.opgenorth@new-eu.de";
				cc_email = "korrekturbeleg-kall@new-eu.de";
				break;
			case "khm":
				persons = telelist_khm;
				location_header.innerHTML = "Werkstatt Kuchenheim";
				location_warn.innerText = "Kuchenheim";
				general_email_to = "";
				cc_email = "a.winkelhag@new-eu.de; b.erven@new-eu.de";
				break;
			case "uelp":
				persons = telelist_uelp;
				location_header.innerHTML = "Werkstatt Ülpenich";
				location_warn.innerText = "Ülpenich";
				general_email_to = "g.schalke@new-eu.de";
				cc_email = "";
				break;
			case "zhm":
				persons = telelist_zhm;
				location_header.innerHTML = "Werkstatt Zingsheim";
				location_warn.innerText = "Zingsheim";
				general_email_to = "c.merks@new-eu.de";
				cc_email = "verwaltung-zingsheim@new-eu.de";
				break;
			case "zvw":
				persons = telelist_zvw;
				location_header.innerHTML = "Zentrale Verwaltung";
				location_warn.innerText = "der Zentralen Verwaltung";
				general_email_to = "";
				cc_email = "";
				break;
			default:
				location_header.innerHTML = "Zentrale Verwaltung";
				location_warn.innerText = "der Zentralen Verwaltung";
				persons = telelist_zvw;
				general_email_to = "";
				cc_email = "";
		}
	}
	function addReason(parent, reason, first) {
		var child = document.createElement("option");
		if (first) {
			child.innerHTML = "Bitte den Grund auswählen";
			child.value = "";
			child.selected = true;
			child.hidden = true;
		} else {
			child.innerHTML = reason["text"];
			child.value = reason["value"];
		}
		parent.appendChild(child);
	}
	function changeReason(request) {
		var optreason_elem = document.getElementById("option_reason");
		// clear opt list
		while (optreason_elem.firstChild) {
			optreason_elem.removeChild(optreason_elem.firstChild);
		}
		// add specific options with request
		addReason(optreason_elem, "", true);
		for (var i = 0; i < optreasons.length; i++) {
			if (request.includes("berstunden") && (optreasons[i]["text"].includes("Sonstige") || optreasons[i]["value"] == "-")) {
				addReason(optreason_elem, optreasons[i], false);
			} else if (request.includes("Krank") && optreasons[i]["text"].includes("Krank")) {
				addReason(optreason_elem, optreasons[i], false);
			} else if (request.includes("Korrektur") && !optreasons[i]["text"].includes("Krank")) {
				addReason(optreason_elem, optreasons[i], false);
			}
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
			for (i = 0; i < persons.length; i++) {
				if (persons[i] !== undefined) {
					_pers_fullname_upp = (persons[i]["Name"].replaceAll("\"","") + " " + persons[i]["Vorname"].replaceAll("\"","")).toUpperCase();
					if (_fullname_upp == _pers_fullname_upp) {
						break;
					}
				}
			}
			if (i == persons.length) i = -1;
		}
		
		return i;
	}
	function validateDate(from, to) {
		var valid = false;
		if (from !== "" && to !== "" && to >= from) valid = true;
		else if (from !== "" && to === "" || to !== "" && from === "") valid = true;
		return valid;
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
		var _dateFrom_elem = document.getElementById("dateFrom");
		var _timeFrom_elem = document.getElementById("timeFrom");
		var _dateTo_elem = document.getElementById("dateTo");
		var _timeTo_elem = document.getElementById("timeTo");
		var _prename = _prename_elem.value;
		var _name = _name_elem.value;
		var _optrequest = document.getElementById("option_request").value;
		var _optreason = document.getElementById("option_reason").value;
		var _description = document.getElementById("description_area").value.replaceAll("\n", "\r\n\t\t\t\t\t\t  ");
		var _dateFrom = _dateFrom_elem.value;
		var _timeFrom = _timeFrom_elem.value;
		var _dateTo = _dateTo_elem.value;
		var _timeTo = _timeTo_elem.value;
		var location = document.querySelector("input[type='radio'][name=location]:checked").value;
		// create email parts
		var email_to = "";
		var subject = "Korrekturbeleg-Zeiterfassung ";
		var body;
		var func;
		var pers_email;
		if (_prename !== "" && _name !== "" && _optrequest !== "" && _optreason !== "") {
			var fullname = _name + " " + _prename;
			var validDate = validateDate(_dateFrom, _dateTo);
			if (validDate) {
				// remove red border
				if (_dateTo_elem.hasAttribute("style")) _dateTo_elem.removeAttribute("style");
				if (_timeTo_elem.hasAttribute("style")) _timeTo_elem.removeAttribute("style");
				if (location !== "zvw" && location !== "uelp") {
					// get email of team head
					if (persIndex > -1) {
						func = persons[persIndex]["Funktion"];
						pers_email = persons[persIndex]["E-Mail"].replaceAll("\"", "");
						for (i = 0; i < persons.length; i++) {
							if (persons[i] !== undefined) {
								if (func.includes("-") && !func.includes("Teamleitung")) {
									var funcParts = func.split("-");
									if ((persons[i]["Funktion"].includes(funcParts[1].trim()) || persons[i]["Funktion"].includes(funcParts[0].trim())) 
									&& (persons[i]["Funktion"].includes("Abteilungsleitung") || persons[i]["Funktion"].includes("Teamleitung"))) {
										email_to = persons[i]["E-Mail"].replaceAll("\"","");
										break;	
									} else if (i == persons.length - 1 && func.includes("ozialer Dienst")) {
										email_to = "t.scheuls@new-eu.de";
									}
								} else if (persons[i]["Funktion"].includes(func) && (persons[i]["Funktion"].includes("Abteilungsleitung") || persons[i]["Funktion"].includes("Teamleitung"))) {
									email_to = persons[i]["E-Mail"].replaceAll("\"","");
									break;
								} else if (i == persons.length - 1 && func.includes("ozialer Dienst")) {
									email_to = "t.scheuls@new-eu.de";
								}
							}
						}
					}
				}
				// set email if no team head found
				if (email_to.length == 0) {
					email_to = general_email_to;
				}
				// build cc part
				var cc_part = "";
				if (cc_email !== "") cc_part = "&cc=" + cc_email; 
				// submit and reset form
				document.getElementById("dateTo_error").classList.add("visually-hidden");
				document.getElementById("name_warning").classList.add("visually-hidden");
				document.getElementById("time_entering").submit();
				// show success Messages
				var _form_success = document.getElementById("form_success");
				var form_success_bs = new bootstrap.Alert(_form_success);
				form_success_bs.show;
				_form_success.classList.remove("visually-hidden");
				// build subject
				subject += fullname;
				// build body
				body = "\t• Name, Vorname:\t" + fullname + "\r\n"
					+ "\t• Antrag auf:\t\t\t" + _optrequest + "\r\n"
					+ "\t• Grund:\t\t\t  " + _optreason + "\r\n"
					+ "\t• Erläuterungen:\t    " + _description + "\r\n"
					+ "\t• Datum und Uhrzeit:\tVon: " + GetLocaleDateString(_dateFrom) + " " + _timeFrom + "\r\n"
					+ "\t\t\t\t\t\t   Bis:  " + GetLocaleDateString(_dateTo) + " " + _timeTo + "\r\n";
				var mailToLink = "mailto:" + email_to + "?subject=" + encodeURIComponent(subject) + cc_part + "&body=" + encodeURIComponent(body);
				window.location.href = mailToLink;
			} else {
					_dateTo_elem.setAttribute("style", "border-color: red");
					_timeTo_elem.setAttribute("style", "border-color: red");
					var _dateTo_error = document.getElementById("dateTo_error");
					var dateTo_error_alert = new bootstrap.Alert(_dateTo_error);
					dateTo_error_alert.show;
					_dateTo_error.classList.remove("visually-hidden");
			}
		}
	}
	
	// add Change EventListener to all Locations
	var locations = document.getElementsByName("location");
	for (var i = 0; i < locations.length; i++) {
		locations[i].addEventListener("change", changeTeleList);
	}
	
	// set required date after reason changed
	document.getElementById("option_reason").addEventListener("change", function() {
		var value_low = this.options[this.selectedIndex].value.toLowerCase();
		var dateFrom_elem = document.getElementById("dateFrom");
		var timeFrom_elem = document.getElementById("timeFrom");
		var dateTo_elem = document.getElementById("dateTo");
		var timeTo_elem = document.getElementById("timeTo");
		if (value_low.includes("kommen")) {
			dateFrom_elem.required = true;
			timeFrom_elem.required = true;
			dateTo_elem.required = false;
			timeTo_elem.required = false;
			// clear not required fields
			dateTo_elem.value = "";
			timeTo_elem.value = "";
		} else if (value_low.includes("gehen")) {
			dateFrom_elem.required = false;
			timeFrom_elem.required = false;
			dateTo_elem.required = true;
			timeTo_elem.required = true;
			// clear not required fields
			dateFrom_elem.value = "";
			timeFrom_elem.value = "";
		} else {
			dateFrom_elem.required = true;
			timeFrom_elem.required = true;
			dateTo_elem.required = true;
			timeTo_elem.required = true;
		}
	});
	
	// Set Time to 00:00 if date change
	document.getElementById("dateFrom").addEventListener("change", function(e) {
		if (e.value !== "" && document.getElementById("timeFrom").value === "") document.getElementById("timeFrom").value = "00:00";
	});
	document.getElementById("dateTo").addEventListener("change", function(e) {
		if (e.value !== "" && document.getElementById("timeTo").value === "") document.getElementById("timeTo").value = "00:00";
	});
	
	document.getElementById("time_entering").addEventListener("submit", (e) => {
		e.preventDefault();
		sendEmail();
	});
	
	document.getElementById("option_request").addEventListener("change", function() {
		changeReason(this.options[this.selectedIndex].value);
	});
	
	document.getElementById("prename").addEventListener("change", function() {
		persIndex = validateName(this.value, null);
		if (persIndex == -1) {
			var _name_warning = document.getElementById("name_warning");
			var name_warning_alert = new bootstrap.Alert(_name_warning);
			name_warning_alert.show;
			_name_warning.classList.remove("visually-hidden");
		} else {
			document.getElementById("name_warning").classList.add("visually-hidden");
		}
	});
	
	document.getElementById("name").addEventListener("change", function() {
		persIndex = validateName(null, this.value);
		if (persIndex == -1) {
			var _name_warning = document.getElementById("name_warning");
			var name_warning_alert = new bootstrap.Alert(_name_warning);
			name_warning_alert.show;
			_name_warning.classList.remove("visually-hidden");
		} else {
			document.getElementById("name_warning").classList.add("visually-hidden");
		}
	});
}

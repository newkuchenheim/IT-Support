window.onload = init;
function init() {
	// reset chosen location
	var location = document.querySelector("input[type='radio'][name=location]:checked");
	if (location != null) {
		location.checked = false;
	}
	//var general_email_to = "";
	
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
				cc_email = "korrekturbeleg-kall@new-eu.de";
				break;
			case "khm":
				persons = telelist_khm;
				location_header.innerHTML = "Werkstatt Kuchenheim";
				location_warn.innerText = "Kuchenheim";
				cc_email = "korrekturbeleg-kuchenheim@new-eu.de";
				break;
			case "uelp":
				persons = telelist_uelp;
				location_header.innerHTML = "Werkstatt Ülpenich";
				location_warn.innerText = "Ülpenich";
				cc_email = "korrekturbeleg-uelpenich@new-eu.de";
				break;
			case "zhm":
				persons = telelist_zhm;
				location_header.innerHTML = "Werkstatt Zingsheim";
				location_warn.innerText = "Zingsheim";
				cc_email = "korrekturbeleg-zingsheim@new-eu.de";
				break;
			case "zvw":
				persons = telelist_zvw;
				location_header.innerHTML = "Zentrale Verwaltung";
				location_warn.innerText = "der Zentralen Verwaltung";
				cc_email = "korrekturbeleg-verwaltung@new-eu.de";
				break;
			case "qubi":
				persons = telelist_qubi;
				location_header.innerHTML = "QuBi Eifel Mechernich";
				location_warn.innerText = "dem QuBi Eifel Mechernich";
				cc_email = "korrekturbeleg-qubi@new-eu.de";
				break;
			default:
				location_header.innerHTML = "Zentrale Verwaltung";
				location_warn.innerText = "der Zentralen Verwaltung";
				persons = telelist_zvw;
				cc_email = "korrekturbeleg-verwaltung@new-eu.de";
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
			
			if(reason["text"].includes("Sonstiges")){
				child.selected = true;
				
				var timeFrom_elem = document.getElementById("timeFrom");
				var timeTo_elem = document.getElementById("timeTo");
				
				timeFrom_elem.required = true;
				timeTo_elem.required = true;
			}
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
			if (request.includes("Krankbuchung") && optreasons[i]["text"].includes("Krank")) {
				addReason(optreason_elem, optreasons[i], false);
			} else if(request === "Korrekturbuchung" && optreasons[i]["text"].includes("Sonstiges")){
				
				addReason(optreason_elem, optreasons[i], false);	
			} 
		}
		
	}
	
	function validateName(prename, name) {
		var _prename = prename;
		var _name = name;
		var i;
		if (persons != null) {
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
		} else {
			i = -2;
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
			var options = {
				year: "numeric",
				month: "2-digit",
				day: "2-digit"
			};
			var _date = new Date(date);
			str_date = _date.toLocaleDateString("de-DE", options);
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
		var _description = document.getElementById("description_area").value.replaceAll("\n", "\r\n\t\t\t\t\t");
		var _dateFrom = _dateFrom_elem.value;
		var _timeFrom = _timeFrom_elem.value;
		var _dateTo = _dateTo_elem.value;
		var _timeTo = _timeTo_elem.value;
		var _location = document.querySelector("input[type='radio'][name=location]:checked");
		// create email parts
		//var email_to = "";
		var body;
		if (_prename !== "" && _name !== "" && _optrequest !== "" && _optreason !== "" 
		&& _location != null) {
			var fullname = _name + " " + _prename;
			var validDate = validateDate(_dateFrom, _dateTo);
			if (validDate) {
				// remove red border
				if (_dateTo_elem.hasAttribute("style")) _dateTo_elem.removeAttribute("style");
				if (_timeTo_elem.hasAttribute("style")) _timeTo_elem.removeAttribute("style");
				
				// submit and reset form
				document.getElementById("dateTo_error").classList.add("visually-hidden");
				document.getElementById("location_error").classList.add("visually-hidden");
				document.getElementById("name_warning").classList.add("visually-hidden");
				document.getElementById("time_entering").submit();
				// show success Messages
				var _form_success = document.getElementById("form_success");
				var form_success_bs = new bootstrap.Alert(_form_success);
				form_success_bs.show;
				_form_success.classList.remove("visually-hidden");
				// build subject
				var subject = "Korrekturbeleg-"+_optrequest + ": " + fullname;
				// build body
				body = "Korrekturbeleg-"+_optrequest + ":\r\n"
					+ "\t• Name, Vorname: " + fullname + "\r\n"
					+ "\t• Antrag auf: " + _optrequest + "\r\n"
					+ "\t• Grund: " + _optreason + "\r\n"
					+ "\t• Erläuterungen: " + _description + "\r\n"
					+ "\t• Datum: " + GetLocaleDateString(_dateFrom) + " " + _timeFrom + " - \r\n"
					+ GetLocaleDateString(_dateTo) + " " + _timeTo + "\r\n";
				var mailToLink = "mailto:" + cc_email + "?subject=" + encodeURIComponent(subject) + "&body=" + encodeURIComponent(body);
				window.location.href = mailToLink;
			} else {
					_dateTo_elem.setAttribute("style", "border-color: red");
					_timeTo_elem.setAttribute("style", "border-color: red");
					var _dateTo_error = document.getElementById("dateTo_error");
					var dateTo_error_alert = new bootstrap.Alert(_dateTo_error);
					dateTo_error_alert.show;
					_dateTo_error.classList.remove("visually-hidden");
			}
		} else if (_location == null) {
			var _location_error = document.getElementById("location_error");
			var location_error_alert = new bootstrap.Alert(_location_error);
			location_error_alert.show;
			_location_error.classList.remove("visually-hidden");
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
		
		dateFrom_elem.required = true;
		dateTo_elem.required = true;
		if(value_low.includes("sonstiges")){
			timeFrom_elem.required = true;
			timeTo_elem.required = true;
		} else {
			timeFrom_elem.required = false;
			timeTo_elem.required = false;
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
	wtConfig.MainSteps[0].title = "Anleitung Korrekturbeleg";
	wtConfig.MainSteps[4].title = "5" + wtConfig.MainSteps[4].title.substring(1);
	var steps = [
		wtConfig.MainSteps[0],
		{
			element: "#step_location",
			title: "1. Schritt",
			content: "Wählen Sie Ihren Standort aus.<br>Ggf. vorher auf Korrekturbeleg klicken.",
			placement: "right",
			btnNext: wtConfig.btnNext,
			btnBack: wtConfig.btnBack,
			onNext: function () {
				var location = document.querySelector("input[type='radio'][name=location]:checked");
				if (location == null || location.value === "") {
					wtConfig.nextCustom();
				}
			}
		},
		wtConfig.MainSteps[2],
		{
			element: "#step_request",
			title: "3. Schritt",
			content: "Wählen Sie die Antragsart und den Grund aus.<br>Bei Bedarf können Sie es erläutern.",
			placement: "bottom",
			btnNext: wtConfig.btnNext,
			btnBack: wtConfig.btnBack,
			width: "450px",
			onNext: function () {
				var request = document.getElementById("option_request").value;
				var reason = document.getElementById("option_reason").value;
				if (request == null || request === "" || reason == null || reason === "") {
					wtConfig.nextCustom();
				}
			}
		},
		{
			element: "#step_dates",
			title: "4. Schritt",
			content: "Geben Sie das Von und Bis Datum an",
			placement: "bottom",
			btnNext: wtConfig.btnNext,
			btnBack: wtConfig.btnBack,
			onNext: function () {
				var dateFrom = document.getElementById("dateFrom").value;
				var dateTo = document.getElementById("dateTo").value;
				if (dateFrom == null || dateFrom === "" || dateTo == null || dateTo === "") {
					wtConfig.nextCustom();
				}
			}
		},
		wtConfig.MainSteps[4]
	]
	wtConfig.WebTour.setSteps(steps);
	document.getElementById("start_tour").addEventListener("click", function() {
		document.getElementById("webtour_msg_div").hidden = true;
		wtConfig.WebTour.start();
	});
	document.getElementById("no_tour").addEventListener("click", function() {
		document.getElementById("webtour_msg_div").hidden = true;
	});
}

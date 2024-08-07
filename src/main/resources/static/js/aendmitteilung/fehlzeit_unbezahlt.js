window.onload = init;
function init() {
	// reset chosen location
	document.getElementById("option_location").value = "";
	document.getElementById("change_notice").reset();
	//var _persIndex = -2;
	var _email_to = "aenderungsmitteilung@new-eu.de";
	// Set Date for first load
	setDateToday();
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
	function validateDate(from, to) {
		var valid = false;
		if (from !== "" && to !== "" && to >= from) valid = true;
		else if (from !== "" && to === "" || to !== "" && from === "") valid = true;
		return valid;
	}
	
	function sendEmail() {
		// get form data
		var _prename_elem = document.getElementById("prename");
		var _name_elem = document.getElementById("name");
		var _location_elem = document.getElementById("option_location");
		var _location_text = _location_elem.options[_location_elem.selectedIndex].text;
		var _dateFrom = document.getElementById("dateFrom").value;
		var _dateTo_elem = document.getElementById("dateTo");
		var _dateTo = _dateTo_elem.value;
		var _dateCreate = document.getElementById("dateCreate").value;
		var _comment = document.getElementById("comment_area").value.replaceAll("\n", "\r\n\t");
		var _prename = _prename_elem.value;
		var _name = _name_elem.value;
		var _days = document.getElementById("days").value;
		var _createdBy = document.getElementById("createdBy").value;
		//var _ccEmail = document.getElementById("cc_email").value;
		// create email parts
		var subject = "Änderungsmitteilung unbezahlte Fehlzeit ";
		var ccPart = "";
		//if (_ccEmail !== null && _ccEmail !== "") ccPart = "&cc=" + _ccEmail;
		var body;
		if (_prename !== "" && _name !== "" && _location_text !== "" && _dateFrom !== "" && _dateTo !== "" && _days !== "" && _createdBy !== "") {
			if (validateDate(_dateFrom, _dateTo)) {
				var fullname = _name + ", " + _prename;
				// show success Messages
				var _form_success = document.getElementById("form_success");
				var form_success_bs = new bootstrap.Alert(_form_success);
				form_success_bs.show;
				_form_success.classList.remove("visually-hidden");
				// build subject
				subject += fullname;
				// submit and reset form
				// remove red border
				if (_dateTo_elem.hasAttribute("style")) _dateTo_elem.removeAttribute("style");
				document.getElementById("dateTo_error").classList.add("visually-hidden");
				document.getElementById("change_notice").submit();
				// build body
				body = "\t• Zweigstelle: " + _location_text + "\r\n"
					+ "\t• Name, Vorname: " + fullname + "\r\n"
					+ "\t• Fehltag(e): " + GetLocaleDateString(_dateFrom) + " - " + GetLocaleDateString(_dateTo) + "\r\n"
					+ "\t• Anzahl Fehltag(e): " + _days + "\r\n"
					+ "\t• Bemerkung: " + _comment + "\r\n"
					+ "\t• Erstellt durch: " + _createdBy + "\r\n"
					+ "\t• Erstellt am: " + GetLocaleDateString(_dateCreate) + "\r\n";
				var mailToLink = "mailto:" + _email_to + "?subject=" + encodeURIComponent(subject) + "&body=" + encodeURIComponent(body) + ccPart;
				window.location.href = mailToLink;
			} else {
				_dateTo_elem.setAttribute("style", "border-color: red");
				var _dateTo_error = document.getElementById("dateTo_error");
				var dateTo_error_alert = new bootstrap.Alert(_dateTo_error);
				dateTo_error_alert.show;
				_dateTo_error.classList.remove("visually-hidden");
			}
		}
	}
	
	// add change event to location
	document.getElementById("option_location").addEventListener("change", changeTeleList);
	
	document.getElementById("change_notice").addEventListener("submit", (e) => {
		e.preventDefault();
		sendEmail();
	});
	
	Date.prototype.addDays = function (days) {
    	var date = new Date(this.valueOf());
    	date.setDate(date.getDate() + days);
    	return date;
	};
	
	/**
	 * @param {String} startDate 
	 * @param {String} endDate 
	 * 
	 * @return {int} WorkingDays
	 */
	function getNumWorkDays(startDateStr, endDateStr) {
		var numWorkDays = 0;
		if (startDateStr !== null && startDateStr !== "" && endDateStr !== null && endDateStr !== "") {
			var currentDate = new Date(startDateStr);
			var endDate = new Date(endDateStr);
    		while (currentDate <= endDate) {
		        // Skips Sunday and Saturday
		        if (currentDate.getDay() !== 0 && currentDate.getDay() !== 6) {
		            numWorkDays++;
		        }
        		currentDate = currentDate.addDays(1);
    		}
		}
		return numWorkDays;
	}
	
	document.getElementById("dateTo").addEventListener("change", function() {
		document.getElementById("days").value = getNumWorkDays(document.getElementById("dateFrom").value, this.value);
	});
	
	document.getElementById("dateFrom").addEventListener("change", function() {
		document.getElementById("days").value = getNumWorkDays(this.value, document.getElementById("dateTo").value);
	});
	wtConfig.MainSteps[0].title += "Unbezahlte Fehlzeit"
	wtConfig.MainSteps[3].title = "4" + wtConfig.MainSteps[3].title.substring(1);
	wtConfig.MainSteps[4].title = "5" + wtConfig.MainSteps[4].title.substring(1);
	var steps = [
		wtConfig.MainSteps[0],
		wtConfig.MainSteps[1],
		wtConfig.MainSteps[2],
		{
			element: "#step_dates",
			title: "3. Schritt",
			content: "Geben Sie das Von und Bis Datum an.<br>Falls erforderlich, passen Sie die Anzahl der Fehltage an.",
			placement: "bottom",
			btnNext: wtConfig.btnNext,
			btnBack: wtConfig.btnBack,
			onNext: function () {
				var dateFrom = document.getElementById("dateFrom").value;
				var dateTo = document.getElementById("dateTo").value;
				var days = document.getElementById("days").value;
				if (dateFrom == null || dateFrom === "" || dateTo == null || dateTo === "" || days == null || days === "") {
					wtConfig.nextCustom();
				}
			},
			width: "450px"
		},
		wtConfig.MainSteps[3],
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
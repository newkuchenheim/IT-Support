window.onload = init;
function init() {
	// reset chosen location
	document.getElementById("option_location").value = "";
	document.getElementById("change_notice").reset();
	var _persIndex = -2;
	var _email_to = "aenderungsmitteilung@new-eu.de;leistunstraegerabrechnung@new-eu.de";
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
	
	function sendEmail() {
		// get form data
		var _prename_elem = document.getElementById("prename");
		var _name_elem = document.getElementById("name");
		var _location_elem = document.getElementById("option_location");
		var _location_text = _location_elem.options[_location_elem.selectedIndex].text;
		var _dateFirstDay = document.getElementById("dateFirstDay").value;
		var _optreason = document.getElementById("option_reason").value;
		var _dateCreate = document.getElementById("dateCreate").value;
		var _comment = document.getElementById("comment_area").value.replaceAll("\n", "\r\n\t");
		var _prename = _prename_elem.value;
		var _name = _name_elem.value;
		var _weekHours = document.getElementById("weekHours").value;
		var _optlunchmodel = document.getElementById("option_lunchmodel").value;
		var _baseAmount = document.getElementById("baseAmount").value;
		var _increaseAmount = document.getElementById("increaseAmount").value;
		var _createdBy = document.getElementById("createdBy").value;
		//var _ccEmail = document.getElementById("cc_email").value;
		// create email parts
		var subject = "Änderungsmitteilung Arbeitszeit ";
		var ccPart = "";
		//if (_ccEmail !== null && _ccEmail !== "") ccPart = "&cc=" + _ccEmail;
		var body;
		if (_prename !== "" && _name !== "" && _location_text !== "" && _dateFirstDay !== "" && _createdBy !== "" 
			&& _baseAmount !== "" && _weekHours !== "" && _optlunchmodel !== "") {
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
			body = "\t• Zweigstelle: " + _location_text + "\r\n"
				+ "\t• Name, Vorname: " + fullname + "\r\n"
				+ "\t• Erster Tag: " + GetLocaleDateString(_dateFirstDay) + "\r\n"
				+ "\t• Grund: " + _optreason + "\r\n"
				+ "\t• Wochenstunden: " + _weekHours + "\r\n"
				+ "\t• Mittagessen / Zeitmodell: " + _optlunchmodel + "\r\n"
				+ "\t• Grundbetrag: " + _baseAmount + "\r\n"
				+ "\t• Neuer Steigerungsbetrag: " + _increaseAmount + "\r\n"
				+ "\t• Bemerkung: " + _comment + "\r\n"
				+ "\t• Erstellt durch: " + _createdBy + "\r\n"
				+ "\t• Erstellt am: " + GetLocaleDateString(_dateCreate) + "\r\n";
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
	wtConfig.MainSteps[0].title += "Arbeitszeit"
	wtConfig.MainSteps[3].title = "9" + wtConfig.MainSteps[3].title.substring(1);
	wtConfig.MainSteps[4].title = "10" + wtConfig.MainSteps[4].title.substring(1);
	var steps = [
		wtConfig.MainSteps[0],
		wtConfig.MainSteps[1],
		wtConfig.MainSteps[2],
		{
			element: "#step_firstday",
			title: "3. Schritt",
			content: "Geben Sie den Ersten Tag an.",
			placement: "bottom",
			btnNext: wtConfig.btnNext,
			btnBack: wtConfig.btnBack,
			onNext: function () {
				var dateFirstDay = document.getElementById("dateFirstDay").value;
				if (dateFirstDay == null || dateFirstDay === "") {
					wtConfig.nextCustom();
				}
			}
		},
		{
			element: "#step_reason",
			title: "4. Schritt",
			content: "Wählen Sie einen Grund aus.",
			placement: "bottom",
			btnNext: wtConfig.btnNext,
			btnBack: wtConfig.btnBack,
			onNext: function () {
				var reason = document.getElementById("option_reason").value;
				if (reason == null || reason === "") {
					wtConfig.nextCustom();
				}
			}
		},
		{
			element: "#step_weekhours",
			title: "5. Schritt",
			content: "Geben Sie die Wochenstunden an.",
			placement: "bottom",
			btnNext: wtConfig.btnNext,
			btnBack: wtConfig.btnBack,
			onNext: function () {
				var weekhours = document.getElementById("weekHours").value;
				if (weekhours == null || weekhours === "") {
					wtConfig.nextCustom();
				}
			}
		},
		{
			element: "#step_lunchmodel",
			title: "6. Schritt",
			content: "Wählen Sie das Mittagessen / Zeitmodell aus",
			placement: "bottom",
			btnNext: wtConfig.btnNext,
			btnBack: wtConfig.btnBack,
			onNext: function () {
				var lunchmodel = document.getElementById("option_lunchmodel").value;
				if (lunchmodel == null || lunchmodel === "") {
					wtConfig.nextCustom();
				}
			}
		},
		{
			element: "#step_baseAmount",
			title: "7. Schritt",
			content: "Geben Sie den Grundbetrag an.",
			placement: "bottom",
			btnNext: wtConfig.btnNext,
			btnBack: wtConfig.btnBack,
			onNext: function () {
				var baseAmount = document.getElementById("baseAmount").value;
				if (baseAmount == null || baseAmount === "") {
					wtConfig.nextCustom();
				}
			}
		},
		{
			element: "#step_increaseAmount",
			title: "8. Schritt",
			content: "Geben Sie den Steigerungsbetrag an.",
			placement: "bottom",
			btnNext: wtConfig.btnNext,
			btnBack: wtConfig.btnBack
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
window.onload = init;
function init() {
	// reset chosen location
	document.getElementById("option_location").value = "";
	document.getElementById("change_notice").reset();
	//var _persIndex = -2;
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
	function GetLocaleDateString(date) {
		/*Format Date string yyyy-mm-dd to dd.mm.yyyy*/
		var str_date = "";
		if (date !== "") {
			var _date = new Date(date);
			str_date = _date.toLocaleDateString();
		}
		return str_date;
	}
	function validateCheckout(from, create, checkout) {
		var valid = false;
		if (checkout) {
			var createDateObj = new Date(create);
			var fromDate = new Date(from);
			var maxMonth = fromDate.getMonth();
			var maxDate = 15;
			var maxYear = fromDate.getFullYear();
			var createMonth = createDateObj.getMonth() + 1;
			var createDate = createDateObj.getDate();
			var createYear = createDateObj.getFullYear();
			if (maxMonth == 0) {
				maxMonth = 12;
				maxYear--;
			}
			if (createYear <= maxYear && (createMonth < maxMonth || (createMonth == maxMonth && createDate <= maxDate))) {
				valid = true;
			} else {
				valid = false;
			}
		} else {
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
		var _dateFrom_elem = document.getElementById("dateFrom");
		var _dateFrom = _dateFrom_elem.value;
		var _dateCreate = document.getElementById("dateCreate").value;
		var _checkout = document.getElementById("checkout").checked;
		var _checkout_text = (_checkout ? "ja" : "nein");
		var _comment = document.getElementById("comment_area").value.replaceAll("\n", "\r\n\t\t\t");
		var _prename = _prename_elem.value;
		var _name = _name_elem.value;
		var _optlunchmodel = document.getElementById("option_lunchmodel").value;
		var _createdBy = document.getElementById("createdBy").value;
		//var _ccEmail = document.getElementById("cc_email").value;
		// create email parts
		var subject = "Änderungsmitteilung Essensgeld ";
		var ccPart = "";
		//if (_ccEmail !== null && _ccEmail !== "") ccPart = "&cc=" + _ccEmail;
		var body;
		if (_prename !== "" && _name !== "" && _location_text !== "" && _dateFrom !== "" && _createdBy !== "" 
			&& ((_optlunchmodel !== "" && !_checkout) || _checkout)) {
			if (validateCheckout(_dateFrom, _dateCreate, _checkout)) {
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
				if (_dateFrom_elem.hasAttribute("style")) _dateFrom_elem.removeAttribute("style");
				document.getElementById("checkout_error").classList.add("visually-hidden");
				document.getElementById("change_notice").submit();
				// build body
				body = "\t• Zweigstelle: " + _location_text + "\r\n"
					+ "\t• Name, Vorname: " + fullname + "\r\n"
					+ "\t• Ab Datum: " + GetLocaleDateString(_dateFrom) + "\r\n"
					+ "\t• Abmeldung: " + _checkout_text + "\r\n"
					+ "\t• Anmeldung / Zeitmodell: " + _optlunchmodel + "\r\n"
					+ "\t• Bemerkung: " + _comment + "\r\n"
					+ "\t• Erstellt durch: " + _createdBy + "\r\n"
					+ "\t• Erstellt am: " + GetLocaleDateString(_dateCreate) + "\r\n";
				var mailToLink = "mailto:" + _email_to + "?subject=" + encodeURIComponent(subject) + "&body=" + encodeURIComponent(body) + ccPart;
				window.location.href = mailToLink;
			} else {
				_dateFrom_elem.setAttribute("style", "border-color: red");
				var _checkout_error = document.getElementById("checkout_error");
				var checkout_error_alert = new bootstrap.Alert(_checkout_error);
				checkout_error_alert.show;
				_checkout_error.classList.remove("visually-hidden");
			}
		}
	}
	
	// add change event to location
	document.getElementById("option_location").addEventListener("change", changeTeleList);
	
	// add change event for checkout
	document.getElementById("checkout").addEventListener("change", function() {
		if (this.checked) {
			document.getElementById("div_lunchmodel_sel").hidden = true;
			document.getElementById("option_lunchmodel").required = false;
			document.getElementById("option_lunchmodel").value = "";
		} else {
			document.getElementById("div_lunchmodel_sel").hidden = false;
			document.getElementById("option_lunchmodel").required = true;
		}
	});
	
	document.getElementById("change_notice").addEventListener("submit", (e) => {
		e.preventDefault();
		sendEmail();
	});
	
	var steps = [
		{
			title: "Anleitung Änderungsmitteilung - Mittagessen",
			content: wtConfig.StartText,
			btnNext: { text: wtConfig.NextText, backgroundColor: wtConfig.NextBgColor, textColor: wtConfig.NextTextColor },
			btnBack: { text: wtConfig.CloseText, backgroundColor: wtConfig.CloseBgColor, textColor: wtConfig.CloseTextColor },
			width: wtConfig.StartWidth
		},
		{
			element: "#step_location",
			title: "1. Schritt",
			content: "Wählen Sie Ihren Standort aus.",
			placement: "bottom",
			btnNext: { text: wtConfig.NextText, backgroundColor: wtConfig.NextBgColor, textColor: wtConfig.NextTextColor },
			btnBack: { text: wtConfig.BackText, backgroundColor: wtConfig.BackBgColor, textColor: wtConfig.BackTextColor },
			onNext: function () {
				var location = document.getElementById("option_location").value;
				if (location == null || location === "") {
					nextCustom();
				}
			}
		},
		{
			element: "#step_names",
			title: "2. Schritt",
			content: "Geben Sie den Vor- und Nachnamen der gewünschten Person ein.",
			placement: "bottom",
			btnNext: { text: wtConfig.NextText, backgroundColor: wtConfig.NextBgColor, textColor: wtConfig.NextTextColor },
			btnBack: { text: wtConfig.BackText, backgroundColor: wtConfig.BackBgColor, textColor: wtConfig.BackTextColor },
			onNext: function () {
				if (NamesEmptyOrWrong()) {
					nextCustom();
				}
			},
			width: "450px"
		},
		{
			element: "#step_from",
			title: "3. Schritt",
			content: "Geben Sie das \"Ab\" Datum an.",
			placement: "bottom",
			btnNext: { text: wtConfig.NextText, backgroundColor: wtConfig.NextBgColor, textColor: wtConfig.NextTextColor },
			btnBack: { text: wtConfig.BackText, backgroundColor: wtConfig.BackBgColor, textColor: wtConfig.BackTextColor },
			onNext: function () {
				var dateFrom = document.getElementById("dateFrom").value;
				if (dateFrom == null || dateFrom === "") {
					nextCustom();
				}
			},
			width: "450px"
		},
		{
			element: "#div_lunchmodel_sel",
			title: "4. Schritt",
			content: "Geben Sie das Zeitmodell an.",
			placement: "bottom",
			btnNext: { text: wtConfig.NextText, backgroundColor: wtConfig.NextBgColor, textColor: wtConfig.NextTextColor },
			btnBack: { text: wtConfig.BackText, backgroundColor: wtConfig.BackBgColor, textColor: wtConfig.BackTextColor },
			onNext: function () {
				var lunchmodel = document.getElementById("option_lunchmodel").value;
				if (lunchmodel == null || lunchmodel === "") {
					nextCustom();
				}
			}
		},
		{
			element: "#step_checkout",
			title: "5. Schritt",
			content: "Falls nötig, aktivieren Sie die Abmeldung.",
			placement: "bottom",
			btnNext: { text: wtConfig.NextText, backgroundColor: wtConfig.NextBgColor, textColor: wtConfig.NextTextColor },
			btnBack: { text: wtConfig.BackText, backgroundColor: wtConfig.BackBgColor, textColor: wtConfig.BackTextColor }
		},
		{
			element: "#step_createdBy",
			title: "6. Schritt",
			content: "Geben Sie in diesem Feld Ihren Namen an.",
			placement: "bottom",
			btnNext: { text: wtConfig.NextText, backgroundColor: wtConfig.NextBgColor, textColor: wtConfig.NextTextColor },
			btnBack: { text: wtConfig.BackText, backgroundColor: wtConfig.BackBgColor, textColor: wtConfig.BackTextColor },
			onNext: function () {
				var createdBy = document.getElementById("createdBy").value;
				if (createdBy == null || createdBy === "") {
					nextCustom();
				}
			}
		},
		{
			element: "#send",
			title: "7. Schritt",
			content: "Klicken Sie auf dem Button \"Send Mail\".<br>Es wird eine Outlook Vorlage geöffnet,<br>die Sie dann versenden können.",
			placement: "top",
			btnNext: { text: wtConfig.FinishText, backgroundColor: wtConfig.FinishBgColor, textColor: wtConfig.FinishTextColor },
			btnBack: { text: wtConfig.BackText, backgroundColor: wtConfig.BackBgColor, textColor: wtConfig.ackTextColor }
	}]
	wt.setSteps(steps);
	document.getElementById("start_tour").addEventListener("click", function() {
		document.getElementById("webtour_msg_div").hidden = true;
		wt.start();
	});
	document.getElementById("no_tour").addEventListener("click", function() {
		document.getElementById("webtour_msg_div").hidden = true;
	});
}
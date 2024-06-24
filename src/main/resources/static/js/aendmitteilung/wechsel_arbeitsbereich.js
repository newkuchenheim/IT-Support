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
		_optcostcentre = document.getElementById("option_costcentre");
		// clear option lists
		while (_optcostcentre.firstChild) {
			_optcostcentre.removeChild(_optcostcentre.firstChild);
		}
		_optcostcentre.value = "";
		// add first option
		addCostCentre(_optcostcentre, null, true);
		for (var i = 0; i < _CostCentres.length; i++) {
			if (_CostCentres[i]["location"] !== "" && _CostCentres[i]["number"] < "19301" && (_CostCentres[i]["location"] === location
			|| _CostCentres[i]["location"].includes("BIAP") || _CostCentres[i]["location"] === "Außenarbeitsplätze")) {
				addCostCentre(_optcostcentre, _CostCentres[i], false);
			}
		}
	}
	function changeTeleListAndCC() {
		changeTeleList();
		var location_elem = document.getElementById("option_location");
		var location_text = location_elem.options[location_elem.selectedIndex].text;
		changeCostCentres(location_text);
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
		var _optcostcentre = document.getElementById("option_costcentre").value;
		var _baseAmount = document.getElementById("BaseAmount").value;
		var _increaseAmount = document.getElementById("IncreaseAmount").value;
		var _checkReducedPension = document.getElementById("checkReducedPension").checked;
		var checkReducedPension_Text = (_checkReducedPension ? "ja" : "nein");
		var _optcostunit = document.getElementById("option_costunit").value;
		var _other_costunit = document.getElementById("other_costunit").value;
		var costunit_text = (_other_costunit !== "" ? _other_costunit : _optcostunit);
		var _lunch = document.getElementById("lunch").checked;
		var lunch_Text = (_lunch ? "ja" : "nein");
		var _paymethod = document.querySelector("input[type='radio'][name=paymethods]:checked").value;;
		var paymethod = (_paymethod !== null && _paymethod !== "" ? _paymethod : "");
		var _optlunchmodel = document.getElementById("option_lunchmodel").value;
		var lunchmodel = (_optlunchmodel !== null && _optlunchmodel !== "" ? ": " + _optlunchmodel : "");
		var _dateCreate = document.getElementById("dateCreate").value;
		var _comment = document.getElementById("comment_area").value.replaceAll("\n", "\r\n\t\t\t\t\t\t\t\t     ");
		var _prename = _prename_elem.value;
		var _name = _name_elem.value;
		var _createdBy = document.getElementById("createdBy").value;
		//var _ccEmail = document.getElementById("cc_email").value;
		// create email parts
		var subject = "Änderungsmitteilung Wechsel in Arbeitsbereich ";
		var ccPart = "";
		//if (_ccEmail !== null && _ccEmail !== "") ccPart = "&cc=" + _ccEmail;
		var body;
		if (_prename !== "" && _name !== "" && _location_text !== "" && _dateChange !== "" && _optcostcentre !== "" && costunit_text !== "" && _baseAmount !== "" && _createdBy !== "") {
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
			body = "\t• Zweigstelle:\t\t\t\t\t" + _location_text + "\r\n"
				+ "\t• Name, Vorname:\t\t\t" + fullname + "\r\n"
				+ "\t• Wechseltag:\t\t\t\t      " + GetLocaleDateString(_dateChange) + "\r\n"
				+ "\t• Kostenträger:\t\t\t\t\t" + costunit_text + "\r\n"
				+ "\t• Kostenstelle:\t\t\t\t\t" + _optcostcentre + "\r\n"
				+ "\t• Prüfung\r\n"
				+ "\t   Erwerbsminderungsrente:\t    " + checkReducedPension_Text + "\r\n"
				+ "\t• Grundbetrag:\t\t\t\t\t" + _baseAmount + "\r\n"
				+ "\t• Steigerungsbetrag:\t\t\t  " + _increaseAmount + "\r\n"
				+ "\t• Mittagessen:\t\t\t\t        " + lunch_Text +  "\r\n"
				+ "\t• Zahlungsmethode:\t\t\t  " + paymethod + lunchmodel + "\r\n"
				+ "\t• Bemerkung:\t\t\t\t\t" + _comment + "\r\n"
				+ "\t• Erstellt durch:\t\t\t\t  " + _createdBy + "\r\n"
				+ "\t• Erstellt am:\t\t\t\t\t   " + GetLocaleDateString(_dateCreate) + "\r\n";
			var mailToLink = "mailto:" + _email_to + "?subject=" + encodeURIComponent(subject) + "&body=" + encodeURIComponent(body) + ccPart;
			window.location.href = mailToLink;
		}
	}
	
	// add change event to location
	document.getElementById("option_location").addEventListener("change", changeTeleListAndCC);
	
	document.getElementById("option_costunit").addEventListener("change", function() {
		if (this.value !== "" && this.value.includes("kein")) {
			document.getElementById("div_other_costunit").hidden = false;
		} else {
			document.getElementById("div_other_costunit").hidden = true;
			document.getElementById("other_costunit").value = "";
		}
	});
	
	document.getElementById("lunch").addEventListener("change", function() {
		if (this.checked) {
			document.getElementById("div_paymethod").hidden = false;
			document.getElementById("lunchpay").required = true;
		} else {
			document.getElementById("div_paymethod").hidden = true;
			document.querySelector("input[type='radio'][name=paymethods]:checked").checked = false;
			document.getElementById("lunchpay").required = false;
		}
	});
	
	document.getElementById("lunchpay").addEventListener("change", function() {
		if (this.checked) {
			document.getElementById("div_lunchmodel_sel").hidden = false;
			document.getElementById("option_lunchmodel").required = true;
		}
	});
	
	document.getElementById("stamps").addEventListener("change", function() {
		if (this.checked) {
			document.getElementById("div_lunchmodel_sel").hidden = true;
			document.getElementById("option_lunchmodel").value = "";
			document.getElementById("option_lunchmodel").required = false;
		}
	});
	
	document.getElementById("change_notice").addEventListener("submit", (e) => {
		e.preventDefault();
		sendEmail();
	});
}
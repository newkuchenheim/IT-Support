window.onload = init;
function init() {
	// reset chosen location
	document.getElementById("option_location").value = "";
	document.getElementById("change_notice").reset();
	//var _persIndex = -2;
	var _email_to = "aenderungsmitteilung@new-eu.de";
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
	
	function sendEmail() {
		// get form data
		var _prename_elem = document.getElementById("prename");
		var _name_elem = document.getElementById("name");
		var _location_elem = document.getElementById("option_location");
		var _location_text = _location_elem.options[_location_elem.selectedIndex].text;
		var _dateChange = document.getElementById("dateChange").value;
		var _optcostcentre = document.getElementById("option_costcentre").value;
		var _newlocation_elem = document.getElementById("option_newlocation");
		var _newlocation_text = _newlocation_elem.options[_newlocation_elem.selectedIndex].text;
		var _baseAmount = document.getElementById("BaseAmount").value;
		var _increaseAmount = document.getElementById("IncreaseAmount").value;
		var _lunch = document.querySelector("input[type='radio'][name=lunch]:checked").value;
		var _optlunchmodel = document.getElementById("option_lunchmodel").value;
		var lunchmodel_part = (_lunch.startsWith("j") ? ": " + _optlunchmodel : "");
		var _dateCreate = document.getElementById("dateCreate").value;
		var _comment = document.getElementById("comment_area").value.replaceAll("\n", "\r\n\t");
		var _prename = _prename_elem.value;
		var _name = _name_elem.value;
		var _createdBy = document.getElementById("createdBy").value;
		//var _ccEmail = document.getElementById("cc_email").value;
		// create email parts
		var subject = "Änderungsmitteilung Wechsel Zweigstelle ";
		var ccPart = "";
		//if (_ccEmail !== null && _ccEmail !== "") ccPart = "&cc=" + _ccEmail;
		var body;
		if (_prename !== "" && _name !== "" && _location_text !== "" && _dateChange !== "" && _optcostcentre !== "" && _baseAmount !== "" && _createdBy !== "") {
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
				+ "\t• Wechseltag: " + GetLocaleDateString(_dateChange) + "\r\n"
				+ "\t• Neue Zweigstelle: " + _newlocation_text + "\r\n"
				+ "\t• Neue Kostenstelle: " + _optcostcentre + "\r\n"
				+ "\t• Grundbetrag: " + _baseAmount + "\r\n"
				+ "\t• Steigerungsbetrag: " + _increaseAmount + "\r\n"
				+ "\t• Teilnahme Mittagessen: " + _lunch + lunchmodel_part + "\r\n"
				+ "\t• Bemerkung: " + _comment + "\r\n"
				+ "\t• Erstellt durch: " + _createdBy + "\r\n"
				+ "\t• Erstellt am: " + GetLocaleDateString(_dateCreate) + "\r\n";
			var mailToLink = "mailto:" + _email_to + "?subject=" + encodeURIComponent(subject) + "&body=" + encodeURIComponent(body) + ccPart;
			window.location.href = mailToLink;
		}
	}
	
	// add change event to location
	document.getElementById("option_location").addEventListener("change", changeTeleList);
	document.getElementById("option_newlocation").addEventListener("change", function() {
		changeCostCentres(this.options[this.selectedIndex].text);
	});
	
	document.getElementById("lunchyes").addEventListener("change", function() {
		if (this.checked) {
			document.getElementById("div_lunchmodel_sel").hidden = false;
			document.getElementById("option_lunchmodel").required = true;
		}
	});
	
	document.getElementById("lunchno").addEventListener("change", function() {
		if (this.checked) {
			document.getElementById("div_lunchmodel_sel").hidden = true;
			document.getElementById("option_lunchmodel").required = false;
			document.getElementById("option_lunchmodel").value = "";
		}
	});
	
	document.getElementById("change_notice").addEventListener("submit", (e) => {
		e.preventDefault();
		sendEmail();
	});
	wtConfig.MainSteps[0].title += "Wechsel Zweigstelle";
	wtConfig.MainSteps[3].title = "9" + wtConfig.MainSteps[3].title.substring(1);
	wtConfig.MainSteps[4].title = "10" + wtConfig.MainSteps[4].title.substring(1);
	var steps = [
		wtConfig.MainSteps[0],
		wtConfig.MainSteps[1],
		wtConfig.MainSteps[2],
		{
			element: "#step_dateChange",
			title: "3. Schritt",
			content: "Geben Sie den Wechseltag an.",
			placement: "bottom",
			btnNext: wtConfig.btnNext,
			btnBack: wtConfig.btnBack,
			onNext: function () {
				var dateChange = document.getElementById("dateChange").value;
				if (dateChange == null || dateChange === "") {
					wtConfig.nextCustom();
				}
			},
			width: "450px"
		},
		{
			element: "#step_newlocation",
			title: "4. Schritt",
			content: "Geben Sie den neuen Standort an.",
			placement: "bottom",
			btnNext: wtConfig.btnNext,
			btnBack: wtConfig.btnBack,
			onNext: function () {
				var newlocation = document.getElementById("option_newlocation").value;
				if (newlocation == null || newlocation === "") {
					wtConfig.nextCustom();
				}
			}
		},
		{
			element: "#step_costcentre",
			title: "5. Schritt",
			content: "Geben Sie die neue Kostenstelle an.",
			placement: "bottom",
			btnNext: wtConfig.btnNext,
			btnBack: wtConfig.btnBack,
			onNext: function () {
				var costcentre = document.getElementById("option_costcentre").value;
				if (costcentre == null || costcentre === "") {
					wtConfig.nextCustom();
				}
			}
		},
		{
			element: "#step_baseAmount",
			title: "6. Schritt",
			content: "Geben Sie den Grundbetrag an.",
			placement: "bottom",
			btnNext: wtConfig.btnNext,
			btnBack: wtConfig.btnBack,
			onNext: function () {
				var baseAmount = document.getElementById("BaseAmount").value;
				if (baseAmount == null || baseAmount === "") {
					wtConfig.nextCustom();
				}
			}
		},
		{
			element: "#step_increaseAmount",
			title: "7. Schritt",
			content: "Geben Sie den Steigerungsbetrag an.",
			placement: "bottom",
			btnNext: wtConfig.btnNext,
			btnBack: wtConfig.btnBack
		},
		{
			element: "#step_lunch",
			title: "8. Schritt",
			content: "Geben Sie die Teilnahme für das Mittagessen an und bei ja das Zeitmodell.",
			placement: "bottom",
			btnNext: wtConfig.btnNext,
			btnBack: wtConfig.btnBack,
			onNext: function () {
				var lunch = document.querySelector("input[type='radio'][name=lunch]:checked");
				var lunchmodel = document.getElementById("option_lunchmodel").value;
				if (lunch == null || lunch.value === "" || (lunch.value === "ja" && (lunchmodel == null || lunchmodel === ""))) {
					wtConfig.nextCustom();
				}
			}
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
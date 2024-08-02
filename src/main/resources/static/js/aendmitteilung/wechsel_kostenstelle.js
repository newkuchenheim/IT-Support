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
			if (_CostCentres[i]["location"] !== "" && _CostCentres[i]["number"] < "19301" && (_CostCentres[i]["location"] === location 
			|| _CostCentres[i]["location"].includes("BIAP") || _CostCentres[i]["location"] === "Außenarbeitsplätze")) {
				addCostCentre(_opt_oldcostcentre, _CostCentres[i], false);
				addCostCentre(_opt_newcostcentre, _CostCentres[i], false);
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
		var _comment = document.getElementById("comment_area").value.replaceAll("\n", "\r\n\t");
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
			body = "\t• Zweigstelle: " + _location_text + "\r\n"
				+ "\t• Name, Vorname: " + fullname + "\r\n"
				+ "\t• Wechseltag: " + GetLocaleDateString(_dateChange) + "\r\n"
				+ "\t• Alte Kostenstelle: " + _optoldcostcentre + "\r\n"
				+ "\t• Neue Kostenstelle: " + _optnewcostcentre + "\r\n"
				+ "\t• Bemerkung: " + _comment + "\r\n"
				+ "\t• Erstellt durch: " + _createdBy + "\r\n"
				+ "\t• Erstellt am: " + GetLocaleDateString(_dateCreate) + "\r\n";
			var mailToLink = "mailto:" + _email_to + "?subject=" + encodeURIComponent(subject) + "&body=" + encodeURIComponent(body) + ccPart;
			window.location.href = mailToLink;
		}
	}
	
	// add change event to location
	document.getElementById("option_location").addEventListener("change", changeTeleListAndCC);
	
	document.getElementById("change_notice").addEventListener("submit", (e) => {
		e.preventDefault();
		sendEmail();
	});
	wtConfig.MainSteps[0].title += "Wechsel Kostenstelle";
	wtConfig.MainSteps[3].title = "4" + wtConfig.MainSteps[3].title.substring(1);
	wtConfig.MainSteps[4].title = "5" + wtConfig.MainSteps[4].title.substring(1);
	var steps = [
		wtConfig.MainSteps[0],
		wtConfig.MainSteps[1],
		wtConfig.MainSteps[2],
		{
			element: "#step_costcentres",
			title: "3. Schritt",
			content: "Geben Sie den Wechseltag, die alte und neue Kostenstelle an.",
			placement: "bottom",
			btnNext: wtConfig.btnNext,
			btnBack: wtConfig.btnBack,
			onNext: function () {
				var dateChange = document.getElementById("dateChange").value;
				var oldcostcentre = document.getElementById("option_oldcostcentre").value;
				var newcostcentre = document.getElementById("option_newcostcentre").value;
				if (dateChange == null || dateChange === "" || oldcostcentre == null || oldcostcentre === "" || newcostcentre == null || newcostcentre === "") {
					wtConfig.nextCustom();
				}
			},
			width: "470px"
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
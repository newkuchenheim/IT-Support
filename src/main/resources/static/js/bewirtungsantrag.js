window.onload = init;
function init() {
	const _dateOptions = {
		year: 'numeric',
		month: '2-digit',
		day: '2-digit'
	};
	var email_to = "hauswirtschaft@new-eu.de";
	// reset chosen location
	document.getElementById("khm_location").checked = true;
	document.getElementById("catering_request").reset();
	changeCostCentres("NE.W Kuchenheim");
	/**
	 * set the field dateCreate to today date
	 */
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
	/**
	 * returns current date as locale date string in format dd.mm.yyyy (with starting zeros)
	 * @param {string} date 
	 *
	 */
	function GetLocaleDateString(date) {
		var str_date = "";
		if (date !== "") {
			var _date = new Date(date);
			str_date = _date.toLocaleDateString('de-DE', _dateOptions);
		}
		return str_date;
	}
	/**
	 * add given cost centre to selection list
	 */
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
	/**
	 * change cost centre list to given location one
	 */
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
			if (_CostCentres[i]["location"] !== "" && _CostCentres[i]["number"] < "19301" && _CostCentres[i]["location"] === location) {
				addCostCentre(_optcostcentre, _CostCentres[i], false);
			}
		}
	}
	/**
	 * change email to location kitchen
	 */
	function changeEmail() {
		var location_header = document.getElementById("location_header");
		var location = this.value;
		var location_text = "";
		document.getElementById("name").value = "";
		switch (location) {
			case "kall":
				location_header.innerHTML = "Veranstaltung in Kall";
				email_to = "hw-kall@new-eu.de";
				location_text = "NE.W Kall";
				break;
			case "khm":
				location_header.innerHTML = "Veranstaltung in Kuchenheim";
				email_to = "hauswirtschaft@new-eu.de";
				location_text = "NE.W Kuchenheim";
				break;
			case "uelp":
				location_header.innerHTML = "Veranstaltung in Ülpenich";
				email_to = "kueche@new-eu.de";
				location_text = "NE.W Ülpenich";
				break;
			case "zhm":
				location_header.innerHTML = "Veranstaltung in Zingsheim";
				email_to = "hw-zingsheim@new-eu.de";
				location_text = "NE.W Zingsheim";
				break;
			case "zvw":
				location_header.innerHTML = "Veranstaltung in der Zentralen Verwaltung";
				email_to = "hauswirtschaft@new-eu.de";
				location_text = "NE.W Verwaltung";
				break;
			case "qubi":
				location_header.innerHTML = "Veranstaltung in QuBi Eifel Mechernich";
				email_to = "kueche-qubi@new-eu.de";
				location_text = "QuBi.Eifel";
				break;
			default:
				location_header.innerHTML = "Veranstaltung in Kuchenheim";
				email_to = "hauswirtschaft@new-eu.de";
				location_text = "NE.W Kuchenheim";
		}
		changeCostCentres(location_text);
	}
	/**
	 * create email template with form data and send it to email client
	 */
	function sendEmail() {
		var _name = document.getElementById("name").value;
		var _dateCreate = document.getElementById("dateCreate").value;
		var _dateMeeting_elem = document.getElementById("dateMeeting");
		var _dateMeeting = _dateMeeting_elem.value;
		var _timeStart = document.getElementById("timeStart").value;
		var _reason = document.getElementById("reason").value;
		var _costcentre = document.getElementById("option_costcentre").value;
		var _room = document.getElementById("room").value;
		var _countPerson = document.getElementById("countPerson").value;
		var _countLunch = document.getElementById("countLunch").value;
		var _countBreadrolls = document.getElementById("countBreadrolls").value;
		var _countCookies = document.getElementById("countCookies").value;
		var _comment = document.getElementById("comment").value;
		var _countCoffee = document.getElementById("countCoffee").value;
		var _countTea = document.getElementById("countTea").value;
		var _countMilk = document.getElementById("countMilk").value;
		var _countSugar = document.getElementById("countSugar").value;
		var _countWater = document.getElementById("countWater").value;
		var _countAppleJuice = document.getElementById("countAppleJuice").value;
		var _countLemon = document.getElementById("countLemon").value;
		var _countMultivitamin = document.getElementById("countMultivitamin").value;
		var _commentDrinks = document.getElementById("commentDrinks").value;
		var _countPlate = document.getElementById("countPlate").value;
		var _countCup = document.getElementById("countCup").value;
		var _countJar = document.getElementById("countJar").value;
		var _countForks = document.getElementById("countForks").value;
		var _countKnives = document.getElementById("countKnives").value;
		var _countSpoons = document.getElementById("countSpoons").value;
		var _countCakeLifter = document.getElementById("countCakeLifter").value;
		if (_name !== "" && _dateCreate !== "" && _dateMeeting !== "" && _timeStart !== "" && _reason !== "" && _costcentre !== "" && _room !== "" && _countPerson !== "") {
			if (_dateMeeting >= _dateCreate) {
				//remove red border
				if (_dateMeeting_elem.hasAttribute("style")) _dateMeeting_elem.removeAttribute("style");
				//submit and reset form
				document.getElementById("dateMeetingError").classList.add("visually-hidden");
				document.getElementById("catering_request").submit();
				// show success Messages
				var _form_success = document.getElementById("form_success");
				var form_success_bs = new bootstrap.Alert(_form_success);
				form_success_bs.show;
				_form_success.classList.remove("visually-hidden");
				var subject = "Bewirtungsantrag für den " + GetLocaleDateString(_dateMeeting) + " von " + _name;
				var body = "\t 𝐕𝐞𝐫𝐚𝐧𝐬𝐭𝐚𝐥𝐭𝐮𝐧𝐠𝐬𝐢𝐧𝐟𝐨:\r\n\t• Name: " + _name + "\r\n"
					+ "\t• Erstellt am: " + GetLocaleDateString(_dateCreate) + "\r\n"
					+ "\t• Tag der Veranstaltung: " + GetLocaleDateString(_dateMeeting) + "\r\n"
					+ "\t• Beginn: " + _timeStart + "\r\n"
					+ "\t• Anlass: " + _reason + "\r\n"
					+ "\t• Kostenstelle: " + _costcentre + "\r\n"
					+ "\t• Tagungsraum: " + _room + "\r\n"
					+ "\t• Anzahl Personen: " + _countPerson + "\r\n\r\n"
					+ "\t 𝐄𝐬𝐬𝐞𝐧:\r\n";
					if (_countLunch !== "") body += "\t• Mittagessen: " + _countLunch + "\r\n";
					if (_countBreadrolls !== "") body += "\t• Brötchen: " + _countBreadrolls + "\r\n";
					if (_countCookies !== "") body += "\t• Gebäck: " + _countCookies + "\r\n";
					if (_comment !== "") {
						body += "\t• Bemerkung: " + _comment + "\r\n\r\n";
					} else {
						body += "\r\n";
					}
					body += "\t 𝐆𝐞𝐭𝐫ä𝐧𝐤𝐞:\r\n";
					if (_countCoffee !== "") body += "\t• Kaffee: " + _countCoffee + "\r\n";
					if (_countTea !== "") body += "\t• Tee: " + _countTea + "\r\n";
					if (_countMilk !== "") body += "\t• Milch: " + _countMilk + "\r\n";
					if (_countSugar !== "") body += "\t• Zucker: " + _countSugar + "\r\n";
					if (_countWater !== "") body += "\t• Sprudel: " + _countWater + "\r\n";
					if (_countAppleJuice !== "") body += "\t• Apfelsaft: " + _countAppleJuice + "\r\n";
					if (_countLemon !== "") body += "\t• Limo: " + _countLemon + "\r\n";
					if (_countMultivitamin !== "") body += "\t• Multivitamin: " + _countMultivitamin + "\r\n";
					if (_commentDrinks !== "") {
						body += "\t• Bemerkung: " + _commentDrinks + "\r\n\r\n";
					} else {
						body += "\r\n";
					}
					body += "\t 𝐆𝐞𝐬𝐜𝐡𝐢𝐫𝐫:\r\n";
					if (_countPlate !== "") body += "\t• Teller: " + _countPlate + "\r\n";
					if (_countCup !== "") body += "\t• Tassen: " + _countCup + "\r\n";
					if (_countJar !== "") body += "\t• Becher: " + _countJar + "\r\n";
					if (_countForks !== "") body += "\t• Kuchengabeln: " + _countForks + "\r\n";
					if (_countKnives !== "") body += "\t• Messer: " + _countKnives + "\r\n";
					if (_countSpoons !== "") body += "\t• Löffel: " + _countSpoons + "\r\n";
					if (_countCakeLifter !== "") body += "\t• Tortenheber: " + _countCakeLifter + "\r\n";
					var mailToLink = "mailto:" + email_to + "?subject=" + encodeURIComponent(subject) + "&body=" + encodeURIComponent(body);
					window.location.href = mailToLink;
				} else {
					_dateMeeting_elem.setAttribute("style", "border-color: red");
					var _dateMeetingError = document.getElementById("dateMeetingError");
					var dateMeetingError_alert = new bootstrap.Alert(_dateMeetingError);
					dateMeetingError_alert.show;
					_dateMeetingError.classList.remove("visually-hidden");
				}
		}
	}
	
	// add Change EventListener to all Locations
	var locations = document.getElementsByName("location");
	for (var i = 0; i < locations.length; i++) {
		locations[i].addEventListener("change", changeEmail);
	}
	
	document.getElementById("catering_request").addEventListener("submit", (e) => {
		e.preventDefault();
		sendEmail();
	});
	
	wtConfig.MainSteps[0].title = "Anleitung Bewirtungsantrag";
	wtConfig.MainSteps[4].title = "12" + wtConfig.MainSteps[4].title.substring(1);
	var steps = [
		wtConfig.MainSteps[0],
		{
			element: "#step_location",
			title: "1. Schritt",
			content: "Wählen Sie den Veranstaltungsort aus.<br>Ggf. vorher auf Bewirtungsantrag klicken.",
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
		{
			element: "#step_name",
			title: "2. Schritt",
			content: "Geben Sie Ihren Namen an.",
			placement: "bottom",
			btnNext: wtConfig.btnNext,
			btnBack: wtConfig.btnBack,
			onNext: function () {
				var name = document.getElementById("name").value;
				if (name == null || name === "") {
					wtConfig.nextCustom();
				} else {
					var re = /[a-zA-ZäüößÜÖÄ\-´'éÉ/\s/]+/;
					var full_matched_name = '';
					var i; 
					var matches = name.match(re);
					if (matches !== null) {
						for (i = 0; i < matches.length; i++) {
							full_matched_name += matches[i];
						}
					}
					if (full_matched_name.length < name.length) {
						wtConfig.nextCustom();
					}
				}
			}
		},
		{
			element: "#step_meeting",
			title: "3. Schritt",
			content: "Geben Sie den Tag der Veranstaltung an.<br>Er muss nachdem Erstell Datum sein, damit das Formular am Ende gesendet werden kann.",
			placement: "bottom",
			btnNext: wtConfig.btnNext,
			btnBack: wtConfig.btnBack,
			width: "450px",
			onNext: function () {
				var dateMeeting = document.getElementById("dateMeeting").value;
				if (dateMeeting == null || dateMeeting === "") {
					wtConfig.nextCustom();
				}
			}
		},
		{
			element: "#step_start",
			title: "4. Schritt",
			content: "Geben Sie den Beginn der Veranstaltung an.",
			placement: "bottom",
			btnNext: wtConfig.btnNext,
			btnBack: wtConfig.btnBack,
			onNext: function () {
				var timeStart = document.getElementById("timeStart").value;
				if (timeStart == null || timeStart === "") {
					wtConfig.nextCustom();
				}
			}
		},
		{
			element: "#step_reason",
			title: "5. Schritt",
			content: "Geben Sie den Anlass an.",
			placement: "bottom",
			btnNext: wtConfig.btnNext,
			btnBack: wtConfig.btnBack,
			onNext: function () {
				var reason = document.getElementById("reason").value;
				if (reason == null || reason === "") {
					wtConfig.nextCustom();
				}
			}
		},
		{
			element: "#step_costcentre",
			title: "6. Schritt",
			content: "Wählen Sie die Kostenstelle aus.",
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
			element: "#step_room",
			title: "7. Schritt",
			content: "Geben Sie den Tagungsraum an.",
			placement: "bottom",
			btnNext: wtConfig.btnNext,
			btnBack: wtConfig.btnBack,
			onNext: function () {
				var room = document.getElementById("room").value;
				if (room == null || room === "") {
					wtConfig.nextCustom();
				}
			}
		},
		{
			element: "#step_personcount",
			title: "8. Schritt",
			content: "Geben Sie die Anzahl der Personen an.",
			placement: "bottom",
			btnNext: wtConfig.btnNext,
			btnBack: wtConfig.btnBack,
			onNext: function () {
				var countPerson = document.getElementById("countPerson").value;
				if (countPerson == null || countPerson === "") {
					wtConfig.nextCustom();
				}
			}
		},
		{
			element: "#step_lunch",
			title: "9. Schritt",
			content: "Geben Sie die benötige Menge vom Essen an.",
			placement: "bottom",
			btnNext: wtConfig.btnNext,
			btnBack: wtConfig.btnBack
		},
		{
			element: "#step_drinks",
			title: "10. Schritt",
			content: "Geben Sie die benötige Menge an Getränken an.",
			placement: "bottom",
			btnNext: wtConfig.btnNext,
			btnBack: wtConfig.btnBack
		},
		{
			element: "#step_dishes",
			title: "11. Schritt",
			content: "Geben Sie die benötige Menge an Geschirr an.",
			placement: "bottom",
			btnNext: wtConfig.btnNext,
			btnBack: wtConfig.btnBack
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
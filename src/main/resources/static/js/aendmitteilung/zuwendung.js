window.onload = init;
function init() {
	// define String Format function
	String.prototype.format = function () {
	  var args = arguments;
	  return this.replace(/{([0-9]+)}/g, function (match, index) {
	    return typeof args[index] == 'undefined' ? match : args[index];
	  });
	};
	// reset chosen location
	document.getElementById("option_location").value = "";
	document.getElementById("change_notice").reset();
	// generate description text for vouchers
	function initVoucherDescr() {
		var _voucher_descr = document.getElementById("voucher_descr");
		var voucherGrp1 = "<strong>";
		var voucherGrp2 = "";
		var voucherGrp3 = "<strong>";
		var voucherGrp1Eur = "";
		var voucherGrp3Eur = "";
		var voucherGrpJub_elem = document.createElement("li");
		var JubilarText = "";
		var voucherGrpTrain_elem = document.createElement("li");
		var TraineeText = "";
		for (var i = 0; i < _VoucherReasons.length; i++) {
			if (_VoucherReasons[i]["value"] !== "Jubilare" && !_VoucherReasons[i]["value"].includes("Praktikum")) {
				var strArray = Array.from(_VoucherReasons[i]["description"]);
				var voucherCount = parseInt(strArray[0]);
				if (voucherCount == 3) {
					voucherGrp3 += _VoucherReasons[i]["value"] + ", "
					if (voucherGrp3Eur.length == 0) {
						var voucherParts = _VoucherReasons[i]["description"].split(":");
						voucherGrp3Eur = voucherParts[1].substring(0, voucherParts[1].indexOf(" "));
					}
				} else if (_VoucherReasons[i]["value"] === "Verabschiedung" && voucherGrp2.length == 0) {
					var voucherParts = _VoucherReasons[i]["description"].split(":");
					voucherGrp2 = "<strong>Verabschiedung</strong><br><b>{0}</b> Gutschein(e) in Höhe von <b>{1}</b>,- EUR"
						.format(voucherParts[0].substring(0, voucherParts[0].indexOf(" ")), voucherParts[1].substring(0, voucherParts[1].indexOf(" ")));
				} else {
					voucherGrp1 += _VoucherReasons[i]["value"] + ", ";
					if (voucherGrp1Eur.length == 0) {
						var voucherParts = _VoucherReasons[i]["description"].split(":");
						voucherGrp1Eur = voucherParts[1].substring(0, voucherParts[1].indexOf(" "));
					}
				}
			} else if (_VoucherReasons[i]["value"] === "Jubilare" && JubilarText.length == 0) {
				JubilarText = "<strong>Jubilare:</strong><br>";
				var jubilar_parts = _VoucherReasons[i]["description"].split(";");
				for (var j = 0; j < jubilar_parts.length; j++) {
					var voucherdef = jubilar_parts[j].split(":");
					var years = voucherdef[0].substring(0,voucherdef[0].indexOf(" "));
					var voucherCount = voucherdef[1].substring(0,voucherdef[1].indexOf(" "));
					if (voucherdef[0].includes("je")) {
						JubilarText += "je <b>{0}</b> weitere Jahre <b>{1}</b> Gutschein(e)".format(years, voucherCount);
					} else {
						JubilarText += "nach <b>{0}</b> Jahren <b>{1}</b> Gutschein(e)".format(years, voucherCount);
					}
					if (j < jubilar_parts.length - 1) JubilarText += "<br>";
				}
			} else if (_VoucherReasons[i]["value"].includes("Praktikum") && TraineeText.length == 0) {
				TraineeText = "<strong>Externes Praktikum:</strong><br>";
				var monthparts = _VoucherReasons[i]["description"].split(";");
				for (var j = 0; j < monthparts.length; j++) {
					var voucherdef = monthparts[j].split(":");
					var month = voucherdef[0].substring(0,voucherdef[0].indexOf(" "));
					var voucherCount = voucherdef[1].substring(0,voucherdef[1].indexOf(" "));
					TraineeText += "<b>{0}</b> Praktikumsmonat <b>{1}</b> Gutschein(e)".format(month, voucherCount);
					if (j < monthparts.length - 1) TraineeText += "<br>";
				}
			}
		}
		voucherGrpJub_elem.innerHTML = JubilarText;
		voucherGrpTrain_elem.innerHTML = TraineeText;
		voucherGrp1 = voucherGrp1.substring(0, voucherGrp1.length - 2) + "</strong><br><b>1</b> Gutschein in Höhe von <b>{0}</b>,- EUR".format(voucherGrp1Eur);
		voucherGrp3 = voucherGrp3.substring(0, voucherGrp3.length - 2) + "</strong><br><b>3</b> Gutscheine in Höhe von jeweils <b>{0}</b>,- EUR".format(voucherGrp3Eur);
		var voucherGrp1_elem = document.createElement("li");
		voucherGrp1_elem.innerHTML = voucherGrp1;
		var voucherGrp2_elem = document.createElement("li");
		voucherGrp2_elem.innerHTML = voucherGrp2;
		var voucherGrp3_elem = document.createElement("li");
		voucherGrp3_elem.innerHTML = voucherGrp3;
		_voucher_descr.appendChild(voucherGrp3_elem);
		_voucher_descr.appendChild(voucherGrp2_elem);
		_voucher_descr.appendChild(voucherGrp1_elem);
		_voucher_descr.appendChild(voucherGrpTrain_elem);
		_voucher_descr.appendChild(voucherGrpJub_elem);
	}
	initVoucherDescr();
	var _persIndex = -2;
	var _email_to = "aenderungsmitteilung@new-eu.de";
	var _maxvoucher = 1;
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
	
	function sumVoucher() {
		var arrCount = document.getElementsByName("voucherCount");
		var sum = 0;
		for (var i=0; i < arrCount.length; i++) {
			var value = parseInt(arrCount[i].value);
			if (value && value <= _maxvoucher) {
				sum += value;
			}
		}
		document.getElementById("voucherCountSum").value = sum;
	}
	
	function sendEmail() {
		// get form data
		var _prename_elem = document.getElementById("prename");
		var _name_elem = document.getElementById("name");
		var _location_elem = document.getElementById("option_location");
		var _location_text = _location_elem.options[_location_elem.selectedIndex].text;
		var _dateCreate = document.getElementById("dateCreate").value;
		var _prename = _prename_elem.value;
		var _name = _name_elem.value;
		var _employee = document.getElementById("employee").value;
		var _createdBy = document.getElementById("createdBy").value;
		var _optvouchers = document.getElementsByName("option_voucher");
		var _optreason = document.getElementById("option_reason").value;
		var _voucherCounts = document.getElementsByName("voucherCount");
		var _voucherCountSum = parseInt(document.getElementById("voucherCountSum").value);
		var _ccEmail = document.getElementById("cc_email").value;
		// create email parts
		var subject = "Änderungsmitteilung Zuwendung " + _location_text;
		var ccPart = "";
		if (_ccEmail !== null && _ccEmail !== "") ccPart = "&cc=" + _ccEmail;
		var body;
		if (_prename !== "" && _name !== "" && _optvouchers[0].value !== "" && _optreason !== "" && _voucherCounts[0].value !== "" && _employee !== "" && _createdBy !== "") {
			if (_voucherCountSum && _voucherCountSum > 0 && _voucherCountSum == _maxvoucher) {
				var fullname = _name + ", " + _prename;
				// hide sum error
				document.getElementById("sum_error").classList.add("visually-hidden");
				// show success Messages
				var _form_success = document.getElementById("form_success");
				var form_success_bs = new bootstrap.Alert(_form_success);
				form_success_bs.show;
				_form_success.classList.remove("visually-hidden");
				// build subject
				subject += " " + fullname;
				// build voucher part
				var vouchers = "";
				var tabs = "\t\t\t\t\t\t\t\t\t\t   ";
				for (var i=0; i < _optvouchers.length; i++) {
					if (parseInt(_voucherCounts[i].value) > 0) {
						if (i > 0) vouchers += tabs;
						vouchers += _optvouchers[i].value + ": " + _voucherCounts[i].value + "\r\n";
					}
				}
				vouchers += tabs + "Summe: " + _voucherCountSum;
				document.getElementById("change_notice").submit();
				// build body
				body = "\t• Zweigstelle:\t\t\t\t\t\t\t" + _location_text + "\r\n"
					+ "\t• Name, Vorname:\t\t\t\t\t" + fullname + "\r\n"
					+ "\t• Grund (Urkunde beifügen):\t\t\t " + _optreason + "\r\n"
					+ "\t• Info für FiBu:\t\t\t\t\t\t" + vouchers + "\r\n"
					+ "\t• Verantwortlicher Mitarbeiter\r\n"
					+ "\t (Empfänger) für die Gutscheinausgabe:\t   " + _employee + "\r\n"
					+ "\t• Erstellt durch:\t\t\t\t\t\t" + _createdBy + "\r\n"
					+ "\t• Erstellt am:\t\t\t\t\t\t\t  " + GetLocaleDateString(_dateCreate) + "\r\n";
				var mailToLink = "mailto:" + _email_to + "?subject=" + encodeURIComponent(subject) + "&body=" + encodeURIComponent(body) + ccPart;
				window.location.href = mailToLink;
			} else {
				var _sum_error = document.getElementById("sum_error");
				var sum_error_bs = new bootstrap.Alert(_sum_error);
				sum_error_bs.show;
				_sum_error.classList.remove("visually-hidden");
			}
		}
	}
	
	// show or hide extra voucher after reason change
	document.getElementById("option_reason").addEventListener("change", function() {
		var value_low = this.options[this.selectedIndex].value.toLowerCase();
		var jubilarSel_elem = document.getElementById("jubilar_sel");
		var optjubilar_elem = document.getElementById("option_jubilar");
		var voucherCount_elem = document.getElementById("voucherCount");
		var voucher2_div = document.getElementById("voucher2");
		var voucherCount2_elem = document.getElementById("voucherCount2");
		var optvoucher2_elem = document.getElementById("option_voucher2");
		var voucher3_div = document.getElementById("voucher3");
		var voucherCount3_elem = document.getElementById("voucherCount3");
		var optvoucher3_elem = document.getElementById("option_voucher3");
		if (value_low === "hochzeit" || value_low === "geburt" || value_low.includes("bergang soz.")) {
			// 3 vouchers
			_maxvoucher = 3;
			voucherCount_elem.max = "3";
			voucher2_div.hidden = false;
			voucherCount2_elem.max = "3";
			voucher3_div.hidden = false;
			voucherCount3_elem.max = "3";
			jubilarSel_elem.hidden = true;
			optjubilar_elem.value = "";
			optjubilar_elem.required = false;
		} else  {
			// 1 voucher
			_maxvoucher = 1;
			// reset fields
			voucherCount_elem.max = "1";
			voucher2_div.hidden = true;
			voucherCount2_elem.max = "2";
			voucherCount2_elem.value = "0";
			optvoucher2_elem.value = "";
			voucher3_div.hidden = true;
			voucherCount3_elem.max = "3";
			voucherCount3_elem.value = "0";
			optvoucher3_elem.value = "";
			if (value_low === "jubilare") {
				jubilarSel_elem.hidden = false;
				optjubilar_elem.required = true;
			} else {
				jubilarSel_elem.hidden = true;
				optjubilar_elem.value = "";
				optjubilar_elem.required = false;
			}
		}
		sumVoucher();
	});
	
	document.getElementById("option_jubilar").addEventListener("change", function() {
		var years = this.options[this.selectedIndex].value;
		var voucherCount_elem = document.getElementById("voucherCount");
		var voucher2_div = document.getElementById("voucher2");
		var voucherCount2_elem = document.getElementById("voucherCount2");
		var optvoucher2_elem = document.getElementById("option_voucher2");
		var voucher3_div = document.getElementById("voucher3");
		var voucherCount3_elem = document.getElementById("voucherCount3");
		var optvoucher3_elem = document.getElementById("option_voucher3");
		if (years === "15") {
			// 2 vouchers
			_maxvoucher = 2;
			voucherCount_elem.max = "2";
			voucher2_div.hidden = false;
			voucherCount2_elem.max = "2";
			voucher3_div.hidden = true;
			voucherCount3_elem.max = "3";
			voucherCount3_elem.value = "0";
			optvoucher3_elem.value = "";
		} else {
			// 1 voucher
			_maxvoucher = 1;
			// reset fields
			voucherCount_elem.max = "1";
			voucher2_div.hidden = true;
			voucherCount2_elem.max = "2";
			voucherCount2_elem.value = "0";
			optvoucher2_elem.value = "";
			voucher3_div.hidden = true;
			voucherCount3_elem.max = "3";
			voucherCount3_elem.value = "0";
			optvoucher3_elem.value = "";
		}
		sumVoucher();
	});
	
	// add Change EventListener to all Locations
	/*var locations = document.getElementsByName("location");
	for (var i = 0; i < locations.length; i++) {
		locations[i].addEventListener("change", changeTeleList);
	}*/
	
	// add change event to location
	document.getElementById("option_location").addEventListener("change", changeTeleList);
	
	// add change event to all count fields
	var arrCount = document.getElementsByName("voucherCount");
	for (var i=0; i < arrCount.length; i++) {
		arrCount[i].addEventListener("change", function() {
			var value = parseInt(this.value);
			if (value && value > 0 && value <= _maxvoucher && parseInt(this.id.charAt(this.id.length - 1))) {
				document.getElementById("option_voucher" + this.id.charAt(this.id.length - 1)).required = true;
			} else if (parseInt(this.id.charAt(this.id.length - 1))) {
				document.getElementById("option_voucher" + this.id.charAt(this.id.length - 1)).required = false;
			}
			sumVoucher();
		});
	}
	
	document.getElementById("change_notice").addEventListener("submit", (e) => {
		e.preventDefault();
		sendEmail();
	});
}
window.onload = init;
function init() {
	function setDateTimeToday() {
		var date = new Date();

		var day = date.getDate();
		var month = date.getMonth() + 1;
		var year = date.getFullYear();
		var hours = date.getHours();
		var minutes = date.getMinutes();
		
		if (month < 10) month = "0" + month;
		if (day < 10) day = "0" + day;
		if (hours < 10) hours = "0" + hours;
		if (minutes < 10) minutes = "0" + minutes;
		
		var today = year + "-" + month + "-" + day;
		var current_time = hours + ":" + minutes;
		document.getElementById('date').value = today;
		document.getElementById('time').value = current_time;
	}
	// Set DateTime for first load
	setDateTimeToday();
	function csvToArray(str, delimiter = ",") {
		/*convert CSV String to an Array with header as indizies*/
		// slice from start of text to the first \n index
		// use split to create an array from string by delimiter
		const headers = str.slice(0, str.indexOf("\r\n")).split(delimiter);

		// slice from \n index + 1 to the end of the text
		// use split to create an array of each csv value row
		const rows = str.slice(str.indexOf("\r\n") + 1).split("\r\n");
			
		// Map the rows
		// split values from each row into an array
		// use headers.reduce to create an object
		// object properties derived from headers:values
		// the object passed as an element of the array
		const arr = rows.map(function (row) {
			const values = row.split(delimiter);
			// console.log(values);
			if (values.length > 2 && (values[2].includes("new-eu.de") || values[2].includes("newjob-eu.de")|| values[2].includes("eulog.org"))) {
				const el = headers.reduce(function (object, header, index) {
					object[header] = values[index];
					return object;
				}, {});
				return el;
			}
		});

		// return the array
		return arr;
	}
	// create Array from csv
	var persons;
	function readCSVFile() {
		var rawFile = new XMLHttpRequest();
		rawFile.open("GET", getFilePath(), true);
		rawFile.setRequestHeader("Cache-Control", "no-cache");
		rawFile.setRequestHeader("Pragma", "no-cache");
		rawFile.setRequestHeader("If-Modified-Since", "Sat, 1 Jan 2000 00:00:00 GMT");
		rawFile.onreadystatechange = function () { 
			if (rawFile.readyState === 4) {
				if (rawFile.status === 200 || rawFile.satus === 0) {
					//console.log(rawFile.responseText);
					persons = csvToArray(rawFile.responseText);
				}
			}
		}
		rawFile.send(null);
	}
	function getFilePath() {
		var location = document.querySelector("input[type='radio'][name=location]:checked").value;
		return "../../" + location + "_telelist.csv";
	}
	readCSVFile();
	function autocomplete(inp, firstCall) {
		function inputEvent() {
			var parent, child, val = this.value;
			/*close any already open lists of autocompleted values*/
			closeAllLists();
			currentFocus = -1;
			parent = document.createElement("DIV");
			parent.setAttribute("id", this.id + "autocomplete-list");
			parent.setAttribute("class", "autocomplete-items");
			this.parentNode.appendChild(parent);
			var max_display = 0;
			for (i = 0; i < persons.length; i++) {
				/*check if the value is part of lastname*/
				if (persons[i] !== undefined) {
					// break loop after 15 names
					if (max_display > 15) break;
					var name = persons[i]["Name"].replaceAll("\"","");
					var fullname = persons[i]["Vorname"].replaceAll("\"","") + " " + name;
					if (name.toUpperCase().includes(val.toUpperCase())) {
						child = document.createElement("DIV");
						/*Set Item Text*/
						child.innerHTML = fullname;
						/*insert a input field that will hold the current person name item's value*/
						child.innerHTML += "<input type='hidden' value='" + fullname + "'>";
						child.addEventListener("click", function() {
							inp.value = this.getElementsByTagName("input")[0].value;
							closeAllLists();
						});
						parent.appendChild(child);
						max_display++;
					}
				}
			}
		}
		// remove all EventListener if is the second call
		if (!firstCall) {
			inp.removeEventListener("input", inputEvent);
			inp.removeEventListener("keydown", keyNavigation);
		}
		var currentFocus;
		inp.addEventListener("input", inputEvent);
		inp.addEventListener("keydown", keyNavigation = function(e) {
			var autolist = document.getElementById(this.id + "autocomplete-list");
			if (autolist) autolist = autolist.getElementsByTagName("div");
			if (e.keyCode == 40) {
				currentFocus++;
				addActive(autolist);
			} else if (e.keyCode == 38) {
				currentFocus--;
				addActive(autolist);
			} else if (e.keyCode == 13) {
				e.preventDefault();
				if (currentFocus > -1) {
					if (autolist) autolist[currentFocus].click();
				}
			}
		});
		function addActive(item) {
			/*a function to classify an item as active*/
			if (!item) return false;
			/*remove the "active" class on all items*/
			removeActive(item);
			if (currentFocus >= item.length) currentFocus = 0;
			if (currentFocus < 0) currentFocus = (item.length - 1);
			item[currentFocus].classList.add("autocomplete-active");
		}
		function removeActive(item) {
			for (var i = 0; i < item.length; i++) {
				item[i].classList.remove("autocomplete-active");
			}
		}
		function closeAllLists(elmnt) {
			/*close all autocomplete lists in the document, except the one passed as an argument*/
			var items = document.getElementsByClassName("autocomplete-items");
			for (var i = 0; i < items.length; i++) {
				if (elmnt != items[i] && elmnt != inp) {
					items[i].parentNode.removeChild(items[i]);
				}
			}
		}
		/*execute a function when someone clicks in the document*/
		document.addEventListener("click", function(e) {
			closeAllLists(e.target);
			// remove required field if company or name is filled
			if (document.getElementById("call_name").value !== "") document.getElementById("call_company").required = false;
			else document.getElementById("call_company").required = true;
			if (document.getElementById("call_company").value !== "") document.getElementById("call_name").required = false;
			else document.getElementById("call_name").required = true;
			// set number as required if call back is checked
			if (document.getElementById("call_back").checked) document.getElementById("call_number").required = true;
			else document.getElementById("call_number").required = false;
		});
	}
	function changeAutoCompleteList() {
		var note_for = document.getElementById("note_for");
		note_for.value = "";
		readCSVFile();
		autocomplete(note_for, false);
	}
	autocomplete(document.getElementById("note_for"), true);
	function GetLocaleDateString(date) {
		/*Format Date string yyyy-mm-dd to dd.mm.yyyy*/
		var _date = new Date(date);
		var day = _date.getDate();
		var month = _date.getMonth() + 1;
		var year = _date.getFullYear();
		
		if (month < 10) month = "0" + month;
		if (day < 10) day = "0" + day;
		return day + "." + month + "." + year;
	}
	function validateNumber(number) {
		/*check if number only contains numbers or - and /*/
		const re = /[0-9\/\+\-]{2,13}/;
		var valid = false;
		var full_matched_str = "";
		if (number !== "") {
			var matches = number.match(re);
			if (matches !== null) {
				for (i = 0; i < matches.length; i++) {
					full_matched_str += matches[i];
				}
				if (full_matched_str.length === number.length) valid = true;
			}
		} else valid = true;
		return valid;
	}
		
	function sendEmail() {
		var _call_name = document.getElementById("call_name").value;
		var _call_company = document.getElementById("call_company").value;
		var _call_reason = document.getElementById("call_reason").value;
		var _date = document.getElementById("date").value;
		var _time = document.getElementById("time").value;
		var _note_for = document.getElementById("note_for").value;
		var _call_number = document.getElementById("call_number").value;
		var email_to = "";
		var subject = "☎ - ";
		var call_back = false;
		if (document.getElementById("call_back").checked) call_back = true;
		var call_later = false;
		if (document.getElementById("call_later").checked) call_later = true;
		var body;
		if ((_call_name !== "" || _call_company !== "") && _note_for !== "") {
			// validate note_for
			var note_for_valid = false;
			var fullname_upp;
			for (i = 0; i < persons.length; i++) {
				if (persons[i] !== undefined) {
				fullname_upp = (persons[i]["Vorname"].replaceAll("\"","") + " " + persons[i]["Name"].replaceAll("\"","")).toUpperCase();
					if (fullname_upp.includes(_note_for.toUpperCase())) {
						email_to = persons[i]["E-Mail"];
						note_for_valid = true;
						break;
					}
				}
			}
				
			var num_valid = validateNumber(_call_number);
			if (num_valid) {
				if (document.getElementById("call_number").hasAttribute("style")) document.getElementById("call_number").removeAttribute("style");
				//document.getElementById("num_error").classList.add("visually-hidden");
			} else {
				document.getElementById("call_number").setAttribute('style', 'border-color: red');
				//document.getElementById("num_error").classList.remove("visually-hidden");
			}
			if (note_for_valid && num_valid) {
				// remove red border
				if (document.getElementById("note_for").hasAttribute("style")) document.getElementById("note_for").removeAttribute("style");
				// submit and reset form
				document.getElementById("name_error").classList.add("visually-hidden");
				document.getElementById("calling_note").submit();
				// show success Messages
				var _form_success = document.getElementById("form_success");
				var form_success_bs = new bootstrap.Alert(_form_success);
				form_success_bs.show;
				_form_success.classList.remove("visually-hidden");
				//document.getElementById("calling_note").reset();
				setDateTimeToday();
				// build subject
				if (_call_company !== "" && _call_name !== "") subject += _call_name + " " + _call_company;
				else if (_call_name !== "" && _call_company === "") subject += _call_name;
				else subject += _call_company;
				if (call_back) subject += " - Zurückrufen";
				else if (call_later) subject += " - Meldet sich später";
				// Build body
				body = "\t• Rückrufnummer: " + _call_number + "\r\n"
					+ "\t• Datum / Uhrzeit: " + GetLocaleDateString(_date) + " " + _time + "\r\n"
					+ "\t• Grund des Anrufes: " + _call_reason;
				var mailToLink = "mailto:" + email_to + "?subject=" + encodeURIComponent(subject) + "&body=" + encodeURIComponent(body);
				window.location.href = mailToLink;
			} else {
				document.getElementById("note_for").setAttribute('style', 'border-color: red');
				// alert("Der angegebene Name ist nicht in der Liste!");
				var _name_error = document.getElementById("name_error");
				var _name_error_toast_el = document.getElementById("name_error_toast");
				var name_error_alert = new bootstrap.Alert(_name_error);
				_name_error.classList.remove("visually-hidden");
			}
		} else {
			if (_call_name === "" && _call_company === "") {
				document.getElementById("call_name").setAttribute('style', 'border-color: coral');
				document.getElementById("call_company").setAttribute('style', 'border-color: coral');
			} 
			if (_note_for === "") document.getElementById("note_for").setAttribute('style', 'border-color: coral');
		}
	}
	document.getElementById("calling_note").addEventListener("submit", (e) => {
		e.preventDefault();
		sendEmail();
	});
	// remove required field if company or name is filled
	document.getElementById("call_name").addEventListener("change", function(e) {
		if (e.value !== "") document.getElementById("call_company").required = false;
		else document.getElementById("call_company").required = true;
	});
	document.getElementById("call_company").addEventListener("change", function(e) {
		if (e.value !== "") document.getElementById("call_name").required = false;
		else document.getElementById("call_name").required = true;
	});
	// add Change EventListener to all Locations
	var locations = document.getElementsByName("location");
	for (var i = 0; i < locations.length; i++) {
		locations[i].addEventListener("change", changeAutoCompleteList);
	}
}
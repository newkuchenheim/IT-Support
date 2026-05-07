window.onload = init;
function init() {
	// create const table head elements
	const _tableHeadElem = document.createElement("THEAD");
	const _tableHeadRow = document.createElement("TR");
	var _tableHeadCol = document.createElement("TH");
	_tableHeadCol.innerText = "Personalnummer";
	_tableHeadRow.appendChild(_tableHeadCol);
	_tableHeadCol = document.createElement("TH");
	_tableHeadCol.innerText = "Vorname";
	_tableHeadRow.appendChild(_tableHeadCol);
	_tableHeadCol = document.createElement("TH");
	_tableHeadCol.innerText = "Nachname";
	_tableHeadRow.appendChild(_tableHeadCol);
	_tableHeadCol = document.createElement("TH");
	_tableHeadCol.innerText = "E-Mail";
	_tableHeadRow.appendChild(_tableHeadCol);
	_tableHeadElem.appendChild(_tableHeadRow);
	
	function createResultTable() {
		var resultViewElem = document.getElementById("resultView");
		// clear last result
		while (resultViewElem.firstChild) {
			resultViewElem.removeChild(resultViewElem.firstChild);
		}
		var persId = document.getElementById("persId").value;
		var lastname = document.getElementById("lastname").value;
		
		var PersonsTmp = null;
		if (persId !== "" && lastname !== "") {
			var lastnameUpp = lastname.toUpperCase();
			PersonsTmp = new Array();
			_Persons.forEach((Person) => {
				// add current Person only to result Persons if search conditions matched
				if (Person.persId == persId && Person.lastname.toUpperCase() == lastnameUpp) {
						PersonsTmp.push(Person);
				}
			});
		}
		// create result info text or table with founded Persons
		if (PersonsTmp !== null && PersonsTmp.length > 0) {
			// create table construct
			var tableElem = document.createElement("TABLE");
			tableElem.classList.add("table", "table-hover", "table-striped", "p-3");
			tableElem.appendChild(_tableHeadElem);
			var tableBodyElem = document.createElement("TBODY");
			PersonsTmp.forEach((item) => {
				var rowElem = document.createElement("TR");
				// add persid
				var colElem = document.createElement("TD");
				colElem.innerText = item.persId;
				rowElem.appendChild(colElem);
				// add firstname
				colElem = document.createElement("TD");
				colElem.innerText = item.firstname;
				rowElem.appendChild(colElem);
				// add lastname
				colElem = document.createElement("TD");
				colElem.innerText = item.lastname;
				rowElem.appendChild(colElem);
				// add email
				colElem = document.createElement("TD");
				colElem.innerText = item.email;
				rowElem.appendChild(colElem);
				// add row to table body element
				tableBodyElem.appendChild(rowElem);
			});
			// add body to table
			tableElem.appendChild(tableBodyElem);
			// add whole table to result div element
			resultViewElem.appendChild(tableElem);
		} else {
			var noResultElem = document.createElement("DIV");
			noResultElem.classList.add("alert", "alert-primary");
			var noResultChild = document.createElement("P");
			noResultChild.classList.add("text-wrap", "fst-normal");
			noResultChild.innerText = "Keine Person gefunden. Bitte ändern Sie Ihre Angaben.";
			noResultElem.appendChild(noResultChild);
			resultViewElem.appendChild(noResultElem);
		}
	}
	
	function triggerSearchButton(event) {
		// if the user press the "Enter" key
		if (event.key === "Enter") {
    		// Cancel the default action, if needed
		    event.preventDefault();
		    // Trigger the button element with a click
		    document.getElementById("searchBtn").click();
  		}
	}
	function refreshPage() {
		window.location.href=window.location.href;
	}
	document.getElementById("resetBtn").addEventListener("click", refreshPage);
	document.getElementById("searchBtn").addEventListener("click", createResultTable);

	// add key event to input fields
	document.getElementById("lastname").addEventListener("keypress", function(e) {
		triggerSearchButton(e);
	});
	wtConfig.MainSteps[0].title = "Anleitung Kostenstellenplan";
	var steps = [
		wtConfig.MainSteps[0],
		{
			element: "#step_filter",
			title: "1. Schritt",
			content: "Geben Sie die Personalnummer und Ihren Nachnamen an.",
			placement: "bottom",
			btnNext: wtConfig.btnNext,
			btnBack: wtConfig.btnBack,
			width: "500px"
		},
		{
			element: "#searchBtn",
			title: "2. Schritt",
			content: "Klicken Sie auf den <b>Suchen</b> - Button.<br>Die passende Person wird dann aufgelistet.",
			placement: "bottom",
			btnNext: wtConfig.btnNext,
			btnBack: wtConfig.btnBack,
			width: "500px"
		},
		{
			element: "#resetBtn",
			title: "3. Schritt",
			content: "Mit dem Button <b>Filter zurücksetzen</b> können Sie alle Filter löschen und neu ausfüllen.",
			placement: "bottom",
			btnNext: wtConfig.btnFinish,
			btnBack: wtConfig.btnBack
		}
	];
	wtConfig.WebTour.setSteps(steps);
	document.getElementById("start_tour").addEventListener("click", function() {
		document.getElementById("webtour_msg_div").hidden = true;
		wtConfig.WebTour.start();
	});
	document.getElementById("no_tour").addEventListener("click", function() {
		document.getElementById("webtour_msg_div").hidden = true;
	});
}

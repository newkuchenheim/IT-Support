window.onload = init;
function init() {
	// create const table head elements
	const _tableHeadElem = document.createElement("THEAD");
	const _tableHeadRow = document.createElement("TR");
	var _tableHeadCol = document.createElement("TH");
	_tableHeadCol.innerText = "Kostenstelle";
	_tableHeadRow.appendChild(_tableHeadCol);
	_tableHeadCol = document.createElement("TH");
	_tableHeadCol.innerText = "Standort";
	_tableHeadRow.appendChild(_tableHeadCol);
	_tableHeadCol = document.createElement("TH");
	_tableHeadCol.innerText = "Bezeichnung";
	_tableHeadRow.appendChild(_tableHeadCol);
	_tableHeadCol = document.createElement("TH");
	_tableHeadCol.innerText = "Bezeichnung 1";
	_tableHeadRow.appendChild(_tableHeadCol);
	_tableHeadElem.appendChild(_tableHeadRow);
	
	function createResultTable() {
		var resultViewElem = document.getElementById("resultView");
		var locationTypeElem = document.getElementById("location_type");
		var locationTypeValue = locationTypeElem.options[locationTypeElem.selectedIndex].value;
		// clear last result
		while (resultViewElem.firstChild) {
			resultViewElem.removeChild(resultViewElem.firstChild);
		}
		var ksValue = document.getElementById("ks").value;
		var locationElem;
		var locationValue = "";
		if (locationTypeValue !== null && locationTypeValue === "freetext") {
			locationElem = document.getElementById("location_freetext");
			locationValue = locationElem.value;
		} else if (locationTypeValue !== null && locationTypeValue === "select") {
			locationElem = document.getElementById("location_sel");
			locationValue = locationElem.options[locationElem.selectedIndex].value;
		}
		var CostCentresTmp = null;
		if (ksValue !== "" || locationValue !== "") {
			var locationValUpp = locationValue.toUpperCase();
			var ksValUpp = ksValue.toUpperCase();
			CostCentresTmp = new Array();
			_CostCentres.forEach((CostCentre) => {
				// add current CostCentre only to result CostCentres if search conditions matched
				if ((ksValUpp !== ""  && CostCentre["label1"].toUpperCase().includes(ksValUpp) 
					&& locationValUpp !== "" && CostCentre["location"].toUpperCase().includes(locationValUpp))
					|| (ksValUpp === "" && locationValUpp !== "" && CostCentre["location"].toUpperCase().includes(locationValUpp))
					|| (locationValUpp === "" && ksValUpp !== "" && CostCentre["label1"].toUpperCase().includes(ksValUpp))) {
						CostCentresTmp.push(CostCentre);
					}
			});
		} else {
			CostCentresTmp = _CostCentres;
		}
		// create result info text or table with founded CostCentres
		if (CostCentresTmp !== null && CostCentresTmp.length > 0) {
			// create table construct
			var tableElem = document.createElement("TABLE");
			tableElem.classList.add("table", "table-hover", "table-striped", "p-3");
			tableElem.appendChild(_tableHeadElem);
			var tableBodyElem = document.createElement("TBODY");
			CostCentresTmp.forEach((item) => {
				var rowElem = document.createElement("TR");
				// add number
				var colElem = document.createElement("TD");
				colElem.innerText = item["number"];
				rowElem.appendChild(colElem);
				// add location
				colElem = document.createElement("TD");
				colElem.innerText = item["location"];
				rowElem.appendChild(colElem);
				// add label
				colElem = document.createElement("TD");
				colElem.innerText = item["label"];
				rowElem.appendChild(colElem);
				// add label 1
				colElem = document.createElement("TD");
				colElem.innerText = item["label1"];
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
			noResultChild.innerText = "Keine Kostenstellen gefunden. Bitte ändern Sie Ihre Angaben.";
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
	
	document.getElementById("searchBtn").addEventListener("click", createResultTable);
	document.getElementById("location_type").addEventListener("change", function() {
		switch(this.options[this.selectedIndex].value) {
			case "freetext":
				document.getElementById("div_location_freetext").hidden = false;
				document.getElementById("div_location_sel").hidden = true;
				break;
			case "select":
				document.getElementById("div_location_freetext").hidden = true;
				document.getElementById("div_location_sel").hidden = false;
				break;
			default:
				document.getElementById("div_location_freetext").hidden = true;
				document.getElementById("div_location_sel").hidden = true;
		}
	});
	// add key event to input fields
	document.getElementById("ks").addEventListener("keypress", function(e) {
		triggerSearchButton(e);
	});
	document.getElementById("location_freetext").addEventListener("keypress", function(e) {
		triggerSearchButton(e);
	});
	document.getElementById("location_sel").addEventListener("keypress", function(e) {
		triggerSearchButton(e);
	});
}

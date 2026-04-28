window.onload = init;
function init() {
	document.getElementById("option_address").value = "";
	function addCostCentre(parent, CostCentre, first) {
		var child = document.createElement("option");
		if (first) {
			child.innerHTML = "Bitte Ihre Kostenstelle auswählen";
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
	
	function changeCostCentres() {
		var address_elem = document.getElementById("option_address");
		var address_text = address_elem.options[address_elem.selectedIndex].text;
		if (address_text !== "") {
			_optcostcentre = document.getElementById("option_costcentre");
			
			// clear option lists
			while (_optcostcentre.firstChild) {
				_optcostcentre.removeChild(_optcostcentre.firstChild);
			}
			_optcostcentre.value = "";
			// add first option
			addCostCentre(_optcostcentre, null, true);
			for (var i = 0; i < _CostCentres.length; i++) {
				if (address_text.includes("Kuchenheim")) {
					if (address_text.includes("Lisztstr") && _CostCentres[i]["location"] == "NE.W Verwaltung") {
						addCostCentre(_optcostcentre, _CostCentres[i], false);
					} else if (!address_text.includes("Lisztstr")) {
						addCostCentre(_optcostcentre, _CostCentres[i], false);
					}
				} else if (_CostCentres[i]["location"] !== "" && _CostCentres[i]["number"] < "19301" && address_text.includes(_CostCentres[i]["location"].substring(5))) {
					addCostCentre(_optcostcentre, _CostCentres[i], false);
				}
			}
		}
	}
	
	// add change event to location
	document.getElementById("option_address").addEventListener("change", changeCostCentres);
}
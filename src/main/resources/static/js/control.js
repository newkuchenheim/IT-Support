function showVertreter(){
	
	//first deactivate div_vetreter
	document.querySelector("#div_vertreter_1").hidden = true;
	document.querySelector("#div_vertreter_2").hidden = true;
	
	var is_vertreter = document.querySelector("#isvertreter").checked;
	if(is_vertreter){
		document.querySelector("#div_vertreter_1").hidden = false;
		document.querySelector("#div_vertreter_2").hidden = false;
	}
	
}

function dateFromLimit(){
	
	//first deactivate div_vetreter
	let _fromtf = document.getElementById("fromtf");
	let _totf = document.getElementById("totf");
	
	let _date = new Date();
	_date.setDate(_date.getDate() - 14);
	_fromtf.min = _date.toISOString().split("T")[0];

	_totf.min = _date.toISOString().split("T")[0];
	
}


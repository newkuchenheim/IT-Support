/**
 * Button event: calculate time difference for hilfdstool site of "Zeiterfassung"
 */


function timeCalculationAction(){
	var timef_input = document.getElementById("timeFrom").value;
	var timet_input = document.getElementById("timeTo").value;
	
	let _diff = diff(timef_input, timet_input);
	//console.log(_diff); 
	
	if(_diff === "Fehler"){
		alert("Bitte kontrollieren Sie Ihre Eingabe der Uhrzeit!");
		
	} else{
		let std_nv_diff = _diff.split(':')[0]; 
		let min_nv_diff = _diff.split(':')[1];
		let _nv_diff = std_nv_diff.toString()+","+min_nv_diff.toString();
			
		document.getElementById("time_diff_echtzeit").innerHTML = "<b>Stunden - Differenz in Echtzeit: </b>" + _diff;
		document.getElementById("time_diff_novatime").innerHTML = "<b>Stunden - Differenz bei Novatime: </b>"+ _nv_diff;	
		
		document.getElementById("div_message").style.display = "block";
	}
	setTimeout(function(){
		window.location.reload(1);
	}, 20000);
}

function diff(start, end) {
    start = start.split(":");
    end = end.split(":");
    var startDate = new Date(0, 0, 0, start[0], start[1], 0);
    var endDate = new Date(0, 0, 0, end[0], end[1], 0);
    var diff = endDate.getTime() - startDate.getTime();
    var hours = Math.floor(diff / 1000 / 60 / 60);
    diff -= hours * 1000 * 60 * 60;
    var minutes = Math.floor(diff / 1000 / 60);

    // If using time pickers with 24 hours format, add the below line get exact hours
    if (hours < 0){
       //hours = hours + 24;
       //alert("Bitte kontrollieren Sie Ihre Eingabe der Uhrzeit!");
      	return "Fehler";
	} 
	
	return (hours <= 9 ? "0" : "") + hours + ":" + (minutes <= 9 ? "0" : "") + minutes;
    	
}
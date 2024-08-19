window.onload = init;
function init() {
	wtConfig.MainSteps[0].title += "Feedback";
	var steps = [
		wtConfig.MainSteps[0],
		wtConfig.MainSteps[1],
		wtConfig.MainSteps[2],
		{
			element: "#step_feedback",
			title: "3. Schritt",
			content: "Füllen Sie das Feedbackfeld aus.",
			placement: "bottom",
			btnNext: wtConfig.btnNext,
			btnBack: wtConfig.btnBack,
			onNext: function () {
				var feedback = document.getElementById("feedback_area").value;
				if (feedback == null || feedback === "") {
					wtConfig.nextCustom();
				}
			},
			width: "450px"
		},
		{
			element: "#send",
			title: "4. Schritt",
			content: "Klicken Sie auf dem Button \"Senden\".<br>Ihr Feedback wird dann direkt an uns gesendet.",
			placement: "top",
			btnNext: wtConfig.btnFinish,
			btnBack: wtConfig.btnBack
	}]
	wtConfig.WebTour.setSteps(steps);
	document.getElementById("start_tour").addEventListener("click", function() {
		document.getElementById("webtour_msg_div").hidden = true;
		wtConfig.WebTour.start();
	});
	document.getElementById("no_tour").addEventListener("click", function() {
		document.getElementById("webtour_msg_div").hidden = true;
	});
}
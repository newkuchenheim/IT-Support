class WebTourConfig {
	constructor() {
		this.NextText = 'Nächste &#8594';
		this.NextTextColor = '#fff';
		this.NextBgColor = '';
		this.BackText = '&#8592 Zurück';
		this.BackTextColor = '#555';
		this.BackBgColor = '#efefef';
		this.CloseText = 'Schließen';
		this.CloseBgColor = '#dc3545';
		this.CloseTextColor = '#fff';
		this.FinishText = 'Fertig';
		this.FinishBgColor = '#198754';
		this.FinishTextColor = '#fff';
		this.StartWidth = '600px';
		this.StartText = 'Willkommen.<br>Alle <b>farblich</b> markierten Felder sind Plficht- oder Falschangaben.<br>'
						+ 'Erst nach der <b>Eingabe</b>, können Sie den nächsten Schritt ausführen.<br>'
						+ 'Sie können die <b>Schritte</b> mit der <b>Maus</b> oder den <b>Pfeiltasten</b> bedienen.<br>Sie können die Anleitung mit <b>\"ESC\"</b> jederzeit beenden';
	}
}
const wtConfig = new WebTourConfig();
const wt = new WebTour({width: '400px'});

function nextCustom() {
	wt.isPaused = true;
	wt.showLoader();
	setTimeout(() => {
		wt.isPaused = false;
		document.querySelector('.wt-loader').remove();
	}, 10000);
}

function NamesEmptyOrWrong() {
	var isEmptyOrWrong = false;
	var prename = document.getElementById("prename").value;
	var name = document.getElementById("name").value;
	if (prename == null || prename === "" || name == null || name === "") {
		isEmptyOrWrong = true;
	} else {
		var re = /[a-zA-ZäüößÜÖÄ\-´'éÉ/\s/]+/;
		var full_matched_prename = "";
		var full_matched_name = "";
		if (prename !== null && prename !== "") {
			var matches = prename.match(re);
			if (matches !== null) {
				for (i = 0; i < matches.length; i++) {
					full_matched_prename += matches[i];
				}
			}
		} 
		if (name !== null && name !== "") {
			var matches = name.match(re);
			if (matches !== null) {
				for (i = 0; i < matches.length; i++) {
					full_matched_name += matches[i];
				}
			}
		}
		if (full_matched_prename.length < prename.length || full_matched_name.length < name.length) {
			isEmptyOrWrong = true;
		}
			
	}
	return isEmptyOrWrong;
}
class WebTourConfig {
	constructor() {
		this.WebTour = new WebTour({width: '400px'});
		this.btnNext = { text: 'Nächste &#8594', backgroundColor: '', textColor: '' }; // empty Colors for default one
		this.btnBack = { text: '&#8592 Zurück', backgroundColor: '', textColor: '' };
		this.btnClose = { text: 'Schließen', backgroundColor: '#dc3545', textColor: '#fff' };
		this.btnFinish = { text: 'Fertig', backgroundColor: '#198754', textColor: '#fff'};
		this.StartWidth = '600px';
		this.StartText = 'Willkommen.<br>Alle <b>farblich</b> markierten Felder sind Plficht- oder Falschangaben.<br>'
						+ 'Erst nach der <b>Eingabe</b>, können Sie den nächsten Schritt ausführen.<br>'
						+ 'Sie können die <b>Schritte</b> mit der <b>Maus</b> oder den <b>Pfeiltasten</b> bedienen.<br>Sie können die Anleitung mit <b>ESC</b> jederzeit beenden';
		this.MainSteps = [
			{
				title: 'Anleitung Änderungsmitteilung - ',
				content: this.StartText,
				btnNext: this.btnNext,
				btnBack: this.btnClose,
				width: this.StartWidth
			},
			{
				element: '#step_location',
				title: '1. Schritt',
				content: 'Wählen Sie Ihren Standort aus.',
				placement: 'bottom',
				btnNext: this.btnNext,
				btnBack: this.btnBack,
				onNext: function () {
					var location = document.getElementById('option_location').value;
					if (location == null || location === '') {
						wtConfig.nextCustom();
					}
				}
			},
			{
				element: '#step_names',
				title: '2. Schritt',
				content: 'Geben Sie den Vor- und Nachnamen der gewünschten Person ein.',
				placement: 'bottom',
				btnNext: this.btnNext,
				btnBack: this.btnBack,
				onNext: function () {
					if (wtConfig.NamesEmptyOrWrong()) {
						wtConfig.nextCustom();
					}
				},
				width: '450px'
			},
			{
				element: '#step_createdBy',
				title: '5. Schritt',
				content: 'Geben Sie in diesem Feld Ihren Namen an.',
				placement: 'bottom',
				btnNext: this.btnNext,
				btnBack: this.btnBack,
				onNext: function () {
					var createdBy = document.getElementById('createdBy').value;
					if (createdBy == null || createdBy === '') {
						wtConfig.nextCustom();
					}
				}
			},
			{
				element: '#send',
				title: '6. Schritt',
				content: 'Klicken Sie auf dem Button "Send Mail".<br>Es wird eine Outlook Vorlage geöffnet,<br>die Sie dann versenden können.',
				placement: 'top',
				btnNext: this.btnFinish,
				btnBack: this.btnBack
		}];
	}
	NamesEmptyOrWrong() {
		var isEmptyOrWrong = false;
		var prename = document.getElementById('prename').value;
		var name = document.getElementById('name').value;
		if (prename == null || prename === '' || name == null || name === '') {
			isEmptyOrWrong = true;
		} else {
			var re = /[a-zA-ZäüößÜÖÄ\-´'éÉ/\s/]+/;
			var full_matched_prename = '';
			var full_matched_name = '';
			var i;
			if (prename !== null && prename !== '') {
				var matches = prename.match(re);
				if (matches !== null) {
					for (i = 0; i < matches.length; i++) {
						full_matched_prename += matches[i];
					}
				}
			} 
			if (name !== null && name !== '') {
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
	nextCustom() {
		this.WebTour.isPaused = true;
		this.WebTour.showLoader();
		setTimeout(() => {
			this.WebTour.isPaused = false;
			document.querySelector('.wt-loader').remove();
		}, 10000);
	}
}

var wtConfig = new WebTourConfig();
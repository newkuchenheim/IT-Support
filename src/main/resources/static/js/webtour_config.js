const wtConfig = new Map();
wtConfig.set('NextText', 'Nächste &#8594');
wtConfig.set('NextTextColor', '#fff');
wtConfig.set('NextBgColor', '#7cd1f9');
wtConfig.set('BackText', '&#8592 Zurück');
wtConfig.set('BackTextColor', '#555');
wtConfig.set('BackBgColor', '#efefef');
wtConfig.set('CloseText', 'Schließen');
wtConfig.set('FinishText', 'Fertig');
wtConfig.set('StartWidth', '600px');

const wt = new WebTour({width: '400px'});

function nextCustom() {
	wt.isPaused = true;
	wt.showLoader();
	setTimeout(() => {
		wt.isPaused = false;
		document.querySelector('.wt-loader').remove();
	}, 10000);
}
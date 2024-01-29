window.onload = init;
function init() {
	var MaxSize = 10485760;// in Bytes, 10 MB
	// set default state
	document.querySelector("#div_micos_sel").hidden = true;
	document.querySelector("#div_user_sel").hidden = true;
	var MicosCatElem = document.getElementById("micos_sel");
	var UserCatElem = document.getElementById("user_sel");
	MicosCatElem.required = false;
	MicosCatElem.value = "";
	UserCatElem.required = false;
	UserCatElem.value = "";
	document.getElementById("caution_sel").value = "";
	document.getElementById("caution_sel").addEventListener("change", function() {
		var MicosCatElem = document.getElementById("micos_sel");
		var UserCatElem = document.getElementById("user_sel");
		if (this.value !== "" && this.value.includes("MICOS")) {
			document.querySelector("#div_micos_sel").hidden = false;
			MicosCatElem.required = true;
			MicosCatElem.value = "ADDR";
			// reset user category if show before
			document.querySelector("#div_user_sel").hidden = true;
			UserCatElem.required = false;
			UserCatElem.value = "";
		} else if (this.value !== "" && this.value.includes("User")) {
			document.querySelector("#div_user_sel").hidden = false;
			UserCatElem.required = true;
			// reset micos category if show before
			document.querySelector("#div_micos_sel").hidden = true;
			MicosCatElem.required = false;
			MicosCatElem.value = "";
		} else {
			document.querySelector("#div_micos_sel").hidden = true;
			document.querySelector("#div_user_sel").hidden = true;
			MicosCatElem.required = false;
			MicosCatElem.value = "";
			UserCatElem.required = false;
			UserCatElem.value = "";
		}
	});
	document.getElementById("file").addEventListener("change", function() {
		if (this.files && this.files.length == 1 && this.files[0].size > MaxSize) {
			this.setAttribute("style", "border-color: red");
			this.value = "";
			var _file_error = document.getElementById("file_error");
			var file_error_alert = new bootstrap.Alert(_file_error);
			file_error_alert.show;
			_file_error.classList.remove("visually-hidden");
		} else {
			if (this.hasAttribute("style")) this.removeAttribute("style");
			_file_error.classList.add("visually-hidden");
		}
	});
	document.getElementById("send_ticket").addEventListener("submit", (e) => {
		e.preventDefault();
		var descr = document.getElementById("beschreibung_area");
		if (descr.value !== "" && descr.value.includes("\\")) {
			descr.value = descr.value.replaceAll("\\", "\\\\");
		}
		document.getElementById("send_ticket").submit();
	});
}
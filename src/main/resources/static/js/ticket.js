window.onload = init;
function init() {
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
}
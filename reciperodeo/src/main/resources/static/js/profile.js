const VALUE = 0;
const INPUT = 1;
const SAVE = 2;
const CANCEL = 3;
const EDIT = 4;

function hideElements(button) {
	let elements = getElementList(button);
	elements[INPUT].style.display = "none";
	elements[SAVE].style.display = "none";
	elements[CANCEL].style.display = "none";
	elements[VALUE].style.display = "initial";
	elements[EDIT].style.display = "initial";
	return elements;
}

function cancelEdit(event) {
	event.preventDefault();
	let elements = hideElements(this);
	elements[INPUT].value = elements[VALUE].innerHTML;
}

function getElementList(button) {
	return document.getElementsByClassName(button.classList[1]);
}

function openEdit(event) {
	event.preventDefault();
	let elements = getElementList(this);
	elements[INPUT].style.display = "initial";
	elements[SAVE].style.display = "initial";
	elements[CANCEL].style.display = "initial";
	elements[VALUE].style.display = "none";
	this.style.display = "none";
}

function setupButtons(c) {
	let elements = document.getElementsByClassName(c);
	elements[EDIT].addEventListener('click', openEdit);
	elements[CANCEL].addEventListener('click', cancelEdit);
}

function deleteEnsure(event) {
	if(!confirm('Are you sure you want to delete your account?')) {
		event.preventDefault();
		location.reload();
	}
}

function changeEnsure(event) {
	if(!confirm('You will be logged out upon change. Is that okay?')) {
		event.preventDefault();
		location.reload();
	}
}

setupButtons("username");

setupButtons("email");

setupButtons("password");

setupButtons("searchable");

document.getElementsByClassName("delete")[0].addEventListener('click', deleteEnsure);
document.getElementById("username-button").addEventListener('click',changeEnsure);
document.getElementById("password-button").addEventListener('click', changeEnsure);
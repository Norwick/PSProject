/**
 * 
 */

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

function saveEdit() {
	let elements = hideElements(this);
	if (this.classList[1] != 'password') {
		elements[VALUE].innerHTML = elements[INPUT].value;
	}
}

function cancelEdit() {
	let elements = hideElements(this);
	elements[INPUT].value = elements[VALUE].innerHTML;
}

function getElementList(button) {
	return document.getElementsByClassName(button.classList[1]);
}

function openEdit() {
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
	elements[SAVE].addEventListener('click', saveEdit);
	elements[CANCEL].addEventListener('click', cancelEdit);
}

let avatarButton = document.getElementById("avatar-button");
/** special case */

setupButtons("username");

setupButtons("email");

setupButtons("password");

setupButtons("bio");

setupButtons("friendly");
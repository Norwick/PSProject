class IngredientInfo {
    constructor() {
        this.ingredientSections = [];
	}
    addSection(ingSection) {
        this.ingredientSections.push(ingSection);
    }
	getSection(i) {
		return this.ingredientSections[i];
	}
	setSection(i, s) {
		this.ingredientSections[i] = s;
	}
    removeSection(i) {
        this.ingredientSections.splice(i, 1);
    }
	getSectionsNum() {
		return this.ingredientSections.length;
	}
}

class InstructionInfo {
	constructor() {
		this.instructionSections = [];
	}
	addSection(insSection) {
		this.instructionSections.push(insSection);
	}
	getSection(i) {
		return this.instructionSections[i];
	}
	setSection(i, s) {
		this.instructionSections[i] = s;
	}
	removeSection(i) {
		this.instructionSections.splice(i,1);
	}
	getSectionsNum() {
		return this.instructionSections.length;
	}
}

class IngredientSection {
	constructor() {
		this.title = '';
		this.ingredients = [];
	}
	addIngredient(ing) {
		this.ingredients.push(ing);
	}
	getIngredient(i) {
		return this.ingredients[i];
	}
	setIngredient(i, ing) {
		this.ingredients[i] = ing;
	}
	removeIngredient(i) {
		this.ingredients.splice(i, 1);
	}
	setTitle(t) {
		this.title = t;
	}
	getTitle() {
		return this.title;
	}
	getIngredientsNum() {
		return this.ingredients.length;
	}
}

class InstructionSection {
	constructor() {
		this.title = '';
		this.instructions = [];
	}
	addInstruction(ins) {
		this.instructions.push(ins);
	}
	getInstruction(i) {
		return this.instructions[i];
	}
	setInstruction(i, ins) {
		this.setInstruction[i] = ins;
	}
	setTitle(t) {
		this.title = t;
	}
	getTitle() {
		return this.title;
	}
	getInstructionsNum() {
		return this.instructions.length;
	}
}

class Ingredient {
	constructor() {
		this.name = '';
		this.amount = '';
		this.notes = [];
	}
	setName(n) {
		this.name = n;
	}
	setAmount(a) {
		this.amount = a;
	}
	addNote(n) {
		this.notes.push(n);
	}
	getNote(i) {
		return this.notes[i];
	}
	setNote(i, n) {
		this.notes[i] = n;
	}
	removeNote(i) {
		this.notes.splice(i, 1);
	}
	getNotesNum() {
		return this.notes.length;
	}
	getName() {
		return this.name;
	}
	getAmount() {
		return this.amount;
	}
}

class Instruction {
	constructor() {
		this.info = '';
		this.notes = [];
	}
	setInfo(inf) {
		this.info = inf;
	}
	addNote(n) {
		this.notes.push(n);
	}
	getNote(i) {
		return this.notes[i];
	}
	setNote(i, n) {
		this.notes[i] = n;
	}
	removeNote(i) {
		this.notes.splice(i, 1);
	}
	getInfo() {
		return this.info;
	}
	getNotesNum() {
		return this.notes.length;
	}
}

function removeSec() {
	let parentNode = this.parentNode;
	parentNode.parentNode.removeChild(parentNode);
}

function createLabel(parent, labelTypeText, classText) {
	let l = document.createElement("label");
	l.innerText = labelTypeText;
	l.classList.add(classText);
	parent.appendChild(l);
}

function createTextArea(parent, classText) {
	let t = document.createElement("textarea");
	t.cols = 60;
	t.classList.add(classText);
	parent.appendChild(t);
	return t;
}

function createInput(parent, classText) {
	let i = document.createElement("input");
	i.type = "text";
	i.classList.add(classText);
	parent.appendChild(i);
	return i;
}

function createOrderedList(parent) {
	let ol = document.createElement("ol");
	parent.appendChild(ol);
	return ol;
}

function createLineBreak(parent) {
	let br = document.createElement("br");
	parent.appendChild(br);
	return br;
}

function createDeleteButton(parent, classText) {
	let del = document.createElement("button");
	del.type = "button";
	del.innerText = "❌";
	del.classList.add(classText);
	del.classList.add("del");
	del.addEventListener('click', removeSec);
	parent.appendChild(del);
	return del;
}

function createAddButton(parent, childTypeText, addAction, classText) {
	let b = document.createElement("button");
	b.innerText = "Add " + childTypeText;
	b.addEventListener('click', addAction);
	b.classList.add(classText);
	b.classList.add("add");
	parent.appendChild(b);
	return b;
}

function createTextAreaDeleteAndLabel(parent, labelTypeText, classText) {
	createLabel(parent, labelTypeText, classText);
	createDeleteButton(parent, classText);
	createLineBreak(parent);
	return createTextArea(parent, classText);
}
function createInputAndLabel(parent, labelTypeText, classText) {
	createLabel(parent, labelTypeText, classText);
	createLineBreak(parent);
	return createInput(parent, classText);
}

function createInputDeleteAndLabel(parent, labelTypeText, classText) {
	createLabel(parent, labelTypeText, classText);
	createDeleteButton(parent, classText);
	createLineBreak(parent);
	return createInput(parent, classText);
}

function addNote(noteList) {
	let note = document.createElement("li");
	createTextAreaDeleteAndLabel(note, "Note", "note");
	noteList.appendChild(note);
}

function addNoteValue(noteList, noteJS) {
	let note = document.createElement("li");
	createTextAreaDeleteAndLabel(note, "Note", "note").value = noteJS;
	noteList.appendChild(note);
}

function viewNote(noteList, noteJS) {
	let note = document.createElement("li");
	note.innerText = noteJS;
	noteList.appendChild(note);
}

function addNoteViaButton() {
	addNote(this.previousElementSibling.previousElementSibling);
}

function addIngredient(ingredientList) {
	let ing = document.createElement("li");
	ingredientList.appendChild(ing);
	
	createInputDeleteAndLabel(ing, "Ingredient Name", "ing");
	createLineBreak(ing);
	createInputAndLabel(ing, "Ingredient Amount", "ing");
	let noteList = createOrderedList(ing);
	createLineBreak(ing);
	createAddButton(ing, "Ingredient Note", addNoteViaButton, "note");
	addNote(noteList);
}

function addIngredientValue(ingredientList, ingJS) {
	let ing = document.createElement("li");
	ingredientList.appendChild(ing);
	createInputDeleteAndLabel(ing, "Ingredient Name", "ing").value = ingJS.name;
	createLineBreak(ing);
	createInputAndLabel(ing, "Ingredient Amount", "ing").value = ingJS.amount;
	let noteList = createOrderedList(ing);
	createLineBreak(ing);
	createAddButton(ing, "Ingredient Note", addNoteViaButton, "note");
	
	let count = ingJS.notes.length;
	for (let i = 0; i < count; ++i) {
		addNoteValue(noteList, ingJS.notes[i]);
	}
}

function viewIngredient(ingredientList, ingJS) {
	let ing = document.createElement("li");
	ingredientList.appendChild(ing);
	
	let name = document.createElement("h4");
	name.innerText = ingJS.name;
	ing.appendChild(name);
	
	let amount = document.createElement("span");
	amount.innerText = ingJS.amount;
	ing.appendChild(amount);
	
	let noteList = createOrderedList(ing);
	let size = ingJS.notes.length;
	for (let i = 0; i < size; ++i) {
		viewNote(noteList, ingJS.notes[i]);
	}
}

function addInstruction(instructionList) {
	let ins = document.createElement("li");
	instructionList.appendChild(ins);
	
	createTextAreaDeleteAndLabel(ins, "Instruction", "ins");
	createLineBreak(ins);
	let noteList = createOrderedList(ins);
	createLineBreak(ins);
	createAddButton(ins, "Instruction Note", addNoteViaButton, "note");
	addNote(noteList);
}

function addInstructionValue(instructionList, insJS) {
	let ins = document.createElement("li");
	instructionList.appendChild(ins);
	
	createTextAreaDeleteAndLabel(ins, "Instruction", "ins").value = insJS.info;
	createLineBreak(ins);
	let noteList = createOrderedList(ins);
	createLineBreak(ins);
	createAddButton(ins, "Instruction Note", addNoteViaButton, "note");
	
	let size = insJS.notes.length;
	for (let i = 0; i < size; ++i) {
		addNoteValue(noteList, insJS.notes[i]);
	}
}

function viewInstruction(instructionList, insJS) {
	let ins = document.createElement("li");
	instructionList.appendChild(ins);
	
	ins.innerText = insJS.info;
	let noteList = createOrderedList(ins);
	
	let size = insJS.notes.length;
	for (let i = 0; i < size; ++i) {
		viewNote(noteList, insJS.notes[i]);
	}
}

function addIngredientViaButton() {
	addIngredient(this.previousElementSibling.previousElementSibling);
}

function addInstructionViaButton() {
	addInstruction(this.previousElementSibling.previousElementSibling);
}

function addIngSection() {
	let ingSec = document.createElement("li");
	ingHTML.lastElementChild.previousElementSibling.appendChild(ingSec);
	
	createInputDeleteAndLabel(ingSec, "Ingredient Section Title", "ingsec");
	let ingredientList = createOrderedList(ingSec);
	createLineBreak(ingSec);
	createAddButton(ingSec, "Ingredient", addIngredientViaButton, "ing");
	addIngredient(ingredientList);
}

function addInsSection() {
	let insSec = document.createElement("li");
	insHTML.lastElementChild.previousElementSibling.appendChild(insSec);
	
	createInputDeleteAndLabel(insSec, "Instruction Section Title", "inssec");
	let instructionList = createOrderedList(insSec);
	createLineBreak(insSec);
	createAddButton(insSec, "Instruction", addInstructionViaButton, "ins");
	addInstruction(instructionList);
}

function addIngSectionValue(ingSecJS) {
	let ingSec = document.createElement("li");
	ingHTML.lastElementChild.previousElementSibling.appendChild(ingSec);
	createInputDeleteAndLabel(ingSec, "Ingredient Section Title", "ingsec").value = ingSecJS.title;
	let ingredientList = createOrderedList(ingSec);
	createLineBreak(ingSec);
	createAddButton(ingSec, "Ingredient", addIngredientViaButton, "ing");
	
	let size = ingSecJS.ingredients.length;
	for(let i = 0; i < size; ++i) {
		addIngredientValue(ingredientList, ingSecJS.ingredients[i]);
	}
}

function viewIngSection(ingSecJS) {
	let ingSec = document.createElement("li");
	ingHTML.appendChild(ingSec);
	
	let title = document.createElement("h3");
	title.innerText = ingSecJS.title;
	ingSec.appendChild(title);
	
	let ingredientList = createOrderedList(ingSec);
	
	let size = ingSecJS.ingredients.length;
	for (let i = 0; i < size; ++i) {
		viewIngredient(ingredientList, ingSecJS.ingredients[i]);
	}
}

function addInsSectionValue(insSecJS) {
	let insSec = document.createElement("li");
	insHTML.lastElementChild.previousElementSibling.appendChild(insSec);
	
	createInputDeleteAndLabel(insSec, "Instruction Section Title", "inssec").value = insSecJS.title;
	let instructionList = createOrderedList(insSec);
	createLineBreak(insSec);
	createAddButton(insSec, "Instruction", addInstructionViaButton, "ins");
	
	let size = insSecJS.instructions.length;
	for (let i = 0; i < size; ++i) {
		addInstructionValue(instructionList, insSecJS.instructions[i]);
	}
}

function viewInsSection(insSecJS) {
	let insSec = document.createElement("li");
	insHTML.appendChild(insSec);
	
	let title = document.createElement("h3");
	title.innerText = insSecJS.title;
	insSec.appendChild(title);
	
	let instructionList = createOrderedList(insSec);
	
	let size = insSecJS.instructions.length;
	for (let i = 0; i < size; ++i) {
		viewInstruction(instructionList, insSecJS.instructions[i]);
	}
}

function setUpIngredients() {
	createOrderedList(ingHTML);
	createAddButton(ingHTML, "Ingredient Section", addIngSection, "ingsec");
	addIngSection();
}


function setUpIngredientsValue(ingInfoJS) {
	let size = ingInfoJS.ingredientSections.length;
	createOrderedList(ingHTML);
	createAddButton(ingHTML, "Ingredient Section", addIngSection, "ingsec");
	for(let i = 0; i < size; ++i) {
		addIngSectionValue(ingInfoJS.ingredientSections[i]);
	}
}

function viewIngredients(ingInfoJS) {
	let size = ingInfoJS.ingredientSections.length;
	createOrderedList(ingHTML);
	for(let i = 0; i < size; ++i) {
		viewIngSection(ingInfoJS.ingredientSections[i]);
	}
}

function setUpInstructions() {
	createOrderedList(insHTML);
	createAddButton(insHTML, "Instruction Section", addIngSection, "inssec");
	addInsSection();
}

function setUpInstructionsValue(insInfoJS) {
	let size = insInfoJS.instructionSections.length;
	createOrderedList(insHTML);
	createAddButton(insHTML, "Instruction Section", addInsSection, "inssec");
	for (let i = 0; i < size; ++i) {
		addInsSectionValue(insInfoJS.instructionSections[i]);
	}
}

function viewInstructions(insInfoJS) {
	let size = insInfoJS.instructionSections.length;
	createOrderedList(insHTML);
	for (let i = 0; i < size; ++i) {
		viewInsSection(insInfoJS.instructionSections[i]);
	}
}

//if i was a good person i'd use classes and ids...
function saveRecipe() {
	let recipeHTML = document.getElementById("saved");
	const ingInfoJS = new IngredientInfo();
	let ingSecNode = recipeHTML.firstElementChild.firstElementChild.firstElementChild;
	while (ingSecNode != null && ingSecNode != undefined) {
		let ingSecJS = new IngredientSection();
		ingInfoJS.addSection(ingSecJS);
		let ingSecTitle = ingSecNode.firstElementChild.nextElementSibling.nextElementSibling.nextElementSibling;
		ingSecJS.setTitle(ingSecTitle.value);
		let ingNode = ingSecTitle.nextElementSibling.firstElementChild;
		while (ingNode != null && ingNode != undefined) {
			let ingJS = new Ingredient();
			ingSecJS.addIngredient(ingJS);
			let ingName = ingNode.firstElementChild.nextElementSibling.nextElementSibling.nextElementSibling;
			ingJS.setName(ingName.value);
			let ingAmount = ingName.nextElementSibling.nextElementSibling.nextElementSibling.nextElementSibling;
			ingJS.setAmount(ingAmount.value);
			let noteNode = ingAmount.nextElementSibling.firstElementChild;
			while (noteNode != null && noteNode != undefined) {
				let note = noteNode.firstElementChild.nextElementSibling.nextElementSibling.nextElementSibling;
				ingJS.addNote(note.value);
				noteNode = noteNode.nextElementSibling;
			}
			ingNode = ingNode.nextElementSibling;
		}
		ingSecNode = ingSecNode.nextElementSibling;
	}
	
	const insInfoJS = new InstructionInfo();
	let insSecNode = recipeHTML.lastElementChild.firstElementChild.firstElementChild;
	while (insSecNode != null && insSecNode != undefined) {
		let insSecJS = new InstructionSection();
		insInfoJS.addSection(insSecJS);
		let insSecTitle = insSecNode.firstElementChild.nextElementSibling.nextElementSibling.nextElementSibling;
		insSecJS.setTitle(insSecTitle.value);
		let insNode = insSecTitle.nextElementSibling.firstElementChild;
		while (insNode != null && insNode != undefined) {
			let insJS = new Instruction();
			insSecJS.addInstruction(insJS);
			let insInfo = insNode.firstElementChild.nextElementSibling.nextElementSibling.nextElementSibling;
			insJS.setInfo(insInfo.value);
			let noteNode = insInfo.nextElementSibling.nextElementSibling.firstElementChild;
			while (noteNode != null && noteNode != undefined) {
				let note = noteNode.firstElementChild.nextElementSibling.nextElementSibling.nextElementSibling;
				insJS.addNote(note.value);
				noteNode = noteNode.nextElementSibling;
			}
			insNode = insNode.nextElementSibling;
		}
		insSecNode = insSecNode.nextElementSibling;
	}
	
	const recipeJS = {ingInfoJS, insInfoJS};
	document.getElementById("recipeJSON").value = JSON.stringify(recipeJS);
	let postButton = document.getElementById("poster");
	postButton.click();
}

function emptySetup() {
	const saver = document.getElementById("saver");
	saver.addEventListener('click', saveRecipe);
	setUpIngredients();
	setUpInstructions();
}

function populatedSetup(recipeJSString) {
	const saver = document.getElementById("saver");
	saver.addEventListener('click', saveRecipe);
	let correctString = recipeJSString.replaceAll("&quot;", '"');
	const recipeJS = JSON.parse(correctString);
	setUpIngredientsValue(recipeJS.ingInfoJS);
	setUpInstructionsValue(recipeJS.insInfoJS);
}

function setNonJson(title, state) {
	document.getElementById("title").value = title;
	document.getElementById("state").value = state;
}

function viewOnly(recipeJSString) {
	let correctString = recipeJSString.replaceAll("&quot;", '"');
	const recipeJS = JSON.parse(correctString);
	viewIngredients(recipeJS.ingInfoJS);
	viewInstructions(recipeJS.insInfoJS);
}
const ingHTML = document.getElementById('ingredients');
const insHTML = document.getElementById('instructions');
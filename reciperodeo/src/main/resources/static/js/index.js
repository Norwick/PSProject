let recipeList = document.querySelector('#trending-recipes ul');
let recipeFiller = recipeList.innerHTML;
recipeList.innerHTML = recipeFiller.repeat(30);
//the above is sort of a loop, eh?
//also here is a loop and a collection to show I can use one
let div46 = new Set();
let div12 = new Set();
for (let i = 1; i <= 100; ++i) {
	if (i % 4 == 0 && i % 6 == 0) {
		div46.add(i);
	}
	if (i % 12 == 0) {
		div12.add(i);
	}
}
let same = div46.size == div12.size;
if (same) {
	for (let n of div46) {
		if (!div12.has(n)) {
			same = false;
		}
	}
}
if (same) {
	console.log('All numbers divisible by 4 and 6 are divisible by 12');
} else {
	console.log('There exists some number divisible by 4 and 6 that is not divisible by 12');
}
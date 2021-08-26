let recipeList = document.querySelector('#trending-recipes ul');
let recipeFiller = recipeList.innerHTML;
recipeList.innerHTML = recipeFiller.repeat(30);
# Recipe Rodeo Readme
Recipe Rodeo is a project I made for Per Scholas. It is a simple recipe website that allows users to create, tag, share, and search for recipes.  
By the way, if you’re trying to run the project, you will need to create a new application.properties instead of just changing values. You can find where to put it by looking at the .gitignore path.
## User Stories
### Implemented
- As a user, I want to log in, so I can save recipes related to me.
- As a user, I want to save recipes privately, so I can track recipes without strangers seeing.
- As a user, I want to save recipes publicly, so I can share my cooking with others.
- As a user, I want to be able to edit recipes, so I can correct mistakes.
- As a user, I want to search for recipes by title, so I can find recipes I want.
- As a user, I want to search for recipes by tags, so I can find related recipes.
- As a user, I want to delete recipes, so that I can get rid of ones I don’t care about.
- As a user, I want to delete my account, so that my data is no longer tracked after I stop using this service.
- As a user, I want to tag my recipes, so others can find my recipes easier.
- As a user, I want to be able to give my recipes sections so that I can better order my recipe.
- As a user, I want to view my recipes, so I can keep track of them.
- As a person, I want to be able to contact the sitemaster, so I can give feedback about issues I run into.
- As a person, I want to view recipes without an account, so I can use them quickly.
- As the site owner, I want to show the latest public recipes, so that people are more likely to engage with others’ content and stay on the site longer.
- As a user, I want to edit my profile, so that I can change any mistakes or outdated info.
- As a user, I want to change my username, because I regret my old username.
- As a user, I want to be searchable, so that friends can find me easier. (only semi implemented)
- As a user, I want to not be searchable, so that I can use this service without others seeing me. (only semi implemented)
- As a user, I want to set an avatar, so I can express myself.
### Unimplemented
- As a user, I want to share recipes only to my friends or via link so I can control who sees my recipes.
- As a user, I want to allow other users to edit my recipe, so we can create it collaboratively.
- As a user, I want to pass my ownership to other users, so I can delete my account but others still have access to my recipes.
- As an admin, I want to delete recipes because they might violate our TOS.
- As an admin, I want to delete users because they might violate our TOS.
- As an admin, I want to delete tags because they might violate our TOS.
- As an admin, I want to block certain phrases because they might violate our TOS.
- As IT, I want to see a log of user activity, so I can get context when an issue is reported.
- As IT, I want to be able to log in - As a user so I can reproduce bugs.
- As a user, I want to save recipes I like, so they’re easier to find later.
- As a user, I want to set an image to a recipe so I can help readers picture the end result.

## Technical Challenges & Solutions
- My worst technical challenge was when I deleted a user via database and didn't delete the recipe (which saves the user id as a field) so things kept crashing and I had no clue what was going on. It was horrible because I thought it was some session invalidation error. I ended switching to my database backup to see if that solved things, and it did, and then I diffcheck'd the creation code for my current database and backup database to discover the problem.
- I wanted to add avatars and recipe pictures. I was able to get file upload to work, but it was in the wrong location and I wasn’t able to upload it to the correct location. Since I didn’t have time, I decided to go with using emoji in place of pictures for the recipes and using gravatar for avatars, which had the added benefit of not needing an extra database field (and if you tilt your head sideways, it could be counted as a microservice). I think if I had more time, I would look into uploading the pictures directly to the database as blobs.
- Related to avatars, adding transparency and non-transparent text to overlay an image really gave me a headache. I ended up going with ‘background-image’ and then ‘linear-gradient’ on a button and then setting the button text to what I wanted, but then the background image repeated, so I had to look that up too. Thankfully other people have dealt with this exact same issue on stack overflow.
- Tomcat gave me a headache when I tried to manually set it up, so I ended up going with the automatically set up tomcat that spring starter projects configure for you.
- JUnit suites didn’t work for me and I couldn’t figure out what I was doing wrong. I’m assuming there’s some dependency issue despite me trying a bunch of configurations and even giving in and including JUnit 4 on the build path, but I did four different tutorials and even generated a test suite via Eclipse and all of them ran but didn’t run the tests. I’m sure with enough time I’d be able to fix it, but I’m stumped, so I’m just including a fake suite class that doesn’t run. Sorry.
- Data entry so I could test looks was a huge pain. I think I created 40ish recipes (title only thankfully) so I could test the index/search page. It was also an issue because I spent time lovingly making recipes for the sake of getting recipe looks semi decent an only to find that it wasn’t properly saved and I was back to zero since my code isn’t robust enough to save javascript generated variable size input forms. I’m sure there’s a way, but since I can solve it by just not having bugs in my code, it’s fine for the scope of this project.
- I really wanted to store passwords as char arrays so I could blank them out, but I wasn't quite sure how to go about that with Spring involved. It was hard to google the question, so maybe it's not too relevant security-wise.
- I also was not able to get Lazy fetching to work. Since the scope of the database is so small, I switched to eager, but this wouldn't fly in the real world.
- Similarly, I had real issues with using composite keys. Before I downgraded the scope of my project due to time (I had more than one type of recipe-user relationship before), I was trying to get hibernate to acknowledge composite keys and it just didn't want to, so I had to use a generated id instead, which made equality somewhat a pain.
- Responsive design. I was trying to get bootstrap to work, but it was taking a long time, so I decided to not deal with it since it isn't an explicit requirement on the final project. I was trying to figure out how to calculate actual width via javascript so I could send it to the server and decide a suitable number of recipes in pagination, but it didn't calculate correctly, so I dropped that since it took too much time, since the only (but most important) broken thing on mobile is the nav-bar. I have an idea how to fix this if I revisit this: I'd look in depth into css media queries.
- Overall I would say there is too much spaghetti code in this project. It’s not apparent with the JPA side of things, but once you enter the general controller or look at the javascript for recipe editing and profile editing you’ll realize how brittle I made everything and how much I should have split things up into separate files. It’s fine for now, but if I came back to this that would probably the first thing to refactor. Also, sorry for the lazy javadoc comments.

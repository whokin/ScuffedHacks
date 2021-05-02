# SystemHacks 2021 #
**The Scuffed Dictionary**
**By: WuRk 1n Pr0gRes**
**(Warren Ho-Kin, Valerie Kistrina, Annie Yao, Dustin Jorgensen)**

The Scuffed Dictionary provides absolutely scuffed definitions for every word that you never knew
you needed in your life. It works in two steps: **searching**and **scuffing**. In short, the program first
finds the definition by scraping the internet and then runs every word through a thesaurus to produce a
truly scuffed definition.

**Searching**: In the DefinitionDisplay.java method the program searches for the inputted user's
requested word to scuffify and searches Merriam-Webster for it's definition

```
'protected Void doInBackground(Void... voids) {

             String url = "https://www.merriam-webster.com/dictionary/" + word + "/";'
```
Once the definition is found it is added to a list called definitionList

```
if (!definition.equals("no matches")){
                        definitionList.add(definition);
                    }
```

The definition is now collected and ready to be scuffed

**Scuffing**: In a similar fashion to how the original definition was gathered, we throw every word in the
definitionList into merriam-webster once again but this time using their thesaurus url. From there we swap the
original word with the first synonym that is offered. 

```
String thesaurusUrl = "https://www.merriam-webster.com/thesaurus/" + word + "/"; 
	Document document = Jsoup.connect(thesaurusUrl).get();

	//get definition
	
	Elements synElements = document.select("ul.mw-list");
	String synonym = extractSynonym(synElements.toString());
```

Once this process is complete, the program prints out the new scuffed definition of the original word the user inputted. 


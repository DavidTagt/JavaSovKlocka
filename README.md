# JavaOchSovKlocka
En mat och sov klocka skriven i Java. 

Programmeringsuppgift som jag gjorde under mars 2021 för en kurs i Android programmering från Stockholms universitet.

Är väl egentligen bara en glorifierad alarm app, men jag hade roligt när jag gjorde den i alla fall :)

Det var lite "moderna" tekniker jag implementerade, till exempel finns det bara en activity som ansvarar för många fragments. Det innebär att jag också behövt implementera en Nav finder, som sköter navigering hit och dit i appen. Kolla under Res -> Navigation -> nav-graph.xml för att se ett flöde på hur man kan navigera runt i appen. 

Databasen som jag använt är Android Room. Det är någon form av ORM tror jag, har varit relativt smärtfri att jobba med. Känns som att den skiljer sig en del från Entity Framework dock!

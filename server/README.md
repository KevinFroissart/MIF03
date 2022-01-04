## 08/10/2021 TP2
Pour ajouter le `Vote Blanc` nous avons choisi premièrement de modifier le type de la
liste de candidats en passant d'une HashMap à une LinkedHashMap. De cette manière
nous pouvons ajouter à la fin de celle-ci le candidat `Vote Blanc` afin que celui-ci
apparaisse en fin de liste dans la page des résultats ainsi que dans la liste de choix
des candidats.

Nous avons aussi ajouté le fait qu'un utilisateur ayant déjà voté ne puisse pas revoter
en affichant un message personnalisé qui propose une redirection sur ballot.jsp

L'encoding UTF-8 est défini dans le fichier web.xml
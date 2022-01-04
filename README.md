## TP2
### Description
Après la mise en place et la configuration de notre VM, il nous est demandé de mettre en place une application avec les technologies de programmation côté serveur en Java.
Cette application aura pour but de servir de plateforme de vote pour une élection, ce qui pose bien évidemment des questions de confidentialité, entre autres :
Qui a le droit de voir le vote de qui ? Qui a le droit d'accéder à quel page ?
Dans ce tp nous avons développé une application Web qui se veut réaliste tout en suivant les directives du tp.

### Conception et réalisation de l'application
Durant la partie de la conception nous avons dans un premier temps configuré nos `JSP` de manière à gérer les redirections,
les accès aux pages, mais aussi dans le `Ballots.jsp`faire la vérification :
Si l'utilisateur est connecté, mais n'a pas voté, ballot.jsp affichera un message en informant l'utilisateur.
`<c:when test="${sessionScope.user != null && ballots.get(sessionScope.user.login) == null}">`

En plus de cela nous avons utilisé les `Servlets` pour traiter les informations renvoyées par les `JSP`.
Notons que pour le Servlet `Deco` est chargé de déconnecter l'utilisateur en supprimant ses attributs de session et de le renvoyer vers la page d'accueil
avec : `response.sendRedirect("index.html");`

La dernière partie de la conception a consisté à faire le refactoring, l'amélioration des fonctionnalités et la gestion des erreurs.
Pour ajouter le `Vote Blanc` nous avons choisi premièrement de modifier le type de la
liste de candidats en passant d'une HashMap à une LinkedHashMap. De cette manière
nous pouvons ajouter à la fin de celle-ci le candidat `Vote Blanc` afin qu'il
apparaisse en fin de liste dans la page des résultats ainsi que dans la liste de choix
des candidats.

Nous avons aussi ajouté le fait qu'un utilisateur ayant déjà voté ne puisse pas revoter
en affichant un message personnalisé qui propose une redirection sur ballot.jsp.

L'encoding UTF-8 est défini dans le fichier web.xml.


### Intégration continue et déploiement sur votre VM
avant de déployer notre application via l'intégration continue on est passé par plusieurs étapes,
- un build du projet,
- compilation du code,
- et la création du package.



## TP3
### Description
Dans ce TP qui est la suite logique de la précédente il sera question de continuer l'amélioration du code en
s'inspirant des notions vues au cours mais aussi des designs patterns étudié en MIFO1.
Dans une seconde partie nous avons utilisé le protocole HTTP pour améliorer
les performances de votre application.

### Refactoring de votre application

### Gestion du cache
La gestion du cache est une part importante de HTTP. En effet, HTTP étant utilisé sur des réseaux peu fiables,
la gestion du cache permet d'améliorer les performances ou de palier à des défaillances. Si un élément intermédiaire du réseau,
ou même le client, dispose d'un système de mise en cache, il peut l'utiliser s'il sait que les données qu'il contient sont à jour ou si le serveur distant est temporairement indisponible.

#### Utilisation des en-têtes HTTP de date
les en-têtes HTTP sont utilisés pour transmettre des informations supplémentaires avec une réponse HTTP ou une requête HTTP.Date
L’en-tête HTTP contient la date et l’heure auxquelles le message a été généré.
Cela permet de ne pas renvoyer une données si elle n'a pas été modifiée depuis le dernier `GET`:
- Lors de requêtes `POST` on met à jour le champ date:
  `getServletContext().setAttribute("date", date.getTime());`
- Dans le cas d'une requête `GET` :
  . on génère un en-tête de réponse Last-Modified,
  . si la requête contient un en-tête If-Modified-Since, on le compare avec la variable d'instance.

#### Utilisation d'Entity Tags (ETag)
L'en-tête de réponse ETag HTTP est un identifiant pour une version spécifique d'une ressource.
Il permet au cache d'être plus efficace et d'économiser de la bande passante, du fait que le serveur Web n'a pas besoin
d'envoyer une réponse complète si le contenu n'a pas changé. Sinon, si le contenu a changé, les etags sont utiles pour
empêcher les mises à jour simultanées d'une ressource de s'écraser mutuellement.
Dans notre cas on l'utilise pour faire la différence entre un utilisateur connecté et un utilisateur non connecté, Notamment pour afficher
la page `Résultats`.
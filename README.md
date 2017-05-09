# nightjawaj
Projet GLA L3 MIAGE

Groupe consitué de Jeremy Quyen, Đức Nguyễn, Lucas Salvato

## Sujet
### Contexte

Plusieurs amis veulent sortir à Paris afin de fêter leurs retrouvailles et ont envie de passer une bonne soirée. Pour cela, ils auraient besoin d'organiser successivement trois sorties

* Boire un verre dans un bar sympa
* Manger au restaurant (attention: un des amis n'aime pas les kebabs!)
* Sortir en boîte de nuit

### Exigences

Votre projet consiste à fournir une interface avec en:

* Entrée: une date, une liste de personnes ayant des contraintes d'adresses (là où ils habitent) et de préférences alimentaires
* Résultat: une liste chronologique de trois adresses (bar, resto, boîte)

Parmi les propriétés que l'on demande sur votre système, vous devrez considérer qu'une sortie prend un certain temps (que vous déterminerez arbitrairement), que le déplacement d'un point à un autre prend aussi un certain temps (qui devra être calculé), et que les sorties ont des horaires d'ouverture coïncidant avec les critères précédents.

Pour aller plus loin, vous êtes libres de prendre des initiatives afin d'enrichir votre projet: permettre à l'utilisateur de refuser un triplet de sorties et générer d'autres, considérer des trajets à vélo et donner les positions de Vélib les plus proches du domicile, se déplacer en voiture et désigner le Sam de la soirée... Notez cependant que toute fonctionnalité doit être documentée et que son absence lors de l'implémentation (e.g. manque de temps, impossibilité de l'API à fournir des informations spécifiques...) entraîne des pénalités. Les étapes d'analyse, de conception et d'implémentation sont donc liées et aucune ne devrait être négligée.

___

API key: AIzaSyBPLlRzEty62nUM8JIArfmRv8YLFMaY5u4

API de secours : AIzaSyCixQpC0sRu2QPXgZF5OKQE19xgx2f-iz8

API URL exemple : https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=48.855218,2.368622&radius=500&type=restaurant&name=burger&key=AIzaSyBPLlRzEty62nUM8JIArfmRv8YLFMaY5u4

Diagrammes UML de nightjawaj : https://repository.genmymodel.com/lonaxous/nightjawaj
         ()
 ________/__
 \ _____/_ /
  \\     //
   \\   //
    \\ //
     | |
     | |
    /   \
   /_____\
  NightJawaj
                                                                
____

# Installation
## Depuis un terminal avec le fichier jar et le fichier sql :

Les fichiers jar et sql sont disponible dans la partie "release"

```bash
sqlite3 database.db < nightjawaj.sql
java -jar nightjawaj.jar initiate
java -jar nightjawaj.jar start
```

* Créer la base de donnée à l'aide du fichier sql

* Entrer les paramètres initiaux (la clé api)

* Lancer le serveur

* Puis ouvrir dans un navigateur web : http://localhost:4567

## Depuis le code avec Intelliji
* Créer la base de donnée

```bash
sqlite3 database.db < nightjawaj.sql
```
* Depuis Intelliji alt+shift+f10, éditer les arguments de lancement. initiate, lancer puis start.

* Puis ouvrir dans un navigateur web : http://localhost:4567

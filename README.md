# nightjawaj
Projet GLA L3 MIAGE

Groupe consitué de Jeremy Quyen, Duc Nguyen, Lucas Salvato

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

API URL exemple : https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=48.855218,2.368622&radius=500&type=restaurant&name=burger&key=AIzaSyBPLlRzEty62nUM8JIArfmRv8YLFMaY5u4

Diagramme de cas d'utilisation : https://repository.genmymodel.com/lonaxous/nightjawaj

# TD Generator Tool — Génération d’exercices Java à partir de projets existants

Ce dépôt présente une chaîne d’outils permettant de transformer automatiquement un projet Java Maven complet en un exercice pédagogique partiel destiné à des travaux dirigés.

L’approche repose sur la modification contrôlée de code existant : certaines méthodes sont partiellement ou totalement retirées afin d’amener les étudiants à analyser, comprendre et compléter un système logiciel réaliste, plutôt qu’à produire un programme ex nihilo.

La solution s’inscrit dans un contexte d’enseignement de la programmation marqué par l’essor des outils d’intelligence artificielle générative, et vise à proposer un cadre pédagogique mieux adapté aux pratiques contemporaines.

---

## Objectif pédagogique

L’objectif principal du projet est de fournir aux enseignants un moyen outillé de :

- partir d’un projet Java fonctionnel existant servant de solution de référence ;
- identifier automatiquement des méthodes pertinentes à transformer en supports pédagogiques ;
- générer un projet exercice compilable, partiellement incomplet ;
- produire un énoncé étudiant cohérent décrivant les tâches attendues.

L’approche privilégie la compréhension de l’architecture, des interactions entre classes et de la logique applicative, plutôt que la simple génération de code.

---

## Vue d’ensemble de l’approche

La chaîne d’outils est structurée autour de deux étapes distinctes :

1. **Analyse statique** du projet Java de référence afin d’identifier des méthodes candidates à une coupure pédagogique.
2. **Transformation contrôlée** du projet selon une configuration explicite, produisant un projet exercice et un énoncé associé.

Ces deux étapes sont matérialisées par deux outils complémentaires :

- **td-analyzer** : outil d’analyse statique et de recommandation ;
- **td-tool** : outil de transformation et de génération du projet exercice.

La communication entre les deux outils s’effectue via un fichier de configuration YAML, modifiable par l’enseignant.

---

## Structure du dépôt

td-generator-tool/
├── td-analyzer/ # Analyse statique et recommandation
├── td-tool/ # Transformation et génération de l’exercice
├── TP2-Solution-Full/ # Exemple de projet Java de référence
├── td-config.generated.yaml # Configuration générée par td-analyzer
└── README.md


---

## Prérequis

- Java JDK 17  
- Maven 3.6 ou supérieur  

---

## Outil 1 — td-analyzer (analyse et recommandation)

### Rôle

`td-analyzer` analyse statiquement un projet Java Maven et attribue un score à chaque méthode à partir de métriques structurelles simples (taille, structures de contrôle, dépendances, références dans les tests).

L’objectif n’est pas d’évaluer la qualité du code, mais d’identifier des méthodes susceptibles de constituer des points d’intérêt pédagogique lorsqu’elles sont partiellement ou totalement retirées.

Le résultat de l’analyse est un fichier YAML décrivant les méthodes sélectionnées et leurs caractéristiques.

---

### Modes d’analyse

Le comportement de l’analyse est contrôlé par un paramètre `mode`.

#### Mode `business` (par défaut)

- Ignore les méthodes considérées comme triviales :
  - getters (`getX`)
  - setters (`setX`)
  - méthodes booléennes simples (`isX`)
- Privilégie les méthodes contenant une logique applicative significative.

Ce mode est recommandé pour les projets applicatifs classiques.

#### Mode `any`

- Analyse l’ensemble des méthodes sans filtrage.
- Utile pour une analyse exhaustive ou pour des projets techniques ou atypiques.

---

### Compilation

Depuis le dossier `td-analyzer` :

mvn clean package


---

### Génération d’un fichier YAML

Exemple de commande :

java -jar target/td-analyzer-1.0.0-jar-with-dependencies.jar --config analyzer-config.yaml


Cette commande analyse le projet de référence et génère un fichier YAML décrivant les méthodes recommandées pour une transformation pédagogique.

---

## Outil 2 — td-tool (transformation du projet)

### Rôle

`td-tool` applique les transformations décrites dans un fichier YAML afin de produire :

- un projet Java Maven exercice, compilable mais partiellement incomplet ;
- un énoncé étudiant décrivant les méthodes à compléter.

---

### Fichier de configuration YAML

Le fichier YAML contient notamment :

- `input` : chemin du projet de référence ;
- `output` : chemin du projet exercice généré ;
- `methods` : liste des méthodes à transformer ;
- `cut` : type de coupure (`full` ou `partial`) ;
- `keepStatements` : nombre d’instructions conservées (si coupure partielle).

Un champ `score` peut être présent à titre informatif ; il est ignoré par `td-tool`.

---

### Compilation

Depuis le dossier `td-tool` :

mvn clean package


---

### Génération du projet exercice

java -jar target/td-tool-1.0.0-jar-with-dependencies.jar --config ../td-config.generated.yaml


Le projet exercice et l’énoncé sont générés dans le dossier spécifié dans la configuration.

---

## Énoncé étudiant

`td-tool` génère automatiquement :

- `ENONCE_TD.txt`
- `ENONCE_TD.pdf`

Ces fichiers listent les méthodes à compléter et précisent le cadre général du travail dirigé, sans divulguer l’implémentation originale.

---

## Workflow résumé

1. Analyse du projet de référence avec `td-analyzer`
2. Génération d’un fichier YAML de recommandations
3. Ajustement éventuel du YAML par l’enseignant
4. Génération du projet exercice avec `td-tool`
5. Distribution aux étudiants

---

## Conclusion

Ce projet propose une approche outillée, reproductible et configurable pour transformer des projets Java existants en exercices pédagogiques adaptés aux pratiques actuelles de développement.  
En séparant clairement l’analyse et la transformation, il offre un compromis entre automatisation et contrôle pédagogique, tout en favorisant un apprentissage centré sur la compréhension et la modification de code réel.




# Générateur d’exercices Java (TD Generator Tool)

Ce projet propose une chaîne d’outils pour la génération automatique d’exercices Java à partir d’un projet Maven existant servant de solution de référence.

Il repose sur deux outils complémentaires :

- **td-analyzer** : outil d’analyse statique qui recommande quelles méthodes sont pédagogiquement pertinentes à couper.
- **td-tool** : outil de transformation qui génère un projet exercice à partir d’une configuration YAML.

L’objectif est de faciliter la création de travaux dirigés en programmation Java tout en laissant à l’enseignant un contrôle précis sur le contenu pédagogique.

---

## Objectif pédagogique

L’objectif principal est de permettre à un enseignant de :

- partir d’une solution Java fonctionnelle existante ;
- identifier automatiquement les méthodes intéressantes à faire implémenter par les étudiants ;
- générer un projet exercice prêt à être distribué ;
- produire un énoncé étudiant clair et cohérent.

L’approche vise à être reproductible, configurable et indépendante du domaine applicatif.

---

## Structure générale du projet

```
td-generator-tool/
├── td-analyzer/            # Outil d’analyse et de recommandation
├── td-tool/                # Outil de génération du projet exercice
├── TP2-Solution-Full/      # Exemple de projet de référence
├── td-config.generated.yaml    # Configuration générée par l'outil analyzer
└── README.md
```

---

## Prérequis

- Java JDK 17  
- Maven 3.6 ou supérieur  

---

## Dépendances principales

Les outils s’appuient notamment sur :

- JavaParser : analyse statique du code source Java
- SnakeYAML : lecture et écriture des fichiers YAML
- Apache PDFBox : génération de l’énoncé étudiant (PDF)

---

## Outil 1 — td-analyzer (analyse et recommandation)

### Rôle

td-analyzer analyse statiquement un projet Java Maven et attribue un score pédagogique à chaque méthode selon plusieurs critères :

- taille et structure du code ;
- complexité (branches, boucles, exceptions, retours) ;
- dépendances internes ;
- présence de tests faisant référence à la méthode.

À partir de ces scores, l’outil génère automatiquement un fichier YAML directement exploitable par td-tool.

---

### Modes d’analyse

Le comportement de l’analyse peut être ajusté avec le paramètre `mode`.

#### Mode business (par défaut)

Ce mode filtre les méthodes peu pertinentes pédagogiquement, notamment :

- getters (`getX`)
- setters (`setX`)
- méthodes booléennes simples (`isX`)

L’objectif est de privilégier les méthodes contenant une logique métier réelle, plus intéressantes à implémenter pour les étudiants.

Ce mode est recommandé pour les projets applicatifs classiques.

#### Mode any

Dans ce mode, toutes les méthodes sont analysées sans filtrage.

Il est utile pour :

- une analyse exhaustive du projet ;
- des projets techniques ou atypiques ;
- laisser un contrôle maximal à l’enseignant lors de la sélection finale.

---

### Compilation de td-analyzer

Depuis le dossier `td-analyzer` :

```
mvn clean package
```

---

### Génération automatique d’un fichier YAML

Exemple de commande (sur une seule ligne) :

```
java -jar target\td-analyzer-1.0.0-jar-with-dependencies.jar --config analyzer-config.yaml

```

Cette commande analyse le projet, sélectionne les méthodes les mieux scorées et génère un fichier YAML prêt à l’emploi.

---

## Outil 2 — td-tool (génération du projet exercice)

### Rôle

td-tool prend en entrée un fichier YAML et :

1. copie intégralement le projet de référence ;
2. modifie les méthodes sélectionnées (coupure totale ou partielle) ;
3. conserve l’architecture Maven et les dépendances ;
4. génère un énoncé étudiant.

---

### Configuration YAML

Le fichier YAML contient notamment :

- `input` : chemin du projet de référence
- `output` : chemin du projet exercice généré
- `methods` : méthodes à modifier
- `cut` : `full` ou `partial`
- `keepStatements` : nombre d’instructions conservées (si partiel)

Un champ `score` peut être présent à titre informatif. Il est ignoré par td-tool s’il existe.

---

### Compilation de td-tool

Depuis le dossier `td-tool` :

```
mvn clean package
```

---

### Génération du projet exercice

```
java -jar target\td-tool-1.0.0-jar-with-dependencies.jar --config ..\td-config.generated.yaml

```

Le projet exercice est généré dans le dossier spécifié dans le fichier YAML.

---

## Énoncé étudiant

td-tool génère automatiquement :

- ENONCE_TD.txt
- ENONCE_TD.pdf

Ces fichiers décrivent les méthodes à compléter ainsi que les consignes générales.

---

## Fonctionnement global (résumé)

1. Analyse du projet de référence avec td-analyzer
2. Génération d’un fichier YAML de recommandations
3. Ajustement éventuel du YAML par l’enseignant
4. Génération du projet exercice avec td-tool
5. Distribution aux étudiants

---

## Conclusion

Cette chaîne d’outils propose une approche structurée, reproductible et configurable pour transformer un projet Java Maven existant en exercice pédagogique, tout en laissant à l’enseignant le contrôle final sur le contenu du travail dirigé.

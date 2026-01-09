# Générateur d’exercices Java (TD Tool)

Ce projet présente un outil de génération d’exercices Java à partir d’un projet Maven existant servant de solution de référence.

L’outil permet de transformer automatiquement un projet Java fonctionnel en projet pédagogique, en retirant le corps de certaines méthodes (au complet ou en partie) tout en conservant l’architecture du projet, les dépendances Maven et le reste du code source intact.

Un énoncé étudiant est également généré afin d’indiquer clairement les méthodes à compléter.

L’outil est conçu pour fonctionner avec tout projet Java Maven standard, indépendamment du domaine applicatif.

---

## Objectif pédagogique

L’objectif principal est de faciliter la création de travaux dirigés en programmation Java, en permettant à un enseignant de partir d’une solution existante, de contrôler précisément quelles méthodes doivent être réimplémentées par les étudiants, et de générer automatiquement un projet prêt à être distribué.

---

## Structure générale

```
td-generator-tool/
├── td-tool/               # Code source de l’outil
├── reference-project/     # Projet Java complet (solution)
├── student-exercise/      # Projet généré pour les étudiants
├── td-config.yaml         # Configuration de génération
└── README.md
```

---

## Prérequis

* Java JDK 17
* Maven 3.6 ou supérieur

---

## Dépendances principales

L’outil s’appuie notamment sur les bibliothèques suivantes :

* JavaParser (analyse et modification du code source Java)
* SnakeYAML (lecture du fichier de configuration)
* Apache PDFBox (génération de l’énoncé étudiant au format PDF)

---

## Compilation de l’outil

Depuis le dossier `td-tool` :

```
mvn clean package
```

Cette commande génère un JAR exécutable contenant toutes les dépendances nécessaires.

---

## Configuration YAML

La génération est pilotée par un fichier `td-config.yaml`.

Les paramètres principaux sont :

* `input` : chemin du projet Maven source
* `output` : chemin du projet exercice généré
* `methods` : liste des méthodes à modifier (coupure totale ou partielle)

Ce mécanisme permet de définir précisément le contenu pédagogique de l’exercice.

---

## Génération d’un projet exercice

Une fois l’outil compilé, la génération se fait avec la commande suivante :

```
java -jar target\td-tool-1.0.0-jar-with-dependencies.jar --config td-config.yaml

```

Le projet exercice est alors généré dans le dossier de sortie spécifié.

---

## Énoncé étudiant

L’outil génère automatiquement un énoncé destiné aux étudiants sous deux formats :

* ENONCE_TD.txt
* ENONCE_TD.pdf

Ces fichiers listent les méthodes à compléter ainsi que les consignes générales.

---

## Fonctionnement interne (résumé)

1. Copie complète du projet de référence
2. Analyse des fichiers Java
3. Modification ciblée des méthodes sélectionnées
4. Génération de l’énoncé étudiant

---

## Conclusion

Cet outil propose une approche simple et reproductible pour transformer un projet Java Maven existant en exercice pédagogique configurable, facilitant ainsi la création de travaux dirigés en programmation Java.

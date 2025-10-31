package com.halloween.officine;

import java.util.*;
import java.util.regex.*;

public class Officine {
    private Map<String, Integer> stocks = new HashMap<>();
    private Map<String, Map<String, Integer>> recettes = new HashMap<>();

    public Officine() {
        // Définition des recettes
        definirRecettes();
    }

    // --- Méthodes publiques ---

    /** Ajoute des ingrédients au stock, ex : "3 yeux de grenouille" */
    public void rentrer(String texte) {
        Map.Entry<Integer, String> parsed = parseQuantiteEtNom(texte);
        int quantite = parsed.getKey();
        String nom = normaliserNom(parsed.getValue());
        stocks.put(nom, stocks.getOrDefault(nom, 0) + quantite);
    }

    /** Retourne la quantité en stock pour un ingrédient ou une potion */
    public int quantite(String nom) {
        nom = normaliserNom(nom);
        return stocks.getOrDefault(nom, 0);
    }

    /** Prépare une potion selon la recette et les stocks disponibles */
    public int preparer(String texte) {
        Map.Entry<Integer, String> parsed = parseQuantiteEtNom(texte);
        int nbAPreparer = parsed.getKey();
        String potion = normaliserNom(parsed.getValue());

        if (!recettes.containsKey(potion)) {
            System.out.println("Aucune recette connue pour " + potion);
            return 0;
        }

        Map<String, Integer> recette = recettes.get(potion);

        // Vérifie combien d'unités on peut réellement préparer
        int maxPossible = Integer.MAX_VALUE;
        for (Map.Entry<String, Integer> e : recette.entrySet()) {
            String ingr = e.getKey();
            int besoin = e.getValue();
            int dispo = stocks.getOrDefault(ingr, 0);
            maxPossible = Math.min(maxPossible, dispo / besoin);
        }

        int nbEffectif = Math.min(nbAPreparer, maxPossible);

        if (nbEffectif == 0) return 0;

        // Consommer les ingrédients
        for (Map.Entry<String, Integer> e : recette.entrySet()) {
            String ingr = e.getKey();
            int besoin = e.getValue() * nbEffectif;
            stocks.put(ingr, stocks.get(ingr) - besoin);
        }

        // Ajouter la potion produite
        stocks.put(potion, stocks.getOrDefault(potion, 0) + nbEffectif);

        return nbEffectif;
    }

    // --- Méthodes privées ---

    /** Définition des recettes */
    private void definirRecettes() {
        recettes.put("fiole de glaires purulentes", Map.of(
                normaliserNom("larme de brume funèbre"), 2,
                normaliserNom("goutte de sang de citrouille"), 1
        ));

        recettes.put("bille d'âme évanescente", Map.of(
                normaliserNom("pincée de poudre de lune"), 3,
                normaliserNom("œil de grenouille"), 1
        ));

        recettes.put("soupçon de sels suffocants", Map.of(
                normaliserNom("croc de troll"), 2,
                normaliserNom("fragment d'écaille de dragonnet"), 1,
                normaliserNom("radicelle de racine hurlante"), 1
        ));

        recettes.put("baton de pâte sépulcrale", Map.of(
                normaliserNom("radicelle de racine hurlante"), 3,
                normaliserNom("fiole de glaires purulentes"), 1
        ));

        recettes.put("bouffée d'essence de cauchemar", Map.of(
                normaliserNom("pincée de poudre de lune"), 2,
                normaliserNom("larme de brume funèbre"), 2
        ));
    }

    /** Parse une chaîne du type "3 yeux de grenouille" */
    private Map.Entry<Integer, String> parseQuantiteEtNom(String texte) {
        Pattern p = Pattern.compile("(\\d+)\\s+(.+)");
        Matcher m = p.matcher(texte.trim());
        if (!m.matches()) throw new IllegalArgumentException("Format invalide : " + texte);
        int qte = Integer.parseInt(m.group(1));
        String nom = m.group(2).trim();
        return Map.entry(qte, nom);
    }

    /** Normalise le nom (singulier/pluriel) pour uniformiser les clés */
    private String normaliserNom(String nom) {
        nom = nom.toLowerCase(Locale.ROOT).trim();

        // Corrige quelques pluriels connus
        nom = nom
                .replace("yeux de grenouille", "œil de grenouille")
                .replace("œils de grenouille", "œil de grenouille")
                .replace("larmes de brume funèbre", "larme de brume funèbre")
                .replace("pincées de poudre de lune", "pincée de poudre de lune")
                .replace("crocs de troll", "croc de troll")
                .replace("fragments d'écaille de dragonnet", "fragment d'écaille de dragonnet")
                .replace("radicelles de racine hurlante", "radicelle de racine hurlante")
                .replace("gouttes de sang de citrouille", "goutte de sang de citrouille")
                .replace("fioles de glaires purulentes", "fiole de glaires purulentes");

        return nom;
    }

    // --- Démo rapide ---
    public static void main(String[] args) {
        Officine o = new Officine();
        o.rentrer("3 yeux de grenouille");
        o.rentrer("4 larmes de brume funèbre");
        o.rentrer("2 gouttes de sang de citrouille");

        System.out.println("Grenouilles : " + o.quantite("œil de grenouille"));
        int nb = o.preparer("2 fioles de glaires purulentes");
        System.out.println("Préparé : " + nb + " fioles");
        System.out.println("Reste larmes : " + o.quantite("larme de brume funèbre"));
        System.out.println("Stock de fioles : " + o.quantite("fiole de glaires purulentes"));
    }
}

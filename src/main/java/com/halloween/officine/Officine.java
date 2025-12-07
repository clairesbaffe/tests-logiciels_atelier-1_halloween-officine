package com.halloween.officine;

import java.util.*;
import java.util.regex.*;

public class Officine {
    private Map<String, Integer> stocks = new HashMap<>();
    private Map<String, Map<String, Integer>> recettes = new HashMap<>();
    private Set<String> ingredientsAutorises = new HashSet<>();

    public Map<String, Map<String, Integer>> getRecettes() {
        return Collections.unmodifiableMap(recettes);
    }

    public Map<String, Integer> getStocks() {
        return Collections.unmodifiableMap(stocks);
    }

    public Officine() {
        definirRecettes();
    }


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

        for (Map.Entry<String, Map<String, Integer>> entry : recettes.entrySet()) {
            ingredientsAutorises.add(entry.getKey());
            ingredientsAutorises.addAll(entry.getValue().keySet());
        }
    }

    public void rentrer(String texte) {
        Map.Entry<Integer, String> parsed = parseQuantiteEtNom(texte);
        int quantite = parsed.getKey();
        String nom = normaliserNom(parsed.getValue());
        validerIngredientAutorise(nom);

        if (stocks.getOrDefault(nom, 0) + quantite < 0) {
            throw new RuntimeException("Quantité à retirer de " + nom + " > stock");
        }

        stocks.put(nom, stocks.getOrDefault(nom, 0) + quantite);
    }

    public int quantite(String nom) {
        nom = normaliserNom(nom);
        validerIngredientAutorise(nom);
        return stocks.getOrDefault(nom, 0);
    }

    public int preparer(String texte) {
        Map.Entry<Integer, String> parsed = parseQuantiteEtNom(texte);
        int nbDemande = parsed.getKey();

        if(nbDemande < 0){
            throw new RuntimeException("Le nombre de potions à préparer est négatif");
        } else if (nbDemande == 0) {
            return 0;
        }

        String potion = normaliserNom(parsed.getValue());

        if (!recettes.containsKey(potion)) {
            throw new RuntimeException("Aucune recette connue pour " + potion);
        }

        int nbFaisable = calculerMaxProductible(potion, new HashSet<>());

        if (nbFaisable == 0) {
            throw new RuntimeException("Impossible de préparer " + potion + " : ingrédients insuffisants.");
        }

        int aProduire = Math.min(nbDemande, nbFaisable);

        if (aProduire > 0) {
            produireReellement(potion, aProduire);
        }

        return aProduire;
    }

    private void produireReellement(String potion, int quantiteAProduire) {
        Map<String, Integer> recette = recettes.get(potion);

        for (Map.Entry<String, Integer> e : recette.entrySet()) {
            String ingr = e.getKey();
            int qteTotaleRequise = e.getValue() * quantiteAProduire;
            int stockActuel = stocks.getOrDefault(ingr, 0);

            if (stockActuel < qteTotaleRequise) {
                if (recettes.containsKey(ingr)) {
                    int manque = qteTotaleRequise - stockActuel;
                    produireReellement(ingr, manque);
                } else {
                    throw new IllegalStateException("Stock insuffisant pour " + ingr + " et pas de recette.");
                }
            }

            stocks.put(ingr, stocks.get(ingr) - qteTotaleRequise);
        }

        stocks.put(potion, stocks.getOrDefault(potion, 0) + quantiteAProduire);
    }

    private Map.Entry<Integer, String> parseQuantiteEtNom(String texte) {
        Pattern p = Pattern.compile("(-?\\d+)\\s+(.+)");
        Matcher m = p.matcher(texte.trim());
        if (!m.matches()) throw new IllegalArgumentException("Format invalide : " + texte);
        int qte = Integer.parseInt(m.group(1));
        String nom = m.group(2).trim();
        return Map.entry(qte, nom);
    }

    private void validerIngredientAutorise(String nom) {
        if (!ingredientsAutorises.contains(nom)) {
            throw new RuntimeException("Ingrédient inconnu ou non autorisé dans l'officine : " + nom);
        }
    }

    private String normaliserNom(String nom) {
        nom = nom.toLowerCase(Locale.ROOT).trim();

        nom = nom
                .replace("oeil de grenouille", "œil de grenouille")
                .replace("yeux de grenouille", "œil de grenouille")
                .replace("œils de grenouille", "œil de grenouille")

                .replace("larmes de brume funèbre", "larme de brume funèbre")
                .replace("pincées de poudre de lune", "pincée de poudre de lune")
                .replace("crocs de troll", "croc de troll")
                .replace("fragments d'écaille de dragonnet", "fragment d'écaille de dragonnet")
                .replace("radicelles de racine hurlante", "radicelle de racine hurlante")
                .replace("gouttes de sang de citrouille", "goutte de sang de citrouille")

                .replace("fioles de glaires purulentes", "fiole de glaires purulentes")
                .replace("billes d'âme évanescente", "bille d'âme évanescente")
                .replace("soupçons de sels suffocants", "soupçon de sels suffocants")
                .replace("batons de pâte sépulcrale", "baton de pâte sépulcrale")
                .replace("bâtons de pâte sépulcrale", "baton de pâte sépulcrale")
                .replace("bouffées d'essence de cauchemar", "bouffée d'essence de cauchemar");

        return nom;
    }

    private int calculerMaxProductible(String nom, Set<String> enCours) {
        if (enCours.contains(nom)) {
            return 0;
        }

        if (!recettes.containsKey(nom)) {
            return stocks.getOrDefault(nom, 0);
        }

        enCours.add(nom);
        Map<String, Integer> recette = recettes.get(nom);
        int maxPourCettePotion = Integer.MAX_VALUE;

        for (Map.Entry<String, Integer> e : recette.entrySet()) {
            String ingr = e.getKey();
            int qteRequiseParUnite = e.getValue();

            int stockDirect = stocks.getOrDefault(ingr, 0);
            int potentielDeCraft = 0;

            if (recettes.containsKey(ingr)) {
                potentielDeCraft = calculerMaxProductible(ingr, enCours);
            }

            int totalDisponible = stockDirect + potentielDeCraft;

            if (qteRequiseParUnite > 0) {
                int faisableAvecCetIngr = totalDisponible / qteRequiseParUnite;
                maxPourCettePotion = Math.min(maxPourCettePotion, faisableAvecCetIngr);
            }
        }

        enCours.remove(nom);

        return maxPourCettePotion;
    }

    public static void main(String[] args) {
        Officine o = new Officine();
        o.rentrer("3 yeux de grenouille");
        o.rentrer("4 larmes de brume funèbre");
        o.rentrer("2 gouttes de sang de citrouille");
//        o.rentrer("-4 radicelles de racine hurlante");

        System.out.println("Grenouilles : " + o.quantite("œil de grenouille"));
        int nb = o.preparer("2 fioles de glaires purulentes");
        System.out.println("Préparé : " + nb + " fioles");
        System.out.println("Reste larmes : " + o.quantite("larme de brume funèbre"));
        System.out.println("Stock de fioles : " + o.quantite("fiole de glaires purulentes"));
    }
}

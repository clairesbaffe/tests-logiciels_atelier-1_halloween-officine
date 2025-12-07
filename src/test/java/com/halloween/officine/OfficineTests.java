package com.halloween.officine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OfficineTests {

    @Test
    void initOfficine() {
        var test = new Officine();

        assertTrue(test.getStocks().isEmpty());

        var recettes = test.getRecettes();
        assertEquals(5, recettes.size());

        assertTrue(recettes.containsKey("fiole de glaires purulentes"));
        assertTrue(recettes.containsKey("bille d'âme évanescente"));
        assertTrue(recettes.containsKey("soupçon de sels suffocants"));
        assertTrue(recettes.containsKey("baton de pâte sépulcrale"));
        assertTrue(recettes.containsKey("bouffée d'essence de cauchemar"));
    }

    @Test
    void ajouterIngredients() {
        var test = new Officine();

        test.rentrer("3 yeux de grenouille");

        assertEquals(3, test.quantite("yeux de grenouille"));
    }

    @Test
    void retirerIngredients(){
        var test = new Officine();

        test.rentrer("3 yeux de grenouille");
        test.rentrer("-2 yeux de grenouille");

        assertEquals(1, test.quantite("yeux de grenouille"));
    }

    @Test
    void ajouterZeroIngredients() {
        var test = new Officine();

        test.rentrer("0 yeux de grenouille");

        assertEquals(0, test.quantite("yeux de grenouille"));
    }

    @Test
    void retirerNombreSuperieurStockIngredients(){
        var test = new Officine();

        assertThrows(RuntimeException.class, () -> {
            test.rentrer("-2 yeux de grenouille");
        });
    }

    @Test
    void ajouterIngredientInexistant(){
        var test = new Officine();

        assertThrows(RuntimeException.class, () -> {
            test.rentrer("3 yeux de vipère");
        });
    }

    @Test
    void ajouterIngredientPlurielSimple() {
        var test = new Officine();

        test.rentrer("4 larmes de brume funèbre");

        assertEquals(4, test.quantite("larmes de brume funèbre"));
    }

    @Test
    void ajouterIngredientPlurielComplexe() {
        var test = new Officine();

        test.rentrer("3 yeux de grenouille");

        assertEquals(3, test.quantite("yeux de grenouille"));
    }

    @Test
    void ajouterIngredientSingulierComplexe() {
        var test = new Officine();

        test.rentrer("1 oeil de grenouille");

        assertEquals(1, test.quantite("yeux de grenouille"));
    }

    @Test
    void recupererQuantiteIngredientSingulier(){
        var test = new Officine();

        test.rentrer("3 yeux de grenouille");

        assertEquals(3, test.quantite("œil de grenouille"));
    }

    @Test
    void recupererQuantiteIngredientPluriel(){
        var test = new Officine();

        test.rentrer("3 yeux de grenouille");

        assertEquals(3, test.quantite("yeux de grenouille"));
    }

    @Test
    void recupererQuantiteIngredientInexistant(){
        var test = new Officine();

        assertThrows(RuntimeException.class, () -> {
            test.quantite("yeux de vipère");
        });
    }

    @Test
    void preparerUnePotionSimpleAvecIngredientsEnStock(){
        var test = new Officine();

        test.rentrer("3 larmes de brume funèbre");
        test.rentrer("4 gouttes de sang de citrouille");

        test.preparer("1 fiole de glaires purulentes");

        assertEquals(1, test.quantite("larmes de brume funèbre"));
        assertEquals(3, test.quantite("gouttes de sang de citrouille"));
        assertEquals(1, test.quantite("fiole de glaires purulentes"));
    }

    @Test
    void preparerPlusieursPotionsSimplesAvecIngredientsEnStock(){
        var test = new Officine();

        test.rentrer("7 larmes de brume funèbre");
        test.rentrer("6 gouttes de sang de citrouille");

        test.preparer("3 fioles de glaires purulentes");

        assertEquals(1, test.quantite("larmes de brume funèbre"));
        assertEquals(3, test.quantite("gouttes de sang de citrouille"));
        assertEquals(3, test.quantite("fiole de glaires purulentes"));
    }

    @Test
    void preparerZeroPotion(){
        var test = new Officine();

        test.preparer("0 fioles de glaires purulentes");

        assertEquals(0, test.quantite("larmes de brume funèbre"));
        assertEquals(0, test.quantite("gouttes de sang de citrouille"));
        assertEquals(0, test.quantite("fiole de glaires purulentes"));
    }

    @Test
    void preparerNbNegatifPotion(){
        var test = new Officine();

        assertThrows(RuntimeException.class, () -> {
            test.preparer("-2 fioles de glaires purulentes");
        });
    }

    @Test
    void preparerPotionInexistante(){
        var test = new Officine();

        assertThrows(RuntimeException.class, () -> {
            test.preparer("2 bouteilles de glaires purulentes");
        });
    }

    @Test
    void preparerUnePotionSimpleAvecIngredientsManquants(){
        var test = new Officine();

        test.rentrer("1 larmes de brume funèbre");
        test.rentrer("4 gouttes de sang de citrouille");

        assertThrows(RuntimeException.class, () -> {
            test.preparer("1 fiole de glaires purulentes");
        });
        // On ne peut préparer aucune potion demandée

        assertEquals(1, test.quantite("larmes de brume funèbre"));
        assertEquals(4, test.quantite("gouttes de sang de citrouille"));
        assertEquals(0, test.quantite("fiole de glaires purulentes"));
    }

    @Test
    void preparerPlusieursPotionsSimplesAvecIngredientsManquants(){
        var test = new Officine();

        test.rentrer("5 larmes de brume funèbre");
        test.rentrer("6 gouttes de sang de citrouille");

        test.preparer("3 fioles de glaires purulentes");
        // On ne peut préparer que 2 potions demandées
        // Pas d'erreur levée

        assertEquals(1, test.quantite("larmes de brume funèbre"));
        assertEquals(4, test.quantite("gouttes de sang de citrouille"));
        assertEquals(2, test.quantite("fiole de glaires purulentes"));
    }

    @Test
    void preparerUnePotionComplexeAvecIngredientsEnStock(){
        var test = new Officine();

        test.rentrer("3 larmes de brume funèbre");
        test.rentrer("1 gouttes de sang de citrouille");
        test.preparer("1 fioles de glaires purulentes");
        test.rentrer("4 radicelles de racine hurlante");

        test.preparer("1 baton de pâte sépulcrale");

        assertEquals(1, test.quantite("larmes de brume funèbre"));
        assertEquals(0, test.quantite("gouttes de sang de citrouille"));
        assertEquals(0, test.quantite("fiole de glaires purulentes"));
        assertEquals(1, test.quantite("radicelles de racine hurlante"));
        assertEquals(1, test.quantite("baton de pâte sépulcrale"));
    }

    @Test
    void preparerPlusieursPotionsComplexesAvecIngredientsEnStock(){
        var test = new Officine();

        test.rentrer("10 larmes de brume funèbre");
        test.rentrer("4 gouttes de sang de citrouille");
        test.preparer("4 fioles de glaires purulentes");
        test.rentrer("12 radicelles de racine hurlante");

        test.preparer("3 baton de pâte sépulcrale");

        assertEquals(2, test.quantite("larmes de brume funèbre"));
        assertEquals(0, test.quantite("gouttes de sang de citrouille"));
        assertEquals(1, test.quantite("fiole de glaires purulentes"));
        assertEquals(3, test.quantite("radicelles de racine hurlante"));
        assertEquals(3, test.quantite("baton de pâte sépulcrale"));
    }

    @Test
    void preparerUnePotionComplexeAvecPotionManquanteMaisIngredientsEnStock(){
        var test = new Officine();

        test.rentrer("3 larmes de brume funèbre");
        test.rentrer("1 gouttes de sang de citrouille");
        test.rentrer("4 radicelles de racine hurlante");

        test.preparer("1 baton de pâte sépulcrale");

        assertEquals(1, test.quantite("larmes de brume funèbre"));
        assertEquals(0, test.quantite("gouttes de sang de citrouille"));
        assertEquals(0, test.quantite("fiole de glaires purulentes"));
        assertEquals(1, test.quantite("radicelles de racine hurlante"));
        assertEquals(1, test.quantite("baton de pâte sépulcrale"));
    }

    @Test
    void preparerPlusieursPotionsComplexesAvecPotionManquanteMaisIngredientsEnStock(){
        var test = new Officine();

        test.rentrer("10 larmes de brume funèbre");
        test.rentrer("4 gouttes de sang de citrouille");
        test.rentrer("12 radicelles de racine hurlante");

        test.preparer("3 baton de pâte sépulcrale");

        assertEquals(4, test.quantite("larmes de brume funèbre"));
        assertEquals(1, test.quantite("gouttes de sang de citrouille"));
        assertEquals(0, test.quantite("fiole de glaires purulentes"));
        assertEquals(3, test.quantite("radicelles de racine hurlante"));
        assertEquals(3, test.quantite("baton de pâte sépulcrale"));
    }

    @Test
    void preparerUnePotionComplexeAvecIngredientsDePotionManquants(){
        var test = new Officine();

        test.rentrer("1 larmes de brume funèbre");
        test.rentrer("1 gouttes de sang de citrouille");
        test.rentrer("4 radicelles de racine hurlante");

        assertThrows(RuntimeException.class, () -> {
            test.preparer("1 baton de pâte sépulcrale");
        });
        // On ne peut faire aucune potion demandée

        assertEquals(1, test.quantite("larmes de brume funèbre"));
        assertEquals(1, test.quantite("gouttes de sang de citrouille"));
        assertEquals(0, test.quantite("fiole de glaires purulentes"));
        assertEquals(4, test.quantite("radicelles de racine hurlante"));
        assertEquals(0, test.quantite("baton de pâte sépulcrale"));
    }

    @Test
    void preparerPlusieursPotionsComplexesAvecIngredientsDePotionManquants(){
        var test = new Officine();

        test.rentrer("4 larmes de brume funèbre");
        test.rentrer("4 gouttes de sang de citrouille");
        test.rentrer("12 radicelles de racine hurlante");

        test.preparer("3 baton de pâte sépulcrale");
        // On ne peut faire que 2 potions demandées

        assertEquals(0, test.quantite("larmes de brume funèbre"));
        assertEquals(2, test.quantite("gouttes de sang de citrouille"));
        assertEquals(0, test.quantite("fiole de glaires purulentes"));
        assertEquals(6, test.quantite("radicelles de racine hurlante"));
        assertEquals(2, test.quantite("baton de pâte sépulcrale"));
    }

    @Test
    void preparerUnePotionComplexeAvecIngredientsManquants(){
        var test = new Officine();

        test.rentrer("3 larmes de brume funèbre");
        test.rentrer("1 gouttes de sang de citrouille");

        assertThrows(RuntimeException.class, () -> {
            test.preparer("1 baton de pâte sépulcrale");
        });
        // On ne peut faire aucune potion demandée
        // Mais on ne fait aucune fiole non plus

        assertEquals(3, test.quantite("larmes de brume funèbre"));
        assertEquals(1, test.quantite("gouttes de sang de citrouille"));
        assertEquals(0, test.quantite("fiole de glaires purulentes"));
        assertEquals(0, test.quantite("radicelles de racine hurlante"));
        assertEquals(0, test.quantite("baton de pâte sépulcrale"));
    }

    @Test
    void preparerPlusieursPotionsComplexesAvecIngredientsManquants(){
        var test = new Officine();

        test.rentrer("10 larmes de brume funèbre");
        test.rentrer("4 gouttes de sang de citrouille");
        test.rentrer("7 radicelles de racine hurlante");

        test.preparer("3 baton de pâte sépulcrale");
        // On ne peut faire que 2 potions demandées
        // On ne fait donc que 2 fioles même si on pourrait en faire 3

        assertEquals(6, test.quantite("larmes de brume funèbre"));
        assertEquals(2, test.quantite("gouttes de sang de citrouille"));
        assertEquals(0, test.quantite("fiole de glaires purulentes"));
        assertEquals(1, test.quantite("radicelles de racine hurlante"));
        assertEquals(2, test.quantite("baton de pâte sépulcrale"));
    }
}

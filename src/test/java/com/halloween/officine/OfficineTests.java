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

        var quantite = test.quantite("yeux de grenouille");

        assertEquals(3, quantite);
    }

    @Test
    void retirerIngredients(){
        var test = new Officine();

        test.rentrer("3 yeux de grenouille");

        test.rentrer("-2 yeux de grenouille");

        var quantite = test.quantite("yeux de grenouille");

        assertEquals(1, quantite);
    }

    @Test
    void ajouterZeroIngredients() {
        var test = new Officine();

        test.rentrer("0 yeux de grenouille");

        var quantite = test.quantite("yeux de grenouille");

        assertEquals(0, quantite);
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

        var quantite = test.quantite("larmes de brume funèbre");

        assertEquals(4, quantite);
    }

    @Test
    void ajouterIngredientPlurielComplexe() {
        var test = new Officine();

        test.rentrer("3 yeux de grenouille");

        var quantite = test.quantite("yeux de grenouille");

        assertEquals(3, quantite);
    }

    @Test
    void ajouterIngredientSingulierComplexe() {
        var test = new Officine();

        test.rentrer("1 oeil de grenouille");

        var quantite = test.quantite("yeux de grenouille");

        assertEquals(1, quantite);
    }

    @Test
    void recupererQuantiteIngredientSingulier(){
        var test = new Officine();

        test.rentrer("3 yeux de grenouille");

        var quantite = test.quantite("œil de grenouille");

        assertEquals(3, quantite);
    }

    @Test
    void recupererQuantiteIngredientPluriel(){
        var test = new Officine();

        test.rentrer("3 yeux de grenouille");

        var quantite = test.quantite("yeux de grenouille");

        assertEquals(3, quantite);
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

        var quantiteLarmes = test.quantite("larmes de brume funèbre");
        var quantiteGouttes = test.quantite("gouttes de sang de citrouille");
        var quantiteFioles = test.quantite("fiole de glaires purulentes");

        assertEquals(1, quantiteLarmes);
        assertEquals(3, quantiteGouttes);
        assertEquals(1, quantiteFioles);
    }

    @Test
    void preparerPlusieursPotionsSimplesAvecIngredientsEnStock(){
        var test = new Officine();

        test.rentrer("7 larmes de brume funèbre");
        test.rentrer("6 gouttes de sang de citrouille");

        test.preparer("3 fioles de glaires purulentes");

        var quantiteLarmes = test.quantite("larmes de brume funèbre");
        var quantiteGouttes = test.quantite("gouttes de sang de citrouille");
        var quantiteFioles = test.quantite("fiole de glaires purulentes");

        assertEquals(1, quantiteLarmes);
        assertEquals(3, quantiteGouttes);
        assertEquals(3, quantiteFioles);
    }

    @Test
    void preparerZeroPotion(){
        var test = new Officine();

        test.preparer("0 fioles de glaires purulentes");

        var quantiteLarmes = test.quantite("larmes de brume funèbre");
        var quantiteGouttes = test.quantite("gouttes de sang de citrouille");
        var quantiteFioles = test.quantite("fiole de glaires purulentes");

        assertEquals(0, quantiteLarmes);
        assertEquals(0, quantiteGouttes);
        assertEquals(0, quantiteFioles);
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

        var quantiteLarmes = test.quantite("larmes de brume funèbre");
        var quantiteGouttes = test.quantite("gouttes de sang de citrouille");
        var quantiteFioles = test.quantite("fiole de glaires purulentes");

        assertEquals(1, quantiteLarmes);
        assertEquals(4, quantiteGouttes);
        assertEquals(0, quantiteFioles);
    }

    @Test
    void preparerPlusieursPotionsSimplesAvecIngredientsManquants(){
        var test = new Officine();

        test.rentrer("5 larmes de brume funèbre");
        test.rentrer("6 gouttes de sang de citrouille");

        test.preparer("3 fioles de glaires purulentes");

        // on ne peut préparer que 2 potions
        // pas d'erreur levée

        var quantiteLarmes = test.quantite("larmes de brume funèbre");
        var quantiteGouttes = test.quantite("gouttes de sang de citrouille");
        var quantiteFioles = test.quantite("fiole de glaires purulentes");

        assertEquals(1, quantiteLarmes);
        assertEquals(4, quantiteGouttes);
        assertEquals(2, quantiteFioles);
    }

    @Test
    void preparerUnePotionComplexeAvecIngredientsEnStock(){
        var test = new Officine();

        test.rentrer("3 larmes de brume funèbre");
        test.rentrer("1 gouttes de sang de citrouille");
        test.preparer("1 fioles de glaires purulentes");
        test.rentrer("4 radicelles de racine hurlante");

        test.preparer("1 baton de pâte sépulcrale");

        var quantiteLarmes = test.quantite("larmes de brume funèbre");
        var quantiteGouttes = test.quantite("gouttes de sang de citrouille");
        var quantiteFioles = test.quantite("fiole de glaires purulentes");
        var quantiteRadicelles = test.quantite("radicelles de racine hurlante");
        var quantiteBatons = test.quantite("baton de pâte sépulcrale");

        assertEquals(1, quantiteLarmes);
        assertEquals(0, quantiteGouttes);
        assertEquals(0, quantiteFioles);
        assertEquals(1, quantiteRadicelles);
        assertEquals(1, quantiteBatons);
    }

    @Test
    void preparerPlusieursPotionsComplexesAvecIngredientsEnStock(){
        var test = new Officine();

        test.rentrer("10 larmes de brume funèbre");
        test.rentrer("4 gouttes de sang de citrouille");
        test.preparer("4 fioles de glaires purulentes");
        test.rentrer("12 radicelles de racine hurlante");

        test.preparer("3 baton de pâte sépulcrale");

        var quantiteLarmes = test.quantite("larmes de brume funèbre");
        var quantiteGouttes = test.quantite("gouttes de sang de citrouille");
        var quantiteFioles = test.quantite("fiole de glaires purulentes");
        var quantiteRadicelles = test.quantite("radicelles de racine hurlante");
        var quantiteBatons = test.quantite("baton de pâte sépulcrale");

        assertEquals(2, quantiteLarmes);
        assertEquals(0, quantiteGouttes);
        assertEquals(1, quantiteFioles);
        assertEquals(3, quantiteRadicelles);
        assertEquals(3, quantiteBatons);
    }

    @Test
    void preparerUnePotionComplexeAvecPotionManquanteMaisIngredientsEnStock(){
        var test = new Officine();

        test.rentrer("3 larmes de brume funèbre");
        test.rentrer("1 gouttes de sang de citrouille");
        test.rentrer("4 radicelles de racine hurlante");

        test.preparer("1 baton de pâte sépulcrale");

        var quantiteLarmes = test.quantite("larmes de brume funèbre");
        var quantiteGouttes = test.quantite("gouttes de sang de citrouille");
        var quantiteFioles = test.quantite("fiole de glaires purulentes");
        var quantiteRadicelles = test.quantite("radicelles de racine hurlante");
        var quantiteBatons = test.quantite("baton de pâte sépulcrale");

        assertEquals(1, quantiteLarmes);
        assertEquals(0, quantiteGouttes);
        assertEquals(0, quantiteFioles);
        assertEquals(1, quantiteRadicelles);
        assertEquals(1, quantiteBatons);
    }

    @Test
    void preparerPlusieursPotionsComplexesAvecPotionManquanteMaisIngredientsEnStock(){
        var test = new Officine();

        test.rentrer("10 larmes de brume funèbre");
        test.rentrer("4 gouttes de sang de citrouille");
        test.rentrer("12 radicelles de racine hurlante");

        test.preparer("3 baton de pâte sépulcrale");

        var quantiteLarmes = test.quantite("larmes de brume funèbre");
        var quantiteGouttes = test.quantite("gouttes de sang de citrouille");
        var quantiteFioles = test.quantite("fiole de glaires purulentes");
        var quantiteRadicelles = test.quantite("radicelles de racine hurlante");
        var quantiteBatons = test.quantite("baton de pâte sépulcrale");

        assertEquals(4, quantiteLarmes);
        assertEquals(1, quantiteGouttes);
        assertEquals(0, quantiteFioles);
        assertEquals(3, quantiteRadicelles);
        assertEquals(3, quantiteBatons);
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
        // on ne peut pas en faire

        var quantiteLarmes = test.quantite("larmes de brume funèbre");
        var quantiteGouttes = test.quantite("gouttes de sang de citrouille");
        var quantiteFioles = test.quantite("fiole de glaires purulentes");
        var quantiteRadicelles = test.quantite("radicelles de racine hurlante");
        var quantiteBatons = test.quantite("baton de pâte sépulcrale");

        assertEquals(1, quantiteLarmes);
        assertEquals(1, quantiteGouttes);
        assertEquals(0, quantiteFioles);
        assertEquals(4, quantiteRadicelles);
        assertEquals(0, quantiteBatons);
    }

    @Test
    void preparerPlusieursPotionsComplexesAvecIngredientsDePotionManquants(){
        var test = new Officine();

        test.rentrer("4 larmes de brume funèbre");
        test.rentrer("4 gouttes de sang de citrouille");
        test.rentrer("12 radicelles de racine hurlante");

        test.preparer("3 baton de pâte sépulcrale");
        // on ne peut en faire que 2

        var quantiteLarmes = test.quantite("larmes de brume funèbre");
        var quantiteGouttes = test.quantite("gouttes de sang de citrouille");
        var quantiteFioles = test.quantite("fiole de glaires purulentes");
        var quantiteRadicelles = test.quantite("radicelles de racine hurlante");
        var quantiteBatons = test.quantite("baton de pâte sépulcrale");

        assertEquals(0, quantiteLarmes);
        assertEquals(2, quantiteGouttes);
        assertEquals(0, quantiteFioles);
        assertEquals(6, quantiteRadicelles);
        assertEquals(2, quantiteBatons);
    }

    @Test
    void preparerUnePotionComplexeAvecIngredientsManquants(){
        var test = new Officine();

        test.rentrer("3 larmes de brume funèbre");
        test.rentrer("1 gouttes de sang de citrouille");

        assertThrows(RuntimeException.class, () -> {
            test.preparer("1 baton de pâte sépulcrale");
        });
        // on ne peut pas en faire
        // mais on ne fait pas de fioles non plus

        var quantiteLarmes = test.quantite("larmes de brume funèbre");
        var quantiteGouttes = test.quantite("gouttes de sang de citrouille");
        var quantiteFioles = test.quantite("fiole de glaires purulentes");
        var quantiteRadicelles = test.quantite("radicelles de racine hurlante");
        var quantiteBatons = test.quantite("baton de pâte sépulcrale");

        assertEquals(3, quantiteLarmes);
        assertEquals(1, quantiteGouttes);
        assertEquals(0, quantiteFioles);
        assertEquals(0, quantiteRadicelles);
        assertEquals(0, quantiteBatons);
    }

    @Test
    void preparerPlusieursPotionsComplexesAvecIngredientsManquants(){
        var test = new Officine();

        test.rentrer("10 larmes de brume funèbre");
        test.rentrer("4 gouttes de sang de citrouille");
        test.rentrer("7 radicelles de racine hurlante");

        test.preparer("3 baton de pâte sépulcrale");
        // on ne peut en faire que 2
        // on ne fait donc que 2 fioles même si on pourrait en faire 3

        var quantiteLarmes = test.quantite("larmes de brume funèbre");
        var quantiteGouttes = test.quantite("gouttes de sang de citrouille");
        var quantiteFioles = test.quantite("fiole de glaires purulentes");
        var quantiteRadicelles = test.quantite("radicelles de racine hurlante");
        var quantiteBatons = test.quantite("baton de pâte sépulcrale");

        assertEquals(6, quantiteLarmes);
        assertEquals(2, quantiteGouttes);
        assertEquals(0, quantiteFioles);
        assertEquals(1, quantiteRadicelles);
        assertEquals(2, quantiteBatons);
    }
}

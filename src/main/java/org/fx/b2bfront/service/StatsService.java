package org.fx.b2bfront.service;

import org.fx.b2bfront.model.*;

import java.util.Arrays;
import java.util.List;

public class StatsService {

    // ------------ NOMBRE ------------------

    public int getUsersCount() {
        return 128;   // mock
    }

    public int getProductsCount() {
        return 542;   // mock
    }

    public int getOrdersCount() {
        return 89;    // mock
    }


    // ------------ TOP SELLERS ------------------

    public List<CompanyStats> getTop3Sellers() {
        return Arrays.asList(
                new CompanyStats(1L,"AtlasSteel", 194000.0),
                new CompanyStats(2L,"MarocMetal", 173500.0),
                new CompanyStats(3L,"BTP&Co",  152300.0)
        );
    }

    public List<CompanyStats> getAllSellers() {
        return Arrays.asList(
                new CompanyStats(1L,"AtlasSteel", 194000),
                new CompanyStats(2L,"MarocMetal", 173500),
                new CompanyStats(3L,"BTP&Co",    152300),
                new CompanyStats(4L,"OxyMetal",   120000),
                new CompanyStats(5L,"Cimentex",   96000)
        );
    }


    // ------------ TOP BUYERS ------------------

    public List<CompanyStats> getTop3Buyers() {
        return Arrays.asList(
                new CompanyStats(1L,"HighTech Group", 250000),
                new CompanyStats(2L,"GeniePro SARL", 194000),
                new CompanyStats(3L,"MoroccoBuild", 162500)
        );
    }

    public List<CompanyStats> getAllBuyers() {
        return Arrays.asList(
                new CompanyStats(1L,"HighTech Group", 250000),
                new CompanyStats(2L,"GeniePro SARL", 194000),
                new CompanyStats(3L,"MoroccoBuild", 162500),
                new CompanyStats(4L,"OasisConstruc", 150000),
                new CompanyStats(5L,"FirmaFox", 120200)
        );
    }


    // ------------ TOP PRODUCTS ------------------

    public List<ProductStats> getTop3Products() {
        return Arrays.asList(
                new ProductStats(1L,"Acier 12mm", 920),
                new ProductStats(2L,"Ciment 35kg", 870),
                new ProductStats(3L,"Câble cuivre", 810)
        );
    }

    public List<ProductStats> getAllProducts() {
        return Arrays.asList(
                new ProductStats(1L,"Acier 12mm", 920),
                new ProductStats(2L,"Ciment 35kg", 870),
                new ProductStats(3L,"Câble cuivre", 810),
                new ProductStats(4L,"Bois MDF", 600),
                new ProductStats(5L,"Tuyaux PVC", 550),
                new ProductStats(6L,"Béton prêt à l’emploi", 540),
                new ProductStats(7L,"Ferro 8mm", 530),
                new ProductStats(8L,"Plâtre projeté", 520),
                new ProductStats(9L,"Peinture façade", 500),
                new ProductStats(10L,"Peinture intérieure mate", 495),

                new ProductStats(11L,"Treillis soudé", 480),
                new ProductStats(12L,"Sable fin 0/2", 475),
                new ProductStats(13L,"Gravier 6/10", 470),
                new ProductStats(14L,"Bloc béton creux", 460),
                new ProductStats(15L,"Parpaing 15", 455),

                new ProductStats(16L,"Brique rouge", 450),
                new ProductStats(17L,"Plaque BA13", 445),
                new ProductStats(18L,"Lisse galvanisée 3m", 440),
                new ProductStats(19L,"Tube acier rond", 438),
                new ProductStats(20L,"Bois contreplaqué 18mm", 430),

                new ProductStats(21L,"Chevilles universelles", 425),
                new ProductStats(22L,"Vis à bois 50mm", 420),
                new ProductStats(23L,"Mortier colle carrelage", 418),
                new ProductStats(24L,"Carrelage 60x60", 415),
                new ProductStats(25L,"Robinet mélangeur", 410),

                new ProductStats(26L,"Tuyau multicouche", 405),
                new ProductStats(27L,"Flexible sanitaire", 402),
                new ProductStats(28L,"Interrupteur mural", 400),
                new ProductStats(29L,"Prise électrique 16A", 398),
                new ProductStats(30L,"Spot LED 18W", 395),

                new ProductStats(31L,"Profilé aluminium", 392),
                new ProductStats(32L,"Grille d’aération", 390),
                new ProductStats(33L,"Porte intérieure bois", 388),
                new ProductStats(34L,"Fenêtre PVC double vitrage", 385),
                new ProductStats(35L,"Colle PU", 380),

                new ProductStats(36L,"Joint silicone", 375),
                new ProductStats(37L,"Rouleau isolant laine de roche", 372),
                new ProductStats(38L,"Enduit de lissage", 370),
                new ProductStats(39L,"Échafaudage acier", 368),
                new ProductStats(40L,"Casque de chantier", 365),

                new ProductStats(41L,"Gants anti-coupure", 360),
                new ProductStats(42L,"Perceuse électrique", 358),
                new ProductStats(43L,"Disque diamant Ø230", 355),
                new ProductStats(44L,"Meuleuse 900W", 350),
                new ProductStats(45L,"Marteau perforateur", 348),

                new ProductStats(46L,"Ruban adhésif orange", 345),
                new ProductStats(47L,"Tuyau PER 16mm", 340),
                new ProductStats(48L,"Registre de ventilation", 338),
                new ProductStats(49L,"Chaise de coffrage", 335),
                new ProductStats(50L,"Coffrage bois 2m", 332),

                new ProductStats(51L,"Pompe à eau 1HP", 330),
                new ProductStats(52L,"Câble électrique 3x2.5mm", 328),
                new ProductStats(53L,"Peinture anticorrosion", 325),
                new ProductStats(54L,"Bétonnière 140L", 323),
                new ProductStats(55L,"Enduit façade monocouche", 320)

        );
    }


    // ------------ TOP CATEGORIES ------------------

    public List<CategoryStats> getTop3Categories() {
        return Arrays.asList(
                new CategoryStats(1,"Métaux", 4200),
                new CategoryStats(2,"Construction", 3800),
                new CategoryStats(3,"Électricité", 3100)
        );
    }

    public List<CategoryStats> getAllCategories() {
        return Arrays.asList(
                new CategoryStats(1,"Métaux", 4200),
                new CategoryStats(2,"Construction", 3800),
                new CategoryStats(3,"Électricité", 3100),
                new CategoryStats(4,"Bois", 2600),
                new CategoryStats(5,"Peinture", 1700)
        );
    }
}

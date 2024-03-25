package ej1;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class PromotionSender {
    public static void main(String[] args) {
        List<String> promotions = Arrays.asList(
            "Descuento en Café: ",
            "Descuento en Refrescos: ",
            "Descuento en Congelados: "
        );
//        notifyPromotions(promotions);
//        notifyPromotionsParallel(promotions);
        notifyPromotionsCompletable(promotions);
        System.out.println("Se realizaron todas las notificaciones de la promoción.");
    }

    public static void notifyPromotionsParallel(List<String> promotions){
        promotions.parallelStream().forEach(promotion -> {
            promotion = promotion + "30%";
            promotion = promotion + " Sólo por hoy";
            notifyCustomers(promotion);
        });
        notifyMarketing("Hoy se publicito un descuento del 30%");
    }

    public static void notifyPromotionsCompletable(List<String> promotions){
        List<CompletableFuture<Void>> notifyClientsFutures = new ArrayList<>();
        promotions.forEach(promotion -> {
            notifyClientsFutures.add(CompletableFuture.runAsync(() -> {
                notifyCustomers(promotion);
            }));
        });
        notifyMarketing("Hoy se publicito un descuento del 30%");
        notifyClientsFutures.forEach(CompletableFuture::join);
    }

    private static void notifyCustomers(String promotion) {
        try {
            TimeUnit.SECONDS.sleep(1);
            System.out.println("Cliente: " + promotion);
        } catch (InterruptedException e) {
            // Handle exception
        }
    }

    private static void notifyMarketing(String promotion) {
        try {
            TimeUnit.SECONDS.sleep(1);
            System.out.println("Marketing: " + promotion);
        } catch (InterruptedException e) {
            // Handle exception
        }
    }

    private static void notifyPromotions(List<String> promotions) {
        for (String promotion : promotions) {
            promotion = promotion + "30%";
            promotion = promotion + " Sólo por hoy";
            notifyCustomers(promotion);
        }
        notifyMarketing("Hoy se publicitó un descuento del 30%");
    }
}

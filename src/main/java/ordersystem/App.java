package ordersystem;

import ordersystem.discount.*;
import ordersystem.immutable.*;
import ordersystem.processing.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class App {
	public static void main(String[] args) {
        System.out.println("Запуск системы учета заказов...");

        Product laptop = new Product("P001", "Ноутбук", new BigDecimal("1200.00"));
        Product mouse = new Product("P002", "Мышь", new BigDecimal("200.00"));
        Product keyboard = new Product("P003", "Клавиатура", new BigDecimal("375.00"));

        System.out.println("\n--- Тестирование Cart (потокобезопасность) ---");
        Cart userCart = new Cart();
        ExecutorService executorService = Executors.newFixedThreadPool(5); 

        for (int i = 0; i < 10; i++) {
        	final int taskId = i;
            executorService.submit(() -> {
                userCart.addItem(laptop, 1);
                userCart.addItem(mouse, 1);
                userCart.addItem(keyboard, 1);
                if (taskId % 2 == 0) {
                    userCart.removeItem(mouse, 1);
                }
                System.out.println(Thread.currentThread().getName() + " - Текущая корзина: " + userCart.getItems().size() + " уникальных товаров.");
            });
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Потоки были прерваны.");
        }

        System.out.println("\n--- Итоговая корзина после многопоточной работы ---");
        userCart.getItems().forEach((product, quantity) ->
                System.out.println("Товар: " + product.getName() + ", Количество: " + quantity + ", Цена: " + product.getPrice().multiply(new BigDecimal(quantity)))
        );
        System.out.println("Общая сумма в корзине: " + userCart.getPrice());

        OrderService orderService = new OrderService();

        DiscountStrategy fixedDiscount = new FixedDiscount(new BigDecimal("0.10")); 

        System.out.println("\n--- Размещение заказа со скидкой ---");
        try {
            Order order1 = orderService.placeOrder("Boba", userCart, fixedDiscount);
            System.out.println("Размещен заказ 1: " + order1);
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка размещения заказа: " + e.getMessage());
        }

        userCart.addItem(keyboard, 1);
        userCart.addItem(laptop, 1);
        
        System.out.println("\n--- Новые товары в корзине ---");
        userCart.getItems().forEach((product, quantity) ->
        System.out.println("Товар: " + product.getName() + ", Количество: " + quantity + ", Цена: " + product.getPrice().multiply(new BigDecimal(quantity)))
);

        System.out.println("\n--- Размещение заказа без скидки ---");
        Order order2 = orderService.placeOrder("Biba", userCart, null);
        System.out.println("Размещен заказ 2: " + order2);


        System.out.println("\n--- Все размещенные заказы ---");
        List<Order> allOrders = orderService.getAllOrders();
        if (allOrders.isEmpty()) {
            System.out.println("Заказов пока нет.");
        } else {
            allOrders.forEach(System.out::println);
        }

        System.out.println("\n--- Поиск заказа по ID ---");
        if (!allOrders.isEmpty()) {
            Order foundOrder = orderService.getOrderById(allOrders.get(0).getOrderId());
            System.out.println("Найден заказ по ID: " + foundOrder);
        }
    }
}
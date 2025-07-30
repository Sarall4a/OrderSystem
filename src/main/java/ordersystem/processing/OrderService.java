package ordersystem.processing;

import ordersystem.immutable.*;
import ordersystem.discount.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderService {
	
	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
	
	private final List<Order> orders = new CopyOnWriteArrayList<>();
	
	public Order placeOrder(String customer_id, Cart cart, DiscountStrategy discount) {
		
		if (customer_id == null || customer_id.isEmpty() || cart == null || cart.getItems().isEmpty()) {
			logger.error("Ошибка размещения заказа: Неверные данные. ID клиента: '{}'", customer_id);
			throw new IllegalArgumentException("Пустой заказ или несуществующий покупатель");
		}
		
		BigDecimal finalPrice = cart.getPrice();
		
		if (discount != null) {
			logger.debug("Применение скидки для клиента '{}'.", customer_id); 
			finalPrice = discount.applyDiscount(cart);
		}
		
		Order newOrder = new Order(customer_id, cart.getItems(), finalPrice);
		orders.add(newOrder);
		cart.clear();
		
		logger.info("Заказ '{}' для клиента '{}' успешно размещен. Сумма: {}.", newOrder.getOrderId(), customer_id, finalPrice);
		return newOrder;
	}
	
	public List<Order> getAllOrders() {
		logger.debug("Запрошены все заказы. Всего: {}.", orders.size());
		return new ArrayList<>(orders);
	}
	
	public Order getOrderById(String order_id) {
		
		logger.debug("Поиск заказа по ID: '{}'.", order_id); 
		
		Order found = orders.stream().filter(order -> order.getOrderId().equals(order_id)).findFirst().orElse(null);
		if (found != null) {
			logger.info("Заказ '{}' найден.", order_id);
		} else {
			logger.warn("Заказ '{}' не найден.", order_id);
		} return found;
	}
}
package ordersystem.immutable;

import java.util.Map;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.Collections;
import java.util.HashMap;
import java.time.LocalDateTime;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Order {
	
	private static final Logger logger = LoggerFactory.getLogger(Order.class);
	
	private final String customer_id;
	private final Map<Product, Integer> goods;
	private final BigDecimal totalPrice;
	private final String order_id;
	private final LocalDateTime orderDate;
	
	public Order(String customer_id, Map<Product, Integer> goods, BigDecimal totalPrice) {
		
		if (customer_id == null || customer_id.isEmpty() || goods == null || goods.isEmpty() || totalPrice == null || totalPrice.compareTo(BigDecimal.ZERO) < 0) {
			logger.error("Попытка создать заказ с некорректными данными. customer_id: '{}', goods: {}, totalPrice: {}", customer_id, goods, totalPrice);
			throw new IllegalArgumentException("Неправильно, переделывай :q");
		}
		this.order_id = UUID.randomUUID().toString();
		this.customer_id = customer_id;
		this.goods = Collections.unmodifiableMap(new HashMap<>(goods));
		this.totalPrice = totalPrice;
		this.orderDate = LocalDateTime.now();
		
		logger.info("Заказ с ID '{}' для клиента '{}' успешно создан. Общая сумма: {}", this.order_id, this.customer_id, this.totalPrice);
	}
	
	public String customerId() {
		return customer_id;
	}
	
	public Map<Product, Integer> getGoods() {
		return goods;
	}
	
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
	
	public String getOrderId() {
		return order_id;
	}
	
	public LocalDateTime getOrderDate() {
		return orderDate;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Order order = (Order) o;
		return Objects.equals(order_id, order.order_id);
	}
	
	public int hashcode() {
		return Objects.hash(order_id);
	}
	
	public String orderInfo() {
		return "Заказ: "  + order_id +
				", покупатель - " + customer_id +
				", корзина - " + goods +
				", сумма - " + totalPrice +
				", дата заказа - " + orderDate;
	}
	
	
}
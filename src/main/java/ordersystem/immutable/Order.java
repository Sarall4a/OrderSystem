package ordersystem.immutable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Order {

  private static final Logger logger = LoggerFactory.getLogger(Order.class);
  private final String customerId;
  private final Map<Product, Integer> goods;
  private final BigDecimal totalPrice;
  private final String orderId;
  private final LocalDateTime orderDate;

  public Order(String customerId, Map<Product, Integer> goods, BigDecimal totalPrice) {
    if (customerId == null || customerId.isEmpty() || goods == null || goods.isEmpty() || totalPrice == null || totalPrice.compareTo(BigDecimal.ZERO) < 0) {
      logger.error(
          "Попытка создать заказ с некорректными данными. customerId: '{}', goods: {}, totalPrice: {}",
          customerId,
          goods,
          totalPrice);
      throw new IllegalArgumentException("Неправильно, переделывай :q");
    }
    this.orderId = UUID.randomUUID().toString();
    this.customerId = customerId;
    this.goods = Collections.unmodifiableMap(new HashMap<>(goods));
    this.totalPrice = totalPrice;
    this.orderDate = LocalDateTime.now();
    logger.info(
        "Заказ с ID '{}' для клиента '{}' успешно создан. Общая сумма: {}",
        this.orderId,
        this.customerId,
        this.totalPrice);
  }

  public String customerId() {
    return customerId;
  }

  public Map<Product, Integer> getGoods() {
    return goods;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  public String getOrderId() {
    return orderId;
  }

  public LocalDateTime getOrderDate() {
    return orderDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Order order = (Order) o;
    return Objects.equals(orderId, order.orderId);
  }

  @Override // Изменено: исправлено название метода на hashCode
  public int hashCode() {
    return Objects.hash(orderId);
  }

  public String orderInfo() {
    return "Заказ: "
        + orderId
        + ", покупатель - "
        + customerId
        + ", корзина - "
        + goods
        + ", сумма - "
        + totalPrice
        + ", дата заказа - "
        + orderDate;
  }
}
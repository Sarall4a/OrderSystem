package ordersystem.processing;

import ordersystem.discount.*;
import ordersystem.immutable.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderService {
  private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
  private final List<Order> orders = new CopyOnWriteArrayList<>();

  public Order placeOrder(String customerId, Cart cart, DiscountStrategy discount) {
    if (customerId == null || customerId.isEmpty() || cart == null || cart.getItems().isEmpty()) {
      logger.error("Ошибка размещения заказа: Неверные данные. ID клиента: '{}'", customerId);
      throw new IllegalArgumentException("Пустой заказ или несуществующий покупатель");
    }

    BigDecimal finalPrice = cart.getPrice();

    if (discount != null) {
      logger.debug("Применение скидки для клиента '{}'.", customerId);
      finalPrice = discount.applyDiscount(cart);
    }

    Order newOrder = new Order(customerId, cart.getItems(), finalPrice);
    orders.add(newOrder);
    cart.clear();
    logger.info(
        "Заказ '{}' для клиента '{}' успешно размещен. Сумма: {}.",
        newOrder.getOrderId(),
        customerId,
        finalPrice);
    return newOrder;
  }

  public List<Order> getAllOrders() {
    logger.debug("Запрошены все заказы. Всего: {}.", orders.size());
    return new ArrayList<>(orders);
  }

  public Order getOrderById(String orderId) {
    logger.debug("Поиск заказа по ID: '{}'.", orderId);
    Order found =
        orders.stream().filter(order -> order.getOrderId().equals(orderId)).findFirst().orElse(null);

    if (found != null) {
      logger.info("Заказ '{}' найден.", orderId);
    } else {
      logger.warn("Заказ '{}' не найден.", orderId);
    }
    return found;
  }
}
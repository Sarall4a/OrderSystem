package ordersystem.processing;

import ordersystem.immutable.Product;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cart {
  private static final Logger logger = LoggerFactory.getLogger(Cart.class);
  private final Map<Product, Integer> goods;

  public Cart() {
    this.goods = new ConcurrentHashMap<>();
    logger.info("Корзина создана.");
  }

  public synchronized void addItem(Product product, int amount) {
    if (product == null || amount <= 0) {
      logger.error(
          "Попытка добавить товар с некорректными параметрами: product={}, amount={}",
          product,
          amount);
      throw new IllegalArgumentException("Товар не найден или кол-во неверное");
    }

    goods.merge(product, amount, Integer::sum);
    logger.info("Добавлен товар: {}, кол-во: {}", product.getName(), amount);
    logger.debug("Текущее состояние корзины после добавления: {}", goods);
  }

  public synchronized void removeItem(Product product, int amount) {
    if (product == null || amount <= 0) {
      logger.error(
          "Попытка удалить товар с некорректными параметрами: product={}, amount={}",
          product,
          amount);
      throw new IllegalArgumentException("Товар не найден или кол-во неверное");
    }

    if (goods.containsKey(product)) {
      int currentAmount = goods.get(product);
      if (currentAmount <= amount) {
        goods.remove(product);
        logger.info("Товар удален: {}", product.getName());
        logger.debug("Текущее состояние корзины после удаления: {}", goods);
      } else {
        goods.put(product, currentAmount - amount);
        logger.info(
            "Кол-во товара успешно убавилось на {}. Новое кол-во для {}: {}",
            amount,
            product.getName(),
            currentAmount - amount);
        logger.debug("Текущее состояние корзины после частичного удаления: {}", goods);
      }

    } else {
      logger.warn("Попытка удалить товар '{}', которого нет в корзине.", product.getName());
    }
  }

  public synchronized Map<Product, Integer> getItems() {
    logger.debug("Запрос списка товаров в корзине.");
    return Collections.unmodifiableMap(new HashMap<>(goods));
  }

  public synchronized BigDecimal getPrice() {
    BigDecimal price = BigDecimal.ZERO;

    for (Map.Entry<Product, Integer> entry : goods.entrySet()) {
      price = price.add(entry.getKey().getPrice().multiply(new BigDecimal(entry.getValue())));
    }
    logger.info("Рассчитана общая стоимость корзины: {}", price);
    return price;
  }

  public void clear() {
    goods.clear();
    logger.info("Корзина очищена!");
  }
}
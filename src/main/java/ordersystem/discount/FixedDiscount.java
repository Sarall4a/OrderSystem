package ordersystem.discount;

import ordersystem.processing.Cart;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FixedDiscount implements DiscountStrategy {
	
	Logger logger = LoggerFactory.getLogger(FixedDiscount.class);
	
	private final BigDecimal percentage;
	
	public FixedDiscount(BigDecimal percentage) {
		
		if (percentage == null || percentage.compareTo(BigDecimal.ZERO) < 0 || percentage.compareTo(BigDecimal.ONE) > 0) {
			logger.error("Попытка создать стратегию скидки с некорректным процентом: {}. Процент должен быть между 0 и 1 (0% и 100%).", percentage);
			throw new IllegalArgumentException("Процент скидки должен быть между 0 и 1 (0% и 100%)");
		}
		this.percentage = percentage;
		logger.debug("Создана стратегия процентной скидки: {}%.", percentage.multiply(new BigDecimal("100")));
		}
	
	@Override
	public BigDecimal applyDiscount(Cart cart) {
		
		BigDecimal cartPrice = cart.getPrice();
		
		BigDecimal discountPercentage = cartPrice.multiply(percentage);
		
		BigDecimal finalPrice = cartPrice.subtract(discountPercentage);
		
		finalPrice = finalPrice.setScale(2, RoundingMode.HALF_UP);
		
		logger.info("Применена скидка {}% к исходной сумме {}. Итоговая сумма: {}.", percentage.multiply(new BigDecimal("100")), cartPrice, finalPrice);
		
		return finalPrice;
	}
}

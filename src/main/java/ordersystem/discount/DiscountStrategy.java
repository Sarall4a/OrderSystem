package ordersystem.discount;	

import ordersystem.processing.Cart;
import java.math.BigDecimal;

public interface DiscountStrategy {
	
	BigDecimal applyDiscount(Cart cart);
}
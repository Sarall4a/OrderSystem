package ordersystem.immutable;

import java.math.BigDecimal;
import java.util.Objects;

public final class Product {
	
	private final String article_id;
	private final String name;
	private final BigDecimal price;
	
	
	public Product(String article_id, String name, BigDecimal price) {
		
		if(article_id == null || article_id.isEmpty() || name == null || name.isEmpty() ||  price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Введите данные корректно");
		}
		
		this.article_id = article_id;
		this.name = name;
		this.price = price;
	}
	
	@Override
	public boolean equals(Object o) {
		
		if (this == o) 
			return true;
		
		if (o == null || getClass() != o.getClass()) 
			return false;
		
		Product product = (Product) o;
		
		return Objects.equals(this.article_id, product.article_id);
		
	}
	
	public String info() {
		return "Товар: " + this.name + ", " + this.price + ", " + this.article_id;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.article_id);
	}
	
	public String getArticle() {
		return this.article_id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public BigDecimal getPrice() {
		return this.price;
	}
	
}
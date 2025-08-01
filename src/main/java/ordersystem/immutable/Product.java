package ordersystem.immutable;

import java.math.BigDecimal;
import java.util.Objects;

public final class Product {

  private final String articleId;
  private final String name;
  private final BigDecimal price;

  public Product(String articleId, String name, BigDecimal price) {
    if (articleId == null
        || articleId.isEmpty()
        || name == null
        || name.isEmpty()
        || price == null
        || price.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Введите данные корректно");
    }
    this.articleId = articleId;
    this.name = name;
    this.price = price;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Product product = (Product) o;
    return Objects.equals(this.articleId, product.articleId);
  }

  public String info() {
    return "Товар: " + this.name + ", " + this.price + ", " + this.articleId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.articleId);
  }

  public String getArticle() {
    return this.articleId;
  }

  public String getName() {
    return this.name;
  }

  public BigDecimal getPrice() {
    return this.price;
  }
}
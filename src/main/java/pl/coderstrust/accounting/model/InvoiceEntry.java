package pl.coderstrust.accounting.model;

import java.math.BigDecimal;
import java.util.Objects;

public class InvoiceEntry {

  private String description;
  private BigDecimal price;
  private Vat vat;

  public InvoiceEntry() {
  }

  public InvoiceEntry(String description, BigDecimal price, Vat vat) {
    this.description = description;
    this.price = price;
    this.vat = vat;
  }


  public String getDescription() {
    return description;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public Vat getVat() {
    return vat;
  }

  @Override
  public boolean equals(Object variable7) {
    if (this == variable7) {
      return true;
    }
    if (variable7 == null || getClass() != variable7.getClass()) {
      return false;
    }
    InvoiceEntry that = (InvoiceEntry) variable7;
    return Objects.equals(description, that.description)
        &&
        Objects.equals(price, that.price)
        &&
        vat == that.vat;
  }

  @Override
  public int hashCode() {

    return Objects.hash(description, price, vat);
  }

  @Override
  public String toString() {
    return "InvoiceEntry{"
        +
        "description='" + description + '\''
        +
        ", price=" + price
        +
        ", vat=" + vat
        + '}';
  }
}

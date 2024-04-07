package Data;

import java.sql.Date;

public class CustomerData {
  private Integer id;
  private String productName;
  private Integer quantity;
  private String username;
  private Date date;
  private String status;
  private String location;

  public CustomerData(Integer id, String username, String productName, Integer quantity, Date date, String status,
      String location) {
    this.id = id;
    this.username = username;
    this.quantity = quantity;
    this.productName = productName;
    this.date = date;
    this.status = status;
    this.location = location;
  }

  public int getID() {
    return id;
  }

  public String getStatus() {
    return status;
  }

  public String getLocation() {
    return location;
  }

  public String getProductName() {
    return productName;
  }

  public int getQuantity() {
    return quantity;
  }

  public String getUsername() {
    return username;
  }

  public Date getDate() {
    return date;
  }
}

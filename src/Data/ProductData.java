package Data;

import java.sql.Date;

public class ProductData {
  private Integer id;
  private String productID;
  private String productName;
  private Integer productStock;
  private String image;
  private Date date;
  private Integer quantity;
  private String location;

  public ProductData(Integer id, String prodcutID, String productName, Integer productStock, String image, String location) {
    this.id = id;
    this.productID = prodcutID;
    this.productName = productName;
    this.productStock = productStock;
    this.image = image;
    this.location = location;
  }

  public ProductData(Integer id, String productID, String productName, Integer quantity, String image, Date date, String location) {
    this.id = id;
    this.productName = productName;
    this.image = image;
    this.date = date;
    this.quantity = quantity;
    this.location = location;
  }

  public ProductData(String productName, Integer quantity, Date date) {
    this.productName = productName;
    this.date = date;
    this.quantity = quantity;
  }


  public String getLocation(){
    return location;
  }
  public Integer getId() {
    return id;
  }

  public String getProductID() {
    return productID;
  }

  public String getProductName() {
    return productName;
  }

  public Integer getProductStock() {
    return productStock;
  }

  public String getImage() {
    return image;
  }

  public Date getDate() {
    return date;
  }

  public Integer getQuantity() {
    return quantity;
  }
}

package Data;

import java.sql.Date;

public class ReceiptData {
  Integer id;
  String productName;
  Integer quantity;
  Date date;
  String location;
  String status;

  public ReceiptData(Integer id, String productName, Integer quantity, Date date){
    this.id = id;
    this.productName = productName;
    this.quantity = quantity;
    this.date = date;
  }
  public ReceiptData(String productName, Integer quantity, Date date, String location, String status){
    this.productName = productName;
    this.quantity = quantity;
    this.date = date;
    this.location = location;
    this.status = status;
  }

public void setStatus(String status){
  this.status =status;
}

public String getStatus(){
  return status;
}

  public String getLocation(){
    return location;
  }

  public String getProductName(){
    return productName;
  }
  public int getID(){
    return id;
  }
  public int getQuantity(){
    return quantity;
  }

  public Date getDate(){
    return date;
  }
}

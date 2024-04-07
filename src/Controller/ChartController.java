package Controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

public class ChartController implements Initializable {

  @FXML
  private AreaChart<?, ?> chartCustomer;

  @FXML
  private Label totalCustomer;

  @FXML
  private Label totalProduct;

  Connection connect;
  PreparedStatement statement;
  ResultSet result;

  private void showTotalCustomer() throws SQLException {
    String sql = "SELECT COUNT(DISTINCT username) FROM custorder";

    connect = DatabaseConnection.getConnection();

    try {
      int username = 0;
      statement = connect.prepareStatement(sql);

      result = statement.executeQuery();

      if (result.next()) {
        username = result.getInt(1);
      }
      totalCustomer.setText(String.valueOf(username));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void showTotalProduct() throws SQLException {
    String sql = "SELECT SUM(quantity) FROM custorder";

    connect = DatabaseConnection.getConnection();

    try {
      int quantity = 0;
      statement = connect.prepareStatement(sql);

      result = statement.executeQuery();

      if (result.next()) {
        quantity = result.getInt("SUM(quantity)");
      }
      totalProduct.setText(String.valueOf(quantity));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void showChart() throws SQLException {
    
    String sql = "SELECT date, SUM(quantity) FROM custorder GROUP BY date ORDER BY TIMESTAMP(date)";

    connect = DatabaseConnection.getConnection();
    XYChart.Series chart = new XYChart.Series<>();

    try {
      statement = connect.prepareStatement(sql);
      result = statement.executeQuery();

      while(result.next()){
        chart.getData().add(new XYChart.Data<>(result.getString(1), result.getInt(2)));
      }
      chartCustomer.getData().add(chart);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    try {
      showTotalCustomer();
      showTotalProduct();
      showChart();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}

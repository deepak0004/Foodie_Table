package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

public class Showfood  implements Initializable{
	
	@FXML TableColumn<Foodtable, String> t1_food;
	@FXML TableColumn<Foodtable, Integer> t1_price;
	@FXML TableColumn<Foodtable, Boolean> t1_order;
	@FXML TableView<Foodtable> f1table;
	@FXML Button f1_order_btn;
	@FXML Label success_search_order_made;
	ArrayList<Integer> food_id=new ArrayList<Integer>();
	ResultSet res;
	Statement myStat, st1;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {				
		try{
			Connection myconn = DriverManager.getConnection("jdbc:mysql://localhost/hotel", "root", "");
			myStat = myconn.createStatement();
			st1 = myconn.createStatement();
		}catch(Exception e){
			System.out.println("ERROR!!");
		}
		
		t1_food.setCellValueFactory(new PropertyValueFactory<Foodtable, String>("food"));
		t1_price.setCellValueFactory(new PropertyValueFactory<Foodtable, Integer>("price"));
		t1_order.setCellValueFactory(new PropertyValueFactory<Foodtable, Boolean>("check"));
		t1_order.setCellFactory(CheckBoxTableCell.forTableColumn(t1_order));
		t1_order.setEditable(true);
		f1table.setEditable(true);
		
		ObservableList<Foodtable> data = FXCollections.observableArrayList();
		System.out.println(CustomerController.customer_search_send_q);

		try {
			res = st1.executeQuery("select * from food where fd_food like '%"+CustomerController.customer_search_send_q+"%' and fd_id not in (select fd_id from `order`)");
			while(res.next()){
				data.add(new Foodtable(res.getString("fd_food"), res.getInt("fd_price")));
				food_id.add(res.getInt("fd_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		f1table.setItems(data);	 
		
		f1_order_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	int i=0;
                for (Foodtable p : f1table.getItems()) {
                    if(p.isCheck()){
                    	try {
							st1.executeUpdate("Insert into `order`(cs_id, fd_id, order_stage) Values ("+ControllerFirst.userid+", "+food_id.get(i)+", 0)");
							success_search_order_made.setTextFill(Color.web("green"));
							success_search_order_made.setText("Food Added Successfully");
                    	} catch (SQLException e) {
							e.printStackTrace();
						}
                    }else{

                    }
                    i++;
                }
            }
        });
				
	}

}

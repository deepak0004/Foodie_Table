package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class WaiterController implements Initializable{

	@FXML TextField chef_name, chef_pass, nfood, nprice;
	@FXML Button chef_save_btn, orders_done, log_me_out, add_food, remove_food;
	@FXML Label chef_data_saved, orders_delivered, food_added;
	@FXML TabPane chef_tabpane;
	@FXML Tab chef_chef_tab, chef_orders_tab, foodtab;
		
	@FXML TableColumn<ChefOrderTable, String> chef_customer_col, chef_order_col;
	@FXML TableColumn<ChefOrderTable, Integer> chef_price_col;
	@FXML TableColumn<ChefOrderTable, Boolean> chef_mark_col;
	@FXML TableView<ChefOrderTable> chef_order_table;
	
	ArrayList<Integer> food_id=new ArrayList<Integer>();
	ArrayList<Integer> c_id=new ArrayList<Integer>();
	
	int waiter_id = ControllerFirst.userid;
	
	ResultSet res;
	Statement myStat;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try{
			Connection myconn = DriverManager.getConnection("jdbc:mysql://localhost/hotel", "root", "");
			myStat = myconn.createStatement();
		}catch(Exception e){
			System.out.println("ERROR!!");
		}
		try {
			res = myStat.executeQuery("select * from `waiter` where `wt_id`="+waiter_id+" Limit 1");
			while(res.next()){
				chef_name.setText(res.getString("wt_name"));
				chef_pass.setText(res.getString("wt_pass"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		chef_save_btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent Event) {
				String newname = chef_name.getText();
				String newpass = chef_pass.getText();
				if(!newname.equals("") && !newpass.equals("")){
					try{
						myStat.executeUpdate("update `waiter` SET `wt_name`='"+newname+"', wt_pass='"+newpass+"' where `wt_id`="+waiter_id);
						chef_data_saved.setTextFill(Color.web("green")); 
						chef_data_saved.setText("Changes Saved Successfully");
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		});
		
		log_me_out.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	Stage cstage = (Stage) log_me_out.getScene().getWindow();
				cstage.close();
				
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LR.fxml"));
	            Parent root1;
				try {
					root1 = (Parent) fxmlLoader.load();
					Stage stage = new Stage();
					Scene scene = new Scene(root1, 673, 510);    
		            stage.setTitle("FXML Welcome");
		            stage.setScene(scene);
		            stage.show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            
            }
        });
		
		chef_order_col.setCellValueFactory(new PropertyValueFactory<ChefOrderTable, String>("food"));
		chef_customer_col.setCellValueFactory(new PropertyValueFactory<ChefOrderTable, String>("customer"));
		chef_price_col.setCellValueFactory(new PropertyValueFactory<ChefOrderTable, Integer>("price"));
		chef_mark_col.setCellValueFactory(new PropertyValueFactory<ChefOrderTable, Boolean>("check"));
		chef_mark_col.setCellFactory(CheckBoxTableCell.forTableColumn(chef_mark_col));
		chef_mark_col.setEditable(true);
		chef_order_table.setEditable(true);
		
		chef_tabpane.getSelectionModel().selectedItemProperty().addListener(
			    new ChangeListener<Tab>() {
			        @Override
			        public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab newTab) {
			        	if(newTab.equals(chef_chef_tab)){
			        		try {
			        			res = myStat.executeQuery("select * from `waiter` where `wt_id`="+waiter_id+" Limit 1");
			        			while(res.next()){
			        				chef_name.setText(res.getString("wt_name"));
			        				chef_pass.setText(res.getString("wt_pass"));
			        			}
			        		} catch (SQLException e) {
			        			// TODO Auto-generated catch block
			        			e.printStackTrace();
			        		}
			        		chef_data_saved.setText("");
			        	}else if(newTab.equals(chef_orders_tab)){
			        		ObservableList<ChefOrderTable> data = FXCollections.observableArrayList();
			        		try {
			        			food_id.clear();
			        			c_id.clear();
			        			res = myStat.executeQuery("SELECT * from (`order` natural join customer) natural join food where order_stage=2 order by cs_id");
			        			while(res.next()){				
			        				data.add(new ChefOrderTable(res.getString("fd_food"), res.getInt("fd_price"), res.getString("cs_name")));
			        				food_id.add(res.getInt("fd_id"));
			        				c_id.add(res.getInt("cs_id"));
			        			}
			        		} catch (Exception e) {
			        			e.printStackTrace();
			        		}
			        		chef_order_table.setItems(data);
			        		orders_delivered.setText("");
			        	}
			        }
			    }
		);

		orders_done.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent Event) {					
				int i=0;
                for (ChefOrderTable p : chef_order_table.getItems()) {
                    if(p.isCheck()){
                    	try {
                    		myStat.executeUpdate("DELETE FROM `hotel`.`order` WHERE `order`.`cs_id` = "+c_id.get(i)+" AND `order`.`fd_id` = "+food_id.get(i));
                    		
                    		orders_delivered.setTextFill(Color.web("green"));
    						orders_delivered.setText("Order Delivered!");
						} catch (SQLException e) {
							e.printStackTrace();
						}
                    }
                    i++;
                }
                ObservableList<ChefOrderTable> data = FXCollections.observableArrayList();
        		
        		try {
        			food_id.clear();
        			c_id.clear();
        			res = myStat.executeQuery("SELECT * from (`order` natural join customer) natural join food where order_stage=2 order by cs_id");
        			while(res.next()){				
        				data.add(new ChefOrderTable(res.getString("fd_food"), res.getInt("fd_price"), res.getString("cs_name")));
        				food_id.add(res.getInt("fd_id"));
        				c_id.add(res.getInt("cs_id"));
        			}
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
        		chef_order_table.setItems(data);
			}
		});
	}
	
}

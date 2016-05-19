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

public class CustomerController implements Initializable {
	
	@FXML TableColumn<Foodtable, String> t_food;
	@FXML TableColumn<Foodtable, Integer> t_price;
	@FXML TableColumn<Foodtable, Boolean> t_order;
	@FXML TableView<Foodtable> ftable;
	
	@FXML TableColumn<CustomerBill, String> status_status, status_food;
	
	@FXML TableColumn<CustomerBill, Integer> status_price;
	@FXML TableColumn<CustomerBill, Boolean> status_remove;
	@FXML TableView<CustomerBill> table_status;
	
	@FXML Button cs_order_btn, cs_changes_saved_btn, customer_food_search, log_me_out, remove_orders, filter_btn;
	@FXML Label ordered_successfully, cs_changes_saved, total_rs;
	@FXML TextField cs_name, cs_pass, customer_search_q, filter_val;
	
	@FXML TabPane customer_tabpane;
	@FXML Tab customer_tab_profile, customer_tab_order, customer_tab_status;
	
	ArrayList<Integer> food_id=new ArrayList<Integer>();
	ArrayList<Integer> what_order_id=new ArrayList<Integer>();
	ArrayList<Integer> who_order_id=new ArrayList<Integer>();
	ResultSet res;
	Statement myStat;
	static String customer_search_send_q;
	
	int customer_id = ControllerFirst.userid;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try{
			Connection myconn = DriverManager.getConnection("jdbc:mysql://localhost/hotel", "root", "");
			myStat = myconn.createStatement();
		}catch(Exception e){
			System.out.println("ERROR!!");
		}
		
		try {
			res = myStat.executeQuery("select * from `customer` where `cs_id`="+customer_id+" Limit 1");
			while(res.next()){
				cs_name.setText(res.getString("cs_name"));
				cs_pass.setText(res.getString("cs_pass"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		cs_changes_saved_btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent Event) {
				String newname = cs_name.getText();
				String newpass = cs_pass.getText();
				if(!newname.equals("") && !newpass.equals("")){
					try{
						myStat.executeUpdate("update `customer` SET `cs_name`='"+newname+"', cs_pass='"+newpass+"' where `cs_id`="+customer_id);
						cs_changes_saved.setTextFill(Color.web("green"));
						cs_changes_saved.setText("Changes Saved Successfully");
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		});
		
		t_food.setCellValueFactory(new PropertyValueFactory<Foodtable, String>("food"));
		t_price.setCellValueFactory(new PropertyValueFactory<Foodtable, Integer>("price"));
		t_order.setCellValueFactory(new PropertyValueFactory<Foodtable, Boolean>("check"));
		t_order.setCellFactory(CheckBoxTableCell.forTableColumn(t_order));
		t_order.setEditable(true);
		ftable.setEditable(true);
		
		cs_order_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	int i=0;
                for (Foodtable p : ftable.getItems()) {
                    if(p.isCheck()){
                    	try {
							myStat.executeUpdate("Insert into `order`(cs_id, fd_id, order_stage) Values ("+customer_id+", "+food_id.get(i)+", 0)");
							ordered_successfully.setTextFill(Color.web("green"));
							ordered_successfully.setText("Order Placed Successfully");
                    	} catch (SQLException e) {
							e.printStackTrace();
						}
                    }
                    i++;
                }
                ObservableList<Foodtable> data = FXCollections.observableArrayList();
        		
        		try {
        			food_id.clear();
        			res = myStat.executeQuery("select * from food where fd_id not in (select fd_id from `order`)");
        			while(res.next()){
        				data.add(new Foodtable(res.getString("fd_food"), res.getInt("fd_price")));
        				food_id.add(res.getInt("fd_id"));
        			}
        		} catch (SQLException e) {
        			e.printStackTrace();
        		}
        		
        		ftable.setItems(data);
        		
        		ordered_successfully.setText("");
               
            }
        });
		
		filter_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	ObservableList<Foodtable> data = FXCollections.observableArrayList();
        		String filter_val_text = filter_val.getText();
        		if(filter_val_text.equals("")){
        			filter_val_text="99999999";
        		}
        		if(filter_val_text.matches("[-+]?\\d*\\.?\\d+")){
        			int zx = Integer.parseInt(filter_val_text);
            		try {
            			food_id.clear();
            			res = myStat.executeQuery("select * from food where fd_price < " + zx + " and fd_id not in (select fd_id from `order`)");
            			while(res.next()){
            				data.add(new Foodtable(res.getString("fd_food"), res.getInt("fd_price")));
            				food_id.add(res.getInt("fd_id"));
            			}
            		} catch (SQLException e) {
            			e.printStackTrace();
            		}
            		
            		ftable.setItems(data);
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
		
		status_food.setCellValueFactory(new PropertyValueFactory<CustomerBill, String>("food"));
		status_price.setCellValueFactory(new PropertyValueFactory<CustomerBill, Integer>("price"));
		status_status.setCellValueFactory(new PropertyValueFactory<CustomerBill, String>("status"));
		status_remove.setCellValueFactory(new PropertyValueFactory<CustomerBill, Boolean>("remove"));
		status_remove.setCellFactory(CheckBoxTableCell.forTableColumn(status_remove));
		status_remove.setEditable(true);
		table_status.setEditable(true);
		
		customer_food_search.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
	            try{
	            	customer_search_send_q = customer_search_q.getText();
	            	
	            	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("foodtable.fxml"));
		            Parent root1 = (Parent) fxmlLoader.load();
		            Stage stage = new Stage();
		            Scene scene = new Scene(root1, 320, 530);    
		            stage.setTitle("FXML Welcome");
		            stage.setScene(scene);
		            stage.show();
		            
	            }catch(Exception e){
	            	e.printStackTrace();
	            }
            }
        });
		
		
		remove_orders.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	int i=0;
                for (CustomerBill p : table_status.getItems()) {
                    if(p.isCheck()){
                    	try {
                    		
							myStat.executeUpdate("DELETE FROM `hotel`.`order` WHERE `order`.`cs_id` = "+customer_id+" AND `order`.`fd_id` = "+what_order_id.get(i));
							
							ObservableList<CustomerBill> data2 = FXCollections.observableArrayList();
			        		
			        		try {
			        			res = myStat.executeQuery("select * from food natural join `order` where cs_id="+customer_id);
			        			what_order_id.clear();
			        			while(res.next()){
			        				if(res.getInt("order_stage")==0 || res.getInt("order_stage")==2){
			        					data2.add(new CustomerBill(res.getString("fd_food"), res.getInt("fd_price"), "Order Pending"));
			        				}else if(res.getInt("order_stage")==1){
			        					data2.add(new CustomerBill(res.getString("fd_food"), res.getInt("fd_price"), "Order Delivered"));
			        				}
			        				what_order_id.add(res.getInt("fd_id"));
			        			}
			        			table_status.setItems(data2);
			        			
			        			res = myStat.executeQuery("select sum(fd_price) as p from food, `order` where food.fd_id=`order`.fd_id and `order_stage`=0 or order_stage=2 and cs_id="+customer_id);
			        			while(res.next()){
			        				total_rs.setText("Rs. "+res.getInt("p"));
			        			}
			        			
			        		} catch (SQLException e) {
			        			e.printStackTrace();
			        		}
							
                    	} catch (SQLException e) {
							e.printStackTrace();
						}
                    }
                    i++;
                }
               
            }
        });
	
		customer_tabpane.getSelectionModel().selectedItemProperty().addListener(
			    new ChangeListener<Tab>() {
			        @Override
			        public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab newTab) {
			        	if(newTab.equals(customer_tab_profile)){
			        		cs_changes_saved.setText(""); ordered_successfully.setText("");
			        		try {
			        			res = myStat.executeQuery("select * from `customer` where `cs_id`="+customer_id+" Limit 1");
			        			while(res.next()){
			        				cs_name.setText(res.getString("cs_name"));
			        				cs_pass.setText(res.getString("cs_pass"));
			        			}
			        		} catch (SQLException e) {
			        			// TODO Auto-generated catch block
			        			e.printStackTrace();
			        		}
			        	}else if(newTab.equals(customer_tab_order)){
			        		filter_val.setText("");cs_changes_saved.setText(""); ordered_successfully.setText("");
			        		ObservableList<Foodtable> data = FXCollections.observableArrayList();
			        		
			        		try {
			        			food_id.clear();
			        			res = myStat.executeQuery("select * from food where fd_id not in (select fd_id from `order`)");
			        			while(res.next()){
			        				data.add(new Foodtable(res.getString("fd_food"), res.getInt("fd_price")));
			        				food_id.add(res.getInt("fd_id"));
			        			}
			        		} catch (SQLException e) {
			        			e.printStackTrace();
			        		}
			        		
			        		ftable.setItems(data);
			        		
			        		ordered_successfully.setText("");
			        		
			        	}else if(newTab.equals(customer_tab_status)){
			        		cs_changes_saved.setText(""); ordered_successfully.setText("");
			        		ObservableList<CustomerBill> data2 = FXCollections.observableArrayList();
			        		what_order_id.clear();
			        		try {
			        			res = myStat.executeQuery("select food.fd_id, cs_id, fd_food, order_stage, fd_price from food, `order` where food.fd_id=`order`.fd_id and cs_id="+customer_id);
			        			while(res.next()){
			        				if(res.getInt("order_stage")==0 || res.getInt("order_stage")==2){
			        					data2.add(new CustomerBill(res.getString("fd_food"), res.getInt("fd_price"), "Order Pending"));
			        				}else if(res.getInt("order_stage")==1){
			        					data2.add(new CustomerBill(res.getString("fd_food"), res.getInt("fd_price"), "Order Delivered"));
			        				}
			        				what_order_id.add(res.getInt("fd_id"));
			        			}
			        			table_status.setItems(data2);
			        			
			        			res = myStat.executeQuery("select sum(fd_price) as p from food, `order` where food.fd_id=`order`.fd_id and cs_id="+customer_id);
			        			while(res.next()){
			        				total_rs.setText("Rs. "+res.getInt("p"));
			        			}
			        			
			        		} catch (SQLException e) {
			        			e.printStackTrace();
			        		}
			        	}
			        }
			    }
		);
	}

}

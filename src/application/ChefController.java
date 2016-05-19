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

public class ChefController implements Initializable{
	
	@FXML TextField chef_name, chef_pass, nfood, nprice;
	@FXML Button chef_save_btn, orders_done, log_me_out, add_food, remove_food;
	@FXML Label chef_data_saved, orders_delivered, food_added;
	@FXML TabPane chef_tabpane;
	@FXML Tab chef_chef_tab, chef_orders_tab, foodtab;
	
	@FXML TableColumn<Foodtable, String> admin_food;
	@FXML TableColumn<Foodtable, Integer> admin_price;
	@FXML TableColumn<Foodtable, Boolean> admin_order;
	@FXML TableView<Foodtable> atable;
	
	
	@FXML TableColumn<ChefOrderTable, String> chef_customer_col, chef_order_col;
	@FXML TableColumn<ChefOrderTable, Integer> chef_price_col;
	@FXML TableColumn<ChefOrderTable, Boolean> chef_mark_col;
	@FXML TableView<ChefOrderTable> chef_order_table;
	
	ArrayList<Integer> food_id=new ArrayList<Integer>();
	ArrayList<Integer> c_id=new ArrayList<Integer>();
	
	int chef_id = ControllerFirst.userid;
	
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
			res = myStat.executeQuery("select * from `chef` where `cf_id`="+chef_id+" Limit 1");
			while(res.next()){
				chef_name.setText(res.getString("cf_name"));
				chef_pass.setText(res.getString("cf_pass"));
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
						myStat.executeUpdate("update `chef` SET `cf_name`='"+newname+"',cf_pass='"+newpass+"' where `cf_id`="+chef_id);
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
			        		chef_data_saved.setText(""); orders_delivered.setText(""); food_added.setText("");
			        		try {
			        			res = myStat.executeQuery("select * from `chef` where `cf_id`="+chef_id+" Limit 1");
			        			while(res.next()){
			        				chef_name.setText(res.getString("cf_name"));
			        				chef_pass.setText(res.getString("cf_pass"));
			        			}
			        		} catch (SQLException e) {
			        			// TODO Auto-generated catch block
			        			e.printStackTrace();
			        		}
			        		chef_data_saved.setText("");
			        	}else if(newTab.equals(chef_orders_tab)){
			        		chef_data_saved.setText(""); orders_delivered.setText(""); food_added.setText("");
			        		ObservableList<ChefOrderTable> data = FXCollections.observableArrayList();
			        		try {
			        			food_id.clear();
			        			c_id.clear();
				    			res = myStat.executeQuery("SELECT * from (`order` natural join customer) natural join food where order_stage=0 order by cs_id");
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
			        	}else if(newTab.equals(foodtab)){
			        		chef_data_saved.setText(""); orders_delivered.setText(""); food_added.setText("");
			        		admin_food.setCellValueFactory(new PropertyValueFactory<Foodtable, String>("food"));
				    		admin_price.setCellValueFactory(new PropertyValueFactory<Foodtable, Integer>("price"));
				    		admin_order.setCellValueFactory(new PropertyValueFactory<Foodtable, Boolean>("check"));
				    		admin_order.setCellFactory(CheckBoxTableCell.forTableColumn(admin_order));
				    		admin_order.setEditable(true);
				    		atable.setEditable(true);
				    		
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
				    		
				    		atable.setItems(data);	 
			        	}
			        }
			    }
		);
		
		remove_food.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	int i=0;
                for (Foodtable p : atable.getItems()) {
                    if(p.isCheck()){
                    	try {
							myStat.executeUpdate("DELETE FROM `hotel`.`food` WHERE `food`.`fd_id` = "+food_id.get(i));
						} catch (SQLException e) {
							e.printStackTrace();
						}
                    }else{
                    	
                    }
                    i++;
                }
                /* new */
                try {
	    			res = myStat.executeQuery("select * from food where fd_id not in (select fd_id from `order`)");
	    			ObservableList<Foodtable> data2 = FXCollections.observableArrayList();
	    			food_id.clear();
	    			while(res.next()){
	    				data2.add(new Foodtable(res.getString("fd_food"), res.getInt("fd_price")));
	    				food_id.add(res.getInt("fd_id"));
	    			}
	                atable.setItems(data2);	
	    		} catch (SQLException e) {
	    			e.printStackTrace();
	    		}
                food_added.setText("");
            }
        });

		add_food.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	try {
            		String newfood = nfood.getText();
            		String newprice = nprice.getText();
            		if(newfood.equals("") && newprice.equals("")){
            			food_added.setTextFill(Color.web("red"));
						food_added.setText("Please fill in the fields correctly!");
            		}else{
            			if(newprice.matches("[-+]?\\d*\\.?\\d+")){
            				int newpricei = Integer.parseInt(newprice);	            		
    						myStat.executeUpdate("INSERT INTO `food`(`fd_food`, `fd_price`, `cf_id`) VALUES ('"+newfood+"', "+newpricei+", "+chef_id+")");
    			    			res = myStat.executeQuery("select * from food where fd_id not in (select fd_id from `order`)");
    			    			ObservableList<Foodtable> data2 = FXCollections.observableArrayList();
    			    			food_id.clear();
    			    			while(res.next()){
    			    				data2.add(new Foodtable(res.getString("fd_food"), res.getInt("fd_price")));
    			    				food_id.add(res.getInt("fd_id"));
    			    			}
    			                atable.setItems(data2);	
    		            		nfood.clear();
    		                	nprice.clear();
    		                	food_added.setTextFill(Color.web("green"));
    							food_added.setText("Food Added Successfully");
            			}else{
            				food_added.setTextFill(Color.web("red"));
    						food_added.setText("Please fill in the fields correctly!");
            			}
            		}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

		orders_done.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent Event) {					
				int i=0;
                for (ChefOrderTable p : chef_order_table.getItems()) {
                    if(p.isCheck()){
                    	try {
                    		myStat.executeUpdate("update `order` set order_stage=2 where cs_id="+c_id.get(i)+" and fd_id="+food_id.get(i));
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
        			res = myStat.executeQuery("SELECT * from (`order` natural join customer) natural join food where order_stage=0 order by cs_id");
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

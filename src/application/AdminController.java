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

public class AdminController implements Initializable{

	@FXML TextField admin_name, admin_pass, nfood, nprice, search_query;
	@FXML Button save_changes_btn, add_food, remove_food, search_btn, approve_chefs, log_me_out, approve_waiter, remove_chef, remove_waiter;
	@FXML Label update_p, chefs_approved, waiter_approved, chef_removed, waiter_removed;
	@FXML Tab foodtab, profiletab, chef_tab, waiter_tab, orders_show, view_chef_tab, view_waiter_tab;
	@FXML TabPane tabpane;
	
	@FXML TableColumn<FoodDetails, String> admin_food;
	@FXML TableColumn<FoodDetails, String> admin_chef;
	@FXML TableColumn<FoodDetails, Integer> admin_price;
	@FXML TableColumn<FoodDetails, Boolean> admin_order;
	@FXML TableView<FoodDetails> atable;
	@FXML TableView<Cheftable> chef_table, waiter_table, view_chef_table, view_waiter_table;
	@FXML TableColumn<Cheftable, Boolean> chef_approve, waiter_approve, view_chef_remove, view_waiter_remove;
	@FXML TableColumn<Cheftable, String> chef_name, waiter_name, view_chef_name, view_waiter_name;
	
	@FXML TableColumn<ViewOrdersAdminTable, String> display_name;
	@FXML TableColumn<ViewOrdersAdminTable, String> display_food;
	@FXML TableColumn<ViewOrdersAdminTable, Integer> display_price;
	@FXML TableView<ViewOrdersAdminTable> orders_show_table;
	
	
	static String send_search_keyword;
	ArrayList<Integer> food_id=new ArrayList<Integer>();
	ArrayList<Integer> chef_ids=new ArrayList<Integer>();
	ArrayList<Integer> waiter_ids=new ArrayList<Integer>();
	
	int admin_id = ControllerFirst.userid;
	
	ResultSet res, res1;
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
		try {
			res = myStat.executeQuery("select * from `admin` where `ad_id`="+admin_id+" Limit 1");
			while(res.next()){
				admin_name.setText(res.getString("ad_name"));
				admin_pass.setText(res.getString("ad_pass"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		save_changes_btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent Event) {
				String newname = admin_name.getText();
				String newpass = admin_pass.getText();
				if(!newname.equals("") && !newpass.equals("")){
					try{
						myStat.executeUpdate("update `admin` SET `ad_name`='"+newname+"', ad_pass='"+newpass+"' where `ad_id`="+admin_id);
						update_p.setTextFill(Color.web("green"));
						update_p.setText("Changes Saved Successfully");
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		});
				
		tabpane.getSelectionModel().selectedItemProperty().addListener(
			    new ChangeListener<Tab>() {
			        @Override
			        public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab newTab) {
			        	if(newTab.equals(profiletab)){
			        		try {
			        			res = myStat.executeQuery("select * from `admin` where `ad_id`="+admin_id+" Limit 1");
			        			while(res.next()){
			        				admin_name.setText(res.getString("ad_name"));
			        				admin_pass.setText(res.getString("ad_pass"));
			        			}
			        		} catch (SQLException e) {
			        			e.printStackTrace();
			        		}
			        		update_p.setText("");
			        	}else if(newTab.equals(foodtab)){
				        	admin_food.setCellValueFactory(new PropertyValueFactory<FoodDetails, String>("food"));
				    		admin_price.setCellValueFactory(new PropertyValueFactory<FoodDetails, Integer>("price"));
				    		admin_order.setCellValueFactory(new PropertyValueFactory<FoodDetails, Boolean>("check"));
							admin_chef.setCellValueFactory(new PropertyValueFactory<FoodDetails, String>("chef"));

				    		admin_order.setCellFactory(CheckBoxTableCell.forTableColumn(admin_order));
				    		admin_order.setEditable(true);
				    		atable.setEditable(true);
				    		
				    		ObservableList<FoodDetails> data = FXCollections.observableArrayList();
				    		
				    		try {
				    			food_id.clear();
				    			res = myStat.executeQuery("select * from food natural join chef where fd_id not in (select fd_id from `order`)");
				    			while(res.next()){
				    				data.add(new FoodDetails(res.getString("fd_food"), res.getInt("fd_price"), res.getString("cf_name")));
				    				food_id.add(res.getInt("fd_id"));
				    			}
				    		} catch (SQLException e) {
				    			e.printStackTrace();
				    		}
				    		
				    		atable.setItems(data);	 
			        	}else if(newTab.equals(orders_show)){

				        	display_name.setCellValueFactory(new PropertyValueFactory<ViewOrdersAdminTable, String>("name"));
			        		display_food.setCellValueFactory(new PropertyValueFactory<ViewOrdersAdminTable, String>("food"));
				        	display_price.setCellValueFactory(new PropertyValueFactory<ViewOrdersAdminTable, Integer>("price"));
				    		
				    		ObservableList<ViewOrdersAdminTable> data = FXCollections.observableArrayList();
				    		
				    		try {
				    			res = myStat.executeQuery("SELECT * from (`order` natural join customer) natural join food order by cs_id");
			        			while(res.next()){				
			        				data.add(new ViewOrdersAdminTable(res.getString("cs_name"), res.getString("fd_food"), res.getInt("fd_price")));
			        			}
				    		} catch (SQLException e) {
				    			e.printStackTrace();
				    		}
				    		
				    		orders_show_table.setItems(data);	 
			        	
			        	}else if(newTab.equals(chef_tab)){
			        		chef_name.setCellValueFactory(new PropertyValueFactory<Cheftable, String>("chef"));
				    		chef_approve.setCellValueFactory(new PropertyValueFactory<Cheftable, Boolean>("approve"));
				    		chef_approve.setCellFactory(CheckBoxTableCell.forTableColumn(chef_approve));
				    		chef_approve.setEditable(true);
				    		chef_table.setEditable(true);
				    		
				    		ObservableList<Cheftable> data = FXCollections.observableArrayList();
				    		
				    		try {
				    			chef_ids.clear();
				    			res = myStat.executeQuery("select * from `wait_chef`");
				    			while(res.next()){
				    				data.add(new Cheftable(res.getString("cf_name")));
				    				chef_ids.add(res.getInt("cf_id"));
				    			}
				    		} catch (SQLException e) {
				    			e.printStackTrace();
				    		}
				    		
				    		chef_table.setItems(data);
				    		chefs_approved.setText("");
			        	}else if(newTab.equals(waiter_tab)){
			        		
			        		waiter_name.setCellValueFactory(new PropertyValueFactory<Cheftable, String>("chef"));
				    		waiter_approve.setCellValueFactory(new PropertyValueFactory<Cheftable, Boolean>("approve"));
				    		waiter_approve.setCellFactory(CheckBoxTableCell.forTableColumn(waiter_approve));
				    		waiter_approve.setEditable(true);
				    		waiter_table.setEditable(true);
				    		
				    		ObservableList<Cheftable> data = FXCollections.observableArrayList();
				    		
				    		try {
				    			waiter_ids.clear();
				    			res = myStat.executeQuery("select * from wait_waiter");
				    			while(res.next()){
				    				data.add(new Cheftable(res.getString("wt_name")));
				    				waiter_ids.add(res.getInt("wt_id"));
				    			}
				    		} catch (SQLException e) {
				    			e.printStackTrace();
				    		}
				    		
				    		waiter_table.setItems(data);
				    		waiter_approved.setText("");
			        	}
			        }
			    }
			);
		remove_food.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	int i=0;
                for (FoodDetails p : atable.getItems()) {
                    if(p.isCheck()){
                    	try {
							st1.executeUpdate("DELETE FROM `hotel`.`food` WHERE `food`.`fd_id` = "+food_id.get(i));
						} catch (Exception e) {
							e.printStackTrace();
						}
                    }
                    i++;
                }
                /* new */
                try {
	    			res = myStat.executeQuery("select * from food natural join chef");
	    			ObservableList<FoodDetails> data2 = FXCollections.observableArrayList();
	    			food_id.clear();
	    			while(res.next()){
	    				data2.add(new FoodDetails(res.getString("fd_food"), res.getInt("fd_price"), res.getString("cf_name")));
	    				food_id.add(res.getInt("fd_id"));
	    			}
	                atable.setItems(data2);	
	    		} catch (SQLException e) {
	    			e.printStackTrace();
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
		
		search_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
	            try{
	            	send_search_keyword = search_query.getText();
	            	
					FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("admin_search.fxml"));
		            Parent root1 = (Parent) fxmlLoader.load();
		            Stage stage = new Stage();
		            Scene scene = new Scene(root1, 267, 410);    
		            stage.setTitle("FXML Welcome");
		            stage.setScene(scene);
		            stage.show();
	            }catch(Exception e){
	            	e.printStackTrace();
	            }
            }
        });
		
		approve_chefs.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	int i=0;
                for (Cheftable p : chef_table.getItems()) {
                    if(p.isApprove()){
                    	try {
							res1 = myStat.executeQuery("Select * from `wait_chef` where `cf_id`="+chef_ids.get(i));
							while(res1.next()){
								String cf_name = res1.getString("cf_name"), cf_pass = res1.getString("cf_pass"), cf_email = res1.getString("cf_email");
								st1.executeUpdate("INSERT INTO `chef`(`cf_name`, `cf_email`, `cf_pass`, `cf_info`, `ad_id`) VALUES ('"+cf_name+"', '"+cf_email+"', '"+cf_pass+"', 'chef', "+admin_id+")");
							}							
							st1.executeUpdate("DELETE FROM `hotel`.`wait_chef` WHERE `wait_chef`.`cf_id` = "+chef_ids.get(i));
							
							chefs_approved.setTextFill(Color.web("green"));
				    		chefs_approved.setText("Chefs Approved Successfully");

                    	} catch (SQLException e) {
							e.printStackTrace();
						}
                    }
                    i++;
                }
                /* new */
                ObservableList<Cheftable> data = FXCollections.observableArrayList();
	    		
	    		try {
	    			chef_ids.clear();
	    			res = myStat.executeQuery("select * from wait_chef");
	    			while(res.next()){
	    				data.add(new Cheftable(res.getString("cf_name")));
	    				chef_ids.add(res.getInt("cf_id"));
	    			}
	    		} catch (SQLException e) {
	    			e.printStackTrace();
	    		}
	    		
	    		chef_table.setItems(data);
            }
        });
		
		approve_waiter.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	int i=0;
	            for (Cheftable p : waiter_table.getItems()) {
	                if(p.isApprove()){
	                	try {
							res1 = myStat.executeQuery("Select * from `wait_waiter` where `wt_id`="+waiter_ids.get(i));
							while(res1.next()){
								String cf_name = res1.getString("wt_name"), cf_pass = res1.getString("wt_pass"), cf_email = res1.getString("wt_email");
								st1.executeUpdate("INSERT INTO `waiter`(`wt_name`, `wt_email`, `wt_pass`, `wt_info`, `ad_id`) VALUES ('"+cf_name+"', '"+cf_email+"', '"+cf_pass+"', 'waiter', "+admin_id+")");
							}							
							st1.executeUpdate("DELETE FROM `hotel`.`wait_waiter` WHERE `wait_waiter`.`wt_id` = "+waiter_ids.get(i));
							waiter_approved.setTextFill(Color.web("green"));
				    		waiter_approved.setText("Waiters Approved Successfully");
	                	} catch (SQLException e) {
							e.printStackTrace();
						}
	                }
                    i++;
                }
	            /* new */
	            ObservableList<Cheftable> data = FXCollections.observableArrayList();
	    		
	    		try {
	    			waiter_ids.clear();
	    			res = myStat.executeQuery("select * from wait_waiter");
	    			while(res.next()){
	    				data.add(new Cheftable(res.getString("wt_name")));
	    				waiter_ids.add(res.getInt("wt_id"));
	    			}
	    		} catch (SQLException e) {
	    			e.printStackTrace();
	    		}
	    		
	    		waiter_table.setItems(data);

	        }
	    });
	}	
}

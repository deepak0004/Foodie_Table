package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ControllerFirst implements Initializable {
	@FXML Button signin_btn, signup_btn;
	@FXML PasswordField signin_pass, signup_pass;
	@FXML TextField signin_email, signup_email, signup_name;
	@FXML Label signin_label1, signup_label1;
	@FXML ToggleGroup get_info;
	String info="";
	ResultSet res;
	Statement myStat;
	String type_info;
	static int userid;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try{
			Connection myconn = DriverManager.getConnection("jdbc:mysql://localhost/hotel", "root", "");
			myStat = myconn.createStatement();
		}catch(Exception e){
			System.out.println("ERROR!!");
		}
		
		signin_btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent Event) {
				try{
					String email  = signin_email.getText();
					String pass  = signin_pass.getText();	
					if(email.equals("") || pass.equals("")){
						signin_label1.setTextFill(Color.web("red"));
						signin_label1.setText("Please Enter both the Credentials!!");
					}else{
						int t=0;
						res = myStat.executeQuery("(select cf_id as id, cf_info as info from chef where cf_email='"+email+"' and cf_pass COLLATE Latin1_General_CS = '"+pass+"') union (select cs_id as id, cs_info  as info from customer where cs_email = '"+email+"' and cs_pass COLLATE Latin1_General_CS = '"+pass+"') union (select ad_id as id, ad_info  as info from admin where ad_email = '"+email+"' and ad_pass COLLATE Latin1_General_CS = '"+pass+"') union (select wt_id as wt_id, wt_info as info from waiter where wt_email='"+email+"' and wt_pass COLLATE Latin1_General_CS = '"+pass+"') union (select wt_id as wt_id, wt_info as info from wait_waiter where wt_email = '"+email+"' and wt_pass COLLATE Latin1_General_CS = '"+pass+"') union (select cf_id as id, cf_info from wait_chef where cf_email = '"+email+"' and cf_pass COLLATE Latin1_General_CS = '"+pass+"')");
						while(res.next()){
							type_info = res.getString("info");
							//setUserId(res.getInt("id"));
							userid = res.getInt("id");
							t++;
						}
						if(t==0){
							signin_label1.setTextFill(Color.web("red"));
							signin_label1.setText("No such account found!");
						}else{
							if(type_info.equalsIgnoreCase("Customer")){
								Stage cstage = (Stage) signin_btn.getScene().getWindow();
								cstage.close();
								FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("customer_side.fxml"));
					            Parent root1 = (Parent) fxmlLoader.load();
					            Stage stage = new Stage();
					            Scene scene = new Scene(root1, 600, 400);    
					            stage.setTitle("FXML Welcome");
					            stage.setScene(scene);
					            stage.show();
							}else if(type_info.equalsIgnoreCase("admin")){
								Stage cstage = (Stage) signin_btn.getScene().getWindow();
								cstage.close();
								FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("admin_page.fxml"));
					            Parent root1 = (Parent) fxmlLoader.load();
					            Stage stage = new Stage();
					            Scene scene = new Scene(root1, 600, 410);    
					            stage.setTitle("FXML Welcome");
					            stage.setScene(scene);
					            stage.show();
							}else if(type_info.equalsIgnoreCase("chef")){
								res = myStat.executeQuery("select * from chef where cf_id="+userid);
								Stage cstage = (Stage) signin_btn.getScene().getWindow();
								cstage.close();
								FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("chef_side.fxml"));
					            Parent root1 = (Parent) fxmlLoader.load();
					            Stage stage = new Stage();
					            Scene scene = new Scene(root1, 600, 400);    
					            stage.setTitle("FXML Welcome");
					            stage.setScene(scene);
					            stage.show();
							}else if(type_info.equalsIgnoreCase("waiter")){
								res = myStat.executeQuery("select * from waiter where wt_id="+userid);
								Stage cstage = (Stage) signin_btn.getScene().getWindow();
								cstage.close();
								FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("waiter_side.fxml"));
					            Parent root1 = (Parent) fxmlLoader.load();
					            Stage stage = new Stage();
					            Scene scene = new Scene(root1, 600, 400);    
					            stage.setTitle("FXML Welcome");
					            stage.setScene(scene);
					            stage.show();
							}else if(type_info.equalsIgnoreCase("nwt")){
								FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("waiter_not.fxml"));
					            Parent root1 = (Parent) fxmlLoader.load();
					            Stage stage = new Stage();
					            Scene scene = new Scene(root1, 600, 350);    
					            stage.setTitle("FXML Welcome");
					            stage.setScene(scene);
					            stage.show();
							}else if(type_info.equalsIgnoreCase("ncf")){
								FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("chef_not_approved.fxml"));
					            Parent root1 = (Parent) fxmlLoader.load();
					            Stage stage = new Stage();
					            Scene scene = new Scene(root1, 600, 350);    
					            stage.setTitle("FXML Welcome");
					            stage.setScene(scene);
					            stage.show();
							}
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		get_info.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {
				RadioButton chk = (RadioButton) t1.getToggleGroup().getSelectedToggle();
				info = chk.getText();
			}
		});
		signup_btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent Event) {
				try{
					String name  = signup_name.getText();
					String email  = signup_email.getText();
					String pass  = signup_pass.getText();
					if(email.equals("") || pass.equals("") || name.equals("") || info.equals("")){
						signup_label1.setTextFill(Color.web("red"));
						signup_label1.setText("Please Enter all details!!");
					}else{
						int t=0;
						res = myStat.executeQuery("select exists((select 1 from chef where cf_email='"+email+"') union (select 1 from customer where cs_email = '"+email+"') union (select 1 from admin where ad_email = '"+email+"') union (select 1 from waiter where wt_email='"+email+"') union (select 1 from wait_waiter where wt_email = '"+email+"') union (select 1 from wait_chef where cf_email = '"+email+"')) as k");
						while(res.next()){
							t = res.getInt("k");
						}
						if(t==1){
							signup_label1.setTextFill(Color.web("red"));
							signup_label1.setText("Email already exists!!");
						}else{
							if(info.equalsIgnoreCase("Customer")){
								myStat.executeUpdate("INSERT INTO customer(cs_name, cs_email, cs_pass, cs_info) VALUES ('"+name+"', '"+email+"', '"+pass+"', 'customer')");
							}else if(info.equalsIgnoreCase("chef")){
								myStat.executeUpdate("INSERT INTO `wait_chef`(`cf_name`, `cf_pass`, `cf_email`, `cf_info`) VALUES ('"+name+"', '"+pass+"', '"+email+"', 'ncf')");
							}else if(info.equalsIgnoreCase("waiter")){
								myStat.executeUpdate("INSERT INTO `wait_waiter`(`wt_name`, `wt_pass`, `wt_email`, `wt_info`) VALUES ('"+name+"', '"+pass+"', '"+email+"', 'nwt')");
							}
							signup_label1.setTextFill(Color.web("green"));
							signup_label1.setText("Your Account has been Created, Please login in to Continue!!");
							
							signup_name.clear();
							signup_email.clear();
							signup_pass.clear();
							
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
}

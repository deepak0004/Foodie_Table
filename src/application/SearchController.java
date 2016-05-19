package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SearchController  implements Initializable{

	@FXML TableColumn<AdminSearch, String> res_col, cat_col;
	@FXML TableView<AdminSearch> search_table;
	
	String query = AdminController.send_search_keyword;
	
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
		
		res_col.setCellValueFactory(new PropertyValueFactory<AdminSearch, String>("result"));
		cat_col.setCellValueFactory(new PropertyValueFactory<AdminSearch, String>("category"));
		
		ObservableList<AdminSearch> data = FXCollections.observableArrayList();
		
		try {
			res = myStat.executeQuery("select cs_name as result, cs_info as category from customer where cs_name like '%"+query+"%' union select cf_name as result, cf_info as category from chef where cf_name like '%"+query+"%' union select fd_food as result, fd_price as category from food where fd_food like '%"+query+"%'");
			while(res.next()){
				if(res.getString("category").equalsIgnoreCase("customer")){
					data.add(new AdminSearch(res.getString("result"), res.getString("category")));
				}else if(res.getString("category").equalsIgnoreCase("chef")){
					data.add(new AdminSearch(res.getString("result"), res.getString("category")));
				}else{
					data.add(new AdminSearch(res.getString("result"), "Food"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		search_table.setItems(data);	
	}

}

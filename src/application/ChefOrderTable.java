package application;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ChefOrderTable {
	
	public final SimpleStringProperty food;
	public final SimpleStringProperty customer;
	public final SimpleIntegerProperty price;    
    public final BooleanProperty check;

    
    public ChefOrderTable(String food, Integer price, String c) {
        this.food = new SimpleStringProperty(food);
        this.price = new SimpleIntegerProperty(price);
        this.check = new SimpleBooleanProperty(false);
        this.customer = new SimpleStringProperty(c);
    }
    
    public boolean isCheck() {
        return check.get();
    }
 
    public String getFood() {
        return food.get();
    }
    public String getCustomer() {
        return customer.get();
    }
    public void setFood(String f) {
        food.set(f);
    }
    public void setCustomer(String f) {
        customer.set(f);
    }
        
    public Integer getPrice() {
        return price.get();
    }
    public void setPrice(Integer p) {
        price.set(p);
    }
    public void setCheck(boolean check) {
        this.check.set(check);
    }
    public StringProperty foodProperty() {
        return food;
    }
    public StringProperty customerProperty() {
        return customer;
    }

    public IntegerProperty priceProperty() {
        return price;
    }

    public BooleanProperty checkProperty() {
        return check;
    }
}

package application;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CustomerBill {
	
	public final SimpleStringProperty food;
    public final SimpleIntegerProperty price;
    public final SimpleStringProperty status;
    public final BooleanProperty remove;
    
    public CustomerBill(String food, Integer price, String s) {
        this.food = new SimpleStringProperty(food);
        this.price = new SimpleIntegerProperty(price);
        this.status = new SimpleStringProperty(s);
        this.remove = new SimpleBooleanProperty(false);
    }
    
    
    public boolean isCheck() {
        return remove.get();
    }
    
    public String getFood() {
        return food.get();
    }
    public String getStatus() {
        return status.get();
    }
    public Integer getPrice() {
        return price.get();
    }
    
    public void setFood(String f) {
        food.set(f);
    }
    public void setStatus(String f) {
        status.set(f);
    }  
    public void setRemove(boolean check) {
        this.remove.set(check);
    }
    
    public void setPrice(Integer p) {
        price.set(p);
    }
    
    public StringProperty foodProperty() {
        return food;
    }
    public IntegerProperty priceProperty() {
        return price;
    }
    public StringProperty statusProperty() {
        return status;
    }
    public BooleanProperty removeProperty() {
        return remove;
    }
}

package application;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Foodtable {
	
	public final SimpleStringProperty food;
    public final SimpleIntegerProperty price;
    public final BooleanProperty check;

    
    public Foodtable(String food, Integer price) {
        this.food = new SimpleStringProperty(food);
        this.price = new SimpleIntegerProperty(price);
        this.check = new SimpleBooleanProperty(false);
    }
    
    public boolean isCheck() {
        return check.get();
    }
 
    public String getFood() {
        return food.get();
    }
    public void setFood(String f) {
        food.set(f);
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

    public IntegerProperty priceProperty() {
        return price;
    }

    public BooleanProperty checkProperty() {
        return check;
    }
}

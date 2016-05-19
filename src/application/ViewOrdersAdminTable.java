package application;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ViewOrdersAdminTable {
	public final SimpleStringProperty name;
	public final SimpleStringProperty food;
    public final SimpleIntegerProperty price;
        
    public ViewOrdersAdminTable(String name, String food, Integer price) {
    	this.name = new SimpleStringProperty(name);
    	this.food = new SimpleStringProperty(food);
        this.price = new SimpleIntegerProperty(price);
    }
    
    public String getName() {
        return this.name.get();
    }
    public String getFood() {
        return this.food.get();
    }
    public Integer getPrice() {
        return price.get();
    }
    
    
    public void setFood(String f) {
        this.food.set(f);
    }
    public void setPrice(Integer p) {
        price.set(p);
    }
    public void setName(String name) {
        this.name.set(name);
    }
    
    
    public StringProperty foodProperty() {
        return food;
    }
    public IntegerProperty priceProperty() {
        return price;
    }

    public StringProperty nameProperty() {
        return name;
    }
}


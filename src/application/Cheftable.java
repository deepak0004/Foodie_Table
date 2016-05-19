package application;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Cheftable {
	
	public final SimpleStringProperty chef;
    public final BooleanProperty approve;
    
    
    public Cheftable(String chef) {
        this.chef = new SimpleStringProperty(chef);
        this.approve = new SimpleBooleanProperty(false);
    }
    
    public boolean isApprove() {
        return approve.get();
    }
 
    public String getChef() {
        return chef.get();
    }
    public void setChef(String f) {
        chef.set(f);
    }
    public void setApprove(boolean approve) {
        this.approve.set(approve);
    }
    public StringProperty chefProperty() {
        return chef;
    }
    public BooleanProperty approveProperty() {
        return approve;
    }
}

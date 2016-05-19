package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AdminSearch {
	
	public final SimpleStringProperty result;
    public final SimpleStringProperty category;
    
    public AdminSearch(String result, String category) {
        this.result = new SimpleStringProperty(result);
        this.category = new SimpleStringProperty(category);
    }     
    public String getResult() {
        return result.get();
    }
    public void setResult(String r) {
        result.set(r);
    }        
    public String getCategory() {
        return category.get();
    }
    public void setCategory(String p) {
        category.set(p);
    }
    public StringProperty resultProperty() {
        return result;
    }

    public StringProperty categoryProperty() {
        return category;
    }
}

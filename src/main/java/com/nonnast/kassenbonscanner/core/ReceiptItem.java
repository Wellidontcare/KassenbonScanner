package com.nonnast.kassenbonscanner.core;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;

public class ReceiptItem {

    private StringProperty name_prop;
    private StringProperty price_prop;
    private StringProperty date_prop;

    public ReceiptItem(String name, double price){
        this.price = price;
        this.name = name;

    }

    public StringProperty name_property(){
       if(name_prop == null) name_prop = new SimpleStringProperty(this.name);
       return name_prop;
    }

    public StringProperty price_property(){
       if(price_prop == null) price_prop = new SimpleStringProperty(Double.toString(price));
       return price_prop;
    }

    public double price;
    public String name;
}

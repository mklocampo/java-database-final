package com.project.code.DTO;

import com.project.code.Model.Inventory;
import com.project.code.Model.Product;

public class CombinedRequestDTO {
    
    private Product product;
    private Inventory inventory;

    public Product getProduct() {
        return product;
    }

    // Setter for product
    public void setProduct(Product product) {
        this.product = product;
    }

    // Getter for store
    public Inventory getInventory() {
        return inventory;
    }

    // Setter for store
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
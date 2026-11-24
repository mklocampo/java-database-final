package com.project.code.Service;

import org.springframework.stereotype.Service;

import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Repository.CustomerRepository;
import com.project.code.Repository.InventoryRepository;
import com.project.code.Repository.OrderDetailsRepository;
import com.project.code.Repository.OrderItemRepository;
import com.project.code.Repository.ProductRepository;
import com.project.code.Repository.StoreRepository;

@Service
public class ServiceClass {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    /*
     * private final CustomerRepository customerRepository;
     * private final StoreRepository storeRepository;
     * private final OrderDetailsRepository orderDetailsRepository;
     * private final OrderItemRepository orderItemRepository;
     */
    public ServiceClass(ProductRepository productRepository, InventoryRepository inventoryRepository,
            CustomerRepository customerRepository, StoreRepository storeRepository,
            OrderDetailsRepository orderDetailsRepository, OrderItemRepository orderItemRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        /*
         * this.customerRepository = customerRepository;
         * this.storeRepository = storeRepository;
         * this.orderDetailsRepository = orderDetailsRepository;
         * this.orderItemRepository = orderItemRepository;
         */
    }

    // 1. **validateInventory Method**:
    // - Checks if an inventory record exists for a given product and store
    // combination.
    // - Parameters: `Inventory inventory`
    // - Return Type: `boolean` (Returns `false` if inventory exists, otherwise
    // `true`)
    public Boolean validateInventory(Inventory paramInventory) {
        Inventory inventory = inventoryRepository.findByProductIdandStoreId(paramInventory.getProduct().getId(),
                paramInventory.getStore().getId());
        return inventory == null;
    }

    // 2. **validateProduct Method**:
    // - Checks if a product exists by its name.
    // - Parameters: `Product product`
    // - Return Type: `boolean` (Returns `false` if a product with the same name
    // exists, otherwise `true`)
    public Boolean validateProduct(Product paramProduct) {
        Product product = (productRepository.findByName(paramProduct.getName()));
        return product == null;
    }

    // 3. **ValidateProductId Method**:
    // - Checks if a product exists by its ID.
    // - Parameters: `long id`
    // - Return Type: `boolean` (Returns `false` if the product does not exist with
    // the given ID, otherwise `true`)
    public Boolean ValidateProductId(long id) {
        Product product = productRepository.findById(id).orElse(null);
        return product == null;
    }

    // 4. **getInventoryId Method**:
    // - Fetches the inventory record for a given product and store combination.
    // - Parameters: `Inventory inventory`
    // - Return Type: `Inventory` (Returns the inventory record for the
    // product-store combination)
    public Inventory getInventoryId(Inventory paramInventory) {
        return inventoryRepository.findByProductIdandStoreId(paramInventory.getProduct().getId(),
                paramInventory.getStore().getId());
    }

}

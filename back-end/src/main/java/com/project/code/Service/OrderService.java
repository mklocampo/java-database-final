package com.project.code.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.code.DTO.PlaceOrderRequestDTO;
import com.project.code.DTO.PurchaseProductDTO;
import com.project.code.Model.Customer;
import com.project.code.Model.Inventory;
import com.project.code.Model.OrderDetails;
import com.project.code.Model.OrderItem;
import com.project.code.Model.Product;
import com.project.code.Model.Store;
import com.project.code.Repository.CustomerRepository;
import com.project.code.Repository.InventoryRepository;
import com.project.code.Repository.OrderDetailsRepository;
import com.project.code.Repository.OrderItemRepository;
import com.project.code.Repository.ProductRepository;
import com.project.code.Repository.StoreRepository;

@Service
public class OrderService {

    //@PersistenceContext
    //private EntityManager entityManager;

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(ProductRepository productRepository, InventoryRepository inventoryRepository,
            CustomerRepository customerRepository, StoreRepository storeRepository,
            OrderDetailsRepository orderDetailsRepository, OrderItemRepository orderItemRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.customerRepository = customerRepository;
        this.storeRepository = storeRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.orderItemRepository = orderItemRepository;
    }

    // 1. **saveOrder Method**:
    // - Processes a customer's order, including saving the order details and
    // associated items.
    // - Parameters: `PlaceOrderRequestDTO placeOrderRequest` (Request data for
    // placing an order)
    // - Return Type: `void` (This method doesn't return anything, it just processes
    // the order)
    public void saveOrder(PlaceOrderRequestDTO placeOrderRequestDTO) {
        // 3. **Retrieve the Store**:
        // - Fetch the store by ID from `storeRepository`.
        // - If the store doesn't exist, throw an exception. Use
        // `storeRepository.findById()`.
        //Store store = storeRepository.findById(placeOrderRequestDTO.getStoreId()) .orElseThrow(() -> new RuntimeException("The store cannot be found"));
        Store store = storeRepository.findById(placeOrderRequestDTO.getStoreId()).orElse(null);
        if (store == null) {
            throw new Error("The store cannot be found");
        }

        // 2. **Retrieve or Create the Customer**:
        // - Check if the customer exists by their email using `findByEmail`.
        // - If the customer exists, use the existing customer; otherwise, create and
        // save a new customer using `customerRepository.save()`.
        Customer customer = customerRepository.findByEmail(placeOrderRequestDTO.getCustomerEmail());
        if (customer == null) {
            customer = new Customer();
            customer.setName(placeOrderRequestDTO.getCustomerName());
            customer.setEmail(placeOrderRequestDTO.getCustomerEmail());
            customer.setPhone(placeOrderRequestDTO.getCustomerPhone());
            customer = customerRepository.save(customer);
        }

        // 4. **Create OrderDetails**:
        // - Create a new `OrderDetails` object and set customer, store, total price,
        // and the current timestamp.
        // - Set the order date using `java.time.LocalDateTime.now()` and save the order
        // with `orderDetailsRepository.save()`.
        OrderDetails orderDetails = new OrderDetails(customer, store, placeOrderRequestDTO.getTotalPrice()
        // LocalDateTime.parse(placeOrderRequestDTO.getDatetime()));
                , java.time.LocalDateTime.now());
        orderDetails = orderDetailsRepository.save(orderDetails);

        // 5. **Create and Save OrderItems**:
        // - For each product purchased, find the corresponding inventory, update stock
        // levels, and save the changes using `inventoryRepository.save()`.
        // - Create and save `OrderItem` for each product and associate it with the
        // `OrderDetails` using `orderItemRepository.save()`.
        List<PurchaseProductDTO> ProductListDTO = placeOrderRequestDTO.getPurchaseProduct();
        for (PurchaseProductDTO productDTO : ProductListDTO) {
            Inventory inventory = inventoryRepository.findByProductIdandStoreId(productDTO.getId(), store.getId());
            if (inventory != null) {
                if (inventory.getStockLevel() > productDTO.getQuantity()) {
                    Product product = productRepository.findByName(productDTO.getName());
                    if (product != null) {
                        inventory.setStockLevel(inventory.getStockLevel() - productDTO.getQuantity());
                        OrderItem orderItem = new OrderItem(orderDetails, product, productDTO.getQuantity(),
                                productDTO.getPrice());
                        orderItemRepository.save(orderItem);
                        inventoryRepository.save(inventory);
                    }
                } else {
                    System.out.println("Quantity of items greater than the stock for product: " + productDTO.getName());
                }
            } else {
                System.out.println("The inventory cannot be found for product: " + productDTO.getName());
            }
        }
    }

}

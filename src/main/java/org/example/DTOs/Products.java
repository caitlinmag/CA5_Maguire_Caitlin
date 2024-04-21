package org.example.DTOs;

public class Products {
    int product_ID;
    String productName;
    String productType;
    int quantity;
    float price;
    int staff_ID;

    public Products(int product_ID, String productName, String productType, int quantity, float price, int staff_ID) {
        this.product_ID = product_ID;
        this.productName = productName;
        this.productType = productType;
        this.quantity = quantity;
        this.price = price;
        this.staff_ID = staff_ID;
    }

    public int getProduct_ID() {
        return product_ID;
    }

    public void setProduct_ID(int product_ID) {
        this.product_ID = product_ID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStaff_ID() {
        return staff_ID;
    }

    public void setStaff_ID(int staff_ID) {
        this.staff_ID = staff_ID;
    }

    @Override
    public String toString() {
        return "Products{" +
                "product_ID=" + product_ID +
                ", productName='" + productName + '\'' +
                ", productType='" + productType + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", staff_ID=" + staff_ID +
                '}';
    }
}

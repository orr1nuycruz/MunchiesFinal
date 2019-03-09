public class CustomerCartClass {
    private String userEmail, itemName;
    private double price;

    public CustomerCartClass() {

    }

    public CustomerCartClass(String userEmail, String itemName, double price) {
        this.userEmail = userEmail;
        this.itemName = itemName;
        this.price = price;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

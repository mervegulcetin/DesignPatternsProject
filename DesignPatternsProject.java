import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
public class DesignPatternsProject {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Patterned Cart Master");
        System.out.println("For create new account press 1");
        int choice1 = scanner.nextInt();
        while(choice1!=1){
            System.out.println("Invalid choice try again");
            choice1=scanner.nextInt();
        }
        System.out.println("Account created succesfully");
        scanner.nextLine();
        System.out.print("How may I address you?");
        String name=scanner.nextLine();
        System.out.println(name+" Select your payment method:");
        System.out.println("1)Credit card");
        System.out.println("2)PayPal");
        int choice2=scanner.nextInt();
        while(choice2!=1 && choice2!=2){
            System.out.println("Invalid choice try again");
            choice2=scanner.nextInt();
        }
        User user1 = null;
        if(choice2==1){
            System.out.println("Enter your card number:");
            scanner.nextLine();
            String card1Num=scanner.nextLine();
            System.out.println("Enter your card's expiration date:");
            String card1ED=scanner.nextLine();
            System.out.println("Enter your card's cvv:");
            String card1cvv=scanner.nextLine();
            CreditCardPayment cart1=new CreditCardPayment(card1Num,card1ED,card1cvv);
            user1=new User(name,cart1);
            System.out.println("Credit card added succesfully!");
        }

        if(choice2==2){
            System.out.println("Enter your email address:");
            scanner.nextLine();
            String paypalmail=scanner.nextLine();
            PayPalPayment paypal=new PayPalPayment(paypalmail);
            user1=new User(name,paypal);
            System.out.println("Your PayPal added succesfully!");

        }

        System.out.println("Welcome to the main page "+name);
        Product laptop = new Product("Laptop", 1200.0, true);
        Product mouse =new Product("Mouse",300,true);
        Product tablet=new Product("Tablet",5000,true);
        System.out.println("1)"+laptop.getProductName()+" Price:"+laptop.getPrice());
        System.out.println("2)"+mouse.getProductName()+" Price:"+mouse.getPrice());
        System.out.println("3)"+tablet.getProductName()+" Price:"+tablet.getPrice());


        System.out.println("Which product would you like to add to your cart?");
        int choice3=scanner.nextInt();

        while(choice3!=1 && choice3!=2 && choice3!=3){
            System.out.println("Product you have called cannot be found try again!");
            choice3=scanner.nextInt();
        }


        if(choice3==1){
            laptop.addObserver(user1);
            laptop.notifyObservers();
            AddToCartCommand adc = new AddToCartCommand(laptop);
            adc.execute();
        }
        if(choice3==2){
            mouse.addObserver(user1);
            AddToCartCommand adc = new AddToCartCommand(mouse);
            adc.execute();
        }
        if(choice3==3){
            tablet.addObserver(user1);
            AddToCartCommand adc = new AddToCartCommand(tablet);
            adc.execute();
        }


        ShoppingCart sc= ShoppingCart.getShoppingCartForUser(user1);
        user1.makePayment();
        CreditCardPayment c1=new CreditCardPayment("6543","345","66");
        user1.setPaymentStrategy(c1);
        CheckoutCommand co=new CheckoutCommand(sc);
        co.execute();
        //   tablet.setPrice(15000);
        //sc.getTotalCost();
    }

}


/*

    CreditCardPayment cart1=new CreditCardPayment("545468","85654","95");
    PayPalPayment paypal=new PayPalPayment("elif@gmail.com");
    Product laptop = new Product("Laptop", 1200.0, true);
    Product mouse =new Product("mouse",300,true);
    Product tablet=new Product("tablet",5000,true);
    // Creating users (observers)
    User user1 = new User("User1",cart1);
    User user2 = new User("User2",cart1);
    User user3= new User("merve",cart1);
    User user4=new User("elif",paypal);

        tablet.addObserver(user4);
                ShoppingCart user4sc=ShoppingCart.getShoppingCartForUser(user4);

                AddToCartCommand ac=new AddToCartCommand(tablet);
                AddToCartCommand ac1=new AddToCartCommand(mouse);
                CheckoutCommand co=new CheckoutCommand(user4sc);
                co.execute();
                ac.execute();
                ac1.execute();

                user4.makePayment();


                mouse.addObserver(user3);
                mouse.setPrice(250);
                mouse .setAvailability(false);
                // Registering users as observers for the product
                laptop.addObserver(user1);
                laptop.addObserver(user2);



                // Simulating price and availability updates
                laptop.setPrice(1100.0);
                laptop.setAvailability(false);
                }

*/

interface Subject {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
}

interface Observer{
    void update(Product product);
}

class Product implements Subject {
    private String productName;
    private double price;
    private boolean available;
    private List<Observer> observers = new ArrayList<>();

    public Product(String productName, double price, boolean available) {
        this.productName = productName;
        this.price = price;
        this.available = available;
    }

    public void setPrice(double price) {
        this.price = price;
        notifyObservers();
    }

    public void setAvailability(boolean available) {
        this.available = available;
        notifyObservers();
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }
    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }

}

class User implements Observer {
    private String userName;
    private PaymentStrategy paymentStrategy;
    private ShoppingCart shoppingCart = ShoppingCart.getInstance();
    private List<Command> commandHistory = new ArrayList<>();

    public User(String userName,PaymentStrategy paymentStrategy) {
        this.userName = userName;
        this.paymentStrategy = paymentStrategy;
    }


    @Override
    public void update(Product product) {
        System.out.println(userName + ": Product " + product.getProductName() +
                " - Price: " + product.getPrice() + ", Available: " + product.isAvailable());
    }


    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    // Make a payment using the associated payment strategy
    public void makePayment() {
      /*  double amount=0;
        for(Product product:ShoppingCart.getItems()) {
            amount += product.getPrice();
        }*/
        if (paymentStrategy != null) {
            System.out.println(userName + " is making a payment.");
            paymentStrategy.pay(shoppingCart.getTotalCost());
        } else {
            System.out.println("Payment strategy not set for " + userName);
        }
    }

    public void performAction(Command command) {
        command.execute();
        commandHistory.add(command);
    }

    public void undoLastAction() {
        if (!commandHistory.isEmpty()) {
            Command lastCommand = commandHistory.remove(commandHistory.size() - 1);
            lastCommand.undo();
        } else {
            System.out.println("No actions to undo.");
        }
    }
}

interface PaymentStrategy {
    void pay(double amount);
}

class CreditCardPayment implements PaymentStrategy {
    private String cardNumber;
    private String expirationDate;
    private String cvv;

    public CreditCardPayment(String cardNumber, String expirationDate, String cvv) {
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
    }

    @Override
    public void pay(double amount) {
        // Implementation for making a payment using a credit card
        System.out.println("Paid " + amount + " using Credit Card.");
    }
}

class PayPalPayment implements PaymentStrategy {
    private String email;

    public PayPalPayment(String email) {
        this.email = email;
    }

    @Override
    public void pay(double amount) {
        // Implementation for making a payment using PayPal
        System.out.println("Paid " + amount + " using PayPal.");
    }
}
class PaymentContext {
    private PaymentStrategy paymentStrategy;

    public PaymentContext(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void executePayment(double amount) {
        paymentStrategy.pay(amount);
    }
}

interface Command {
    void execute();

    void undo();
}

// ConcreteCommand classes
class AddToCartCommand implements Command {
    private Product product;

    AddToCartCommand(Product product) {
        this.product = product;
    }

    public void execute() {
        // Perform the actual action, add item to the cart
        ShoppingCart.addToCart(product);
        System.out.println("Added " + product.getProductName() + " to the cart.");

        // print the current items in the  cart.
        System.out.println("Current contents of the cart:");
        ShoppingCart.printCart();
    }

    public void undo() {
        // Undo the action, remove the item from the cart
        ShoppingCart.removeFromCart(product);
        System.out.println("Removed " + product.getProductName() + " from the cart.");

        //Again, prints contents of the cart
        System.out.println("Current contents of the cart:");
        ShoppingCart.printCart();
    }
}

class
CheckoutCommand implements Command {
    private ShoppingCart shoppingCart;

    public CheckoutCommand(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }
    public void execute() {
        ShoppingCart.checkout();
        System.out.println("Checkout completed. Thank you!");
    }

    public void undo() {
        // Undo the checkout if needed
        ShoppingCart.cancelCheckout();
        System.out.println("Checkout canceled. Items are still in the cart.");
    }
}

// Receiver class(Command Pattern)
class ShoppingCart {
    private static ShoppingCart shoppingCart;
    private static List<Product> items = new ArrayList<>();
    private User user;

    public ShoppingCart(User user) {
        this.user = user;
    }
    private ShoppingCart(){

    }

    public static List<Product> getItems() {
        return items;
    }

    public static synchronized ShoppingCart getInstance(){
        if (shoppingCart==null) {
            shoppingCart = new ShoppingCart();
        }
        return shoppingCart;

    }

    public static void addToCart(Product product) {
        items.add(product);
    }

    public static void removeFromCart(Product product) {
        items.remove(product);
    }

    static void printCart() {
        for(Product item:items){
            System.out.println(item.getProductName());
        }
    }

    public User getUser(){
        return user;
    }

    public static ShoppingCart getShoppingCartForUser(User user) {
        ShoppingCart userShoppingCart = new ShoppingCart();
        userShoppingCart.user = user;
        return userShoppingCart;
    }

    static void checkout() {
        double totalCost = calculateTotalCost();
        System.out.println("Total cost: " + totalCost);
    }

    static void cancelCheckout() {
        System.out.println("Checkout canceled. Items are still in the cart.");
    }

    private static double calculateTotalCost() {
        // Calculate the total cost based on the items in the cart
        double totalCost = 0.0;
        for (Product product : items) {
            totalCost += product.getPrice();
        }
        return totalCost;
    }
    public double getTotalCost(){
        double amount=calculateTotalCost();
        return amount;
    }

}
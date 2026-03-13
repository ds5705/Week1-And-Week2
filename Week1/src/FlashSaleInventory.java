import java.util.*;

public class FlashSaleInventory {

    private HashMap<String, Integer> stock = new HashMap<>();
    private HashMap<String, LinkedList<Integer>> waitingList = new HashMap<>();

    public void addProduct(String productId, int quantity) {
        stock.put(productId, quantity);
        waitingList.put(productId, new LinkedList<>());
    }

    public String checkStock(String productId) {
        int quantity = stock.getOrDefault(productId, 0);
        return quantity + " units available";
    }

    public synchronized String purchaseItem(String productId, int userId) {

        int quantity = stock.getOrDefault(productId, 0);

        if (quantity > 0) {
            stock.put(productId, quantity - 1);
            return "Success, " + (quantity - 1) + " units remaining";
        }
        else {
            LinkedList<Integer> queue = waitingList.get(productId);
            queue.add(userId);
            return "Added to waiting list, position #" + queue.size();
        }
    }

    public static void main(String[] args) {

        FlashSaleInventory system = new FlashSaleInventory();

        system.addProduct("IPHONE15_256GB", 100);

        System.out.println(system.checkStock("IPHONE15_256GB"));

        System.out.println(system.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(system.purchaseItem("IPHONE15_256GB", 67890));

        for(int i=0;i<100;i++){
            system.purchaseItem("IPHONE15_256GB", i);
        }

        System.out.println(system.purchaseItem("IPHONE15_256GB", 99999));
    }
}
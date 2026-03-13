import java.util.*;

class Transaction {
    int id;
    int amount;
    String merchant;
    String account;
    long time;

    Transaction(int id, int amount, String merchant, String account, long time) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.account = account;
        this.time = time;
    }
}

public class FraudDetection {

    public static List<int[]> findTwoSum(List<Transaction> transactions, int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();
        List<int[]> result = new ArrayList<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                result.add(new int[]{map.get(complement).id, t.id});
            }

            map.put(t.amount, t);
        }

        return result;
    }

    public static List<int[]> findTwoSumTimeWindow(List<Transaction> transactions, int target, long window) {

        HashMap<Integer, Transaction> map = new HashMap<>();
        List<int[]> result = new ArrayList<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction prev = map.get(complement);

                if (Math.abs(t.time - prev.time) <= window) {
                    result.add(new int[]{prev.id, t.id});
                }
            }

            map.put(t.amount, t);
        }

        return result;
    }

    public static List<List<Integer>> findKSum(List<Transaction> transactions, int k, int target) {

        List<List<Integer>> result = new ArrayList<>();
        kSumHelper(transactions, 0, k, target, new ArrayList<>(), result);
        return result;
    }

    private static void kSumHelper(List<Transaction> transactions, int start, int k, int target,
                                   List<Integer> current, List<List<Integer>> result) {

        if (k == 0 && target == 0) {
            result.add(new ArrayList<>(current));
            return;
        }

        if (k == 0) return;

        for (int i = start; i < transactions.size(); i++) {

            current.add(transactions.get(i).id);

            kSumHelper(transactions, i + 1, k - 1,
                    target - transactions.get(i).amount, current, result);

            current.remove(current.size() - 1);
        }
    }

    public static Map<String, List<String>> detectDuplicates(List<Transaction> transactions) {

        HashMap<String, List<String>> map = new HashMap<>();

        for (Transaction t : transactions) {

            String key = t.amount + "-" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t.account);
        }

        return map;
    }

    public static void main(String[] args) {

        List<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction(1, 500, "StoreA", "acc1", 1000));
        transactions.add(new Transaction(2, 300, "StoreB", "acc2", 1100));
        transactions.add(new Transaction(3, 200, "StoreC", "acc3", 1200));

        List<int[]> pairs = findTwoSum(transactions, 500);

        for (int[] p : pairs) {
            System.out.println("Pair: " + p[0] + " , " + p[1]);
        }

        List<List<Integer>> ksum = findKSum(transactions, 3, 1000);

        for (List<Integer> list : ksum) {
            System.out.println("K-Sum: " + list);
        }

        Map<String, List<String>> dup = detectDuplicates(transactions);

        System.out.println(dup);
    }
}
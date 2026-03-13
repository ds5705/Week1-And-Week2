import java.util.*;

public class UsernameChecker {

    private HashMap<String, Integer> users = new HashMap<>();
    private HashMap<String, Integer> attemptFrequency = new HashMap<>();

    public boolean checkAvailability(String username) {

        attemptFrequency.put(username, attemptFrequency.getOrDefault(username, 0) + 1);

        if (users.containsKey(username)) {
            return false;
        }
        return true;
    }

    public void registerUser(String username, int userId) {
        users.put(username, userId);
    }

    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        suggestions.add(username + "1");
        suggestions.add(username + "2");
        suggestions.add(username.replace("_", "."));
        suggestions.add(username + "_official");
        suggestions.add(username + "123");

        return suggestions;
    }

    public String getMostAttempted() {

        String mostAttempted = "";
        int max = 0;

        for (String user : attemptFrequency.keySet()) {

            int count = attemptFrequency.get(user);

            if (count > max) {
                max = count;
                mostAttempted = user;
            }
        }

        return mostAttempted + " (" + max + " attempts)";
    }

    public static void main(String[] args) {

        UsernameChecker system = new UsernameChecker();

        system.registerUser("john_doe", 101);
        system.registerUser("admin", 102);

        System.out.println(system.checkAvailability("john_doe"));
        System.out.println(system.checkAvailability("jane_smith"));

        System.out.println(system.suggestAlternatives("john_doe"));

        System.out.println(system.getMostAttempted());
    }
}
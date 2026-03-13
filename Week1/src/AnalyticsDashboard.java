import java.util.*;

class Event {
    String url;
    String userId;
    String source;

    Event(String url, String userId, String source) {
        this.url = url;
        this.userId = userId;
        this.source = source;
    }
}

public class AnalyticsDashboard {

    private HashMap<String, Integer> pageViews = new HashMap<>();
    private HashMap<String, HashSet<String>> uniqueVisitors = new HashMap<>();
    private HashMap<String, Integer> trafficSources = new HashMap<>();

    public void processEvent(Event e) {

        pageViews.put(e.url, pageViews.getOrDefault(e.url, 0) + 1);

        uniqueVisitors.putIfAbsent(e.url, new HashSet<>());
        uniqueVisitors.get(e.url).add(e.userId);

        trafficSources.put(e.source, trafficSources.getOrDefault(e.source, 0) + 1);
    }

    public void getDashboard() {

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());

        pq.addAll(pageViews.entrySet());

        System.out.println("Top Pages:");

        int count = 0;
        while (!pq.isEmpty() && count < 10) {

            Map.Entry<String, Integer> entry = pq.poll();
            String url = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(url).size();

            System.out.println((count + 1) + ". " + url + " - " + views + " views (" + unique + " unique)");

            count++;
        }

        int total = 0;
        for (int v : trafficSources.values()) {
            total += v;
        }

        System.out.println("\nTraffic Sources:");

        for (String source : trafficSources.keySet()) {

            int c = trafficSources.get(source);
            double percent = (c * 100.0) / total;

            System.out.println(source + ": " + percent + "%");
        }
    }

    public static void main(String[] args) {

        AnalyticsDashboard dashboard = new AnalyticsDashboard();

        dashboard.processEvent(new Event("/article/breaking-news", "user_123", "google"));
        dashboard.processEvent(new Event("/article/breaking-news", "user_456", "facebook"));
        dashboard.processEvent(new Event("/sports/championship", "user_111", "direct"));
        dashboard.processEvent(new Event("/sports/championship", "user_222", "google"));
        dashboard.processEvent(new Event("/sports/championship", "user_333", "google"));

        dashboard.getDashboard();
    }
}
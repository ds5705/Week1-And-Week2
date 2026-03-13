import java.util.*;

class VideoData {
    String videoId;
    String content;

    VideoData(String videoId, String content) {
        this.videoId = videoId;
        this.content = content;
    }
}

class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private int capacity;

    LRUCache(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
}

public class MultiLevelCache {

    private LRUCache<String, VideoData> L1;
    private LRUCache<String, VideoData> L2;
    private HashMap<String, VideoData> L3;

    private int L1Hits = 0;
    private int L2Hits = 0;
    private int L3Hits = 0;

    public MultiLevelCache() {
        L1 = new LRUCache<>(10000);
        L2 = new LRUCache<>(100000);
        L3 = new HashMap<>();
    }

    public VideoData getVideo(String videoId) {

        if (L1.containsKey(videoId)) {
            L1Hits++;
            return L1.get(videoId);
        }

        if (L2.containsKey(videoId)) {
            L2Hits++;
            VideoData data = L2.get(videoId);
            L1.put(videoId, data);
            return data;
        }

        if (L3.containsKey(videoId)) {
            L3Hits++;
            VideoData data = L3.get(videoId);
            L2.put(videoId, data);
            return data;
        }

        VideoData data = new VideoData(videoId, "VideoContent_" + videoId);
        L3.put(videoId, data);
        L2.put(videoId, data);

        return data;
    }

    public void getStatistics() {

        int total = L1Hits + L2Hits + L3Hits;

        double l1Rate = total == 0 ? 0 : (L1Hits * 100.0) / total;
        double l2Rate = total == 0 ? 0 : (L2Hits * 100.0) / total;
        double l3Rate = total == 0 ? 0 : (L3Hits * 100.0) / total;

        System.out.println("L1 Hit Rate: " + l1Rate + "%");
        System.out.println("L2 Hit Rate: " + l2Rate + "%");
        System.out.println("L3 Hit Rate: " + l3Rate + "%");
    }

    public static void main(String[] args) {

        MultiLevelCache cache = new MultiLevelCache();

        cache.getVideo("video_123");
        cache.getVideo("video_123");
        cache.getVideo("video_999");

        cache.getStatistics();
    }
}
import java.util.*;

public class PlagiarismDetector {

    private HashMap<String, Set<String>> ngramIndex = new HashMap<>();
    private int n = 5;

    public List<String> generateNGrams(String text) {

        List<String> grams = new ArrayList<>();
        String[] words = text.split("\\s+");

        for (int i = 0; i <= words.length - n; i++) {

            StringBuilder gram = new StringBuilder();

            for (int j = i; j < i + n; j++) {
                gram.append(words[j]).append(" ");
            }

            grams.add(gram.toString().trim());
        }

        return grams;
    }

    public void addDocument(String docId, String text) {

        List<String> grams = generateNGrams(text);

        for (String gram : grams) {

            ngramIndex.putIfAbsent(gram, new HashSet<>());

            ngramIndex.get(gram).add(docId);
        }
    }

    public void analyzeDocument(String docId, String text) {

        List<String> grams = generateNGrams(text);

        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String gram : grams) {

            if (ngramIndex.containsKey(gram)) {

                for (String existingDoc : ngramIndex.get(gram)) {

                    if (!existingDoc.equals(docId)) {

                        matchCount.put(existingDoc,
                                matchCount.getOrDefault(existingDoc, 0) + 1);
                    }
                }
            }
        }

        System.out.println("Extracted " + grams.size() + " n-grams");

        for (String doc : matchCount.keySet()) {

            int matches = matchCount.get(doc);

            double similarity = (matches * 100.0) / grams.size();

            System.out.println("Found " + matches + " matching n-grams with " + doc);
            System.out.println("Similarity: " + similarity + "%");
        }
    }

    public static void main(String[] args) {

        PlagiarismDetector detector = new PlagiarismDetector();

        String essay1 = "machine learning is a field of artificial intelligence that focuses on data";
        String essay2 = "machine learning is a field of artificial intelligence that focuses on data analysis";
        String essay3 = "football is a popular sport played worldwide";

        detector.addDocument("essay_089.txt", essay1);
        detector.addDocument("essay_092.txt", essay2);

        detector.analyzeDocument("essay_123.txt", essay2);
    }
}
import java.util.*;
import java.io.*;

public class bai4 {
    public static Map<String, Integer> vocab = new HashMap<String, Integer>();
    public static Map<String, Integer> corpus = new HashMap<String, Integer>();
    public static Map<String, Integer> pairCorpus = new HashMap<String, Integer>();
    public static Double[] probs;
    public static Double[][] conditionalProbs;

    public static void readFile() {
        try {
            Vector<String> lines = new Vector<String>();
            File file = new File("c:\\Users\\Admin\\Desktop\\code\\java1\\UIT-ViOCD.txt");
            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                lines.addElement(line);
            }
            fileScanner.close();

            for (String line : lines) {
                // Remove line break, tabs, and handle spaces
                line = line.replace("\n", "").replace("\r", "").replace("\t", "");
                line = line.replaceAll("^\\s+", "");
                line = line.replaceAll("\\s+$", "");
                line = line.toLowerCase();

                // Collecting words
                String[] words = line.split("\\s+");
                for (String word : words) {
                    if (corpus.containsKey(word)) {
                        corpus.put(word, corpus.get(word) + 1);
                    } else {
                        vocab.put(word, vocab.size());
                        corpus.put(word, 1);
                    }
                }

                // Collecting pairs of words
                for (int i = 0; i < words.length - 1; i++) {
                    String words_ij = words[i] + "_" + words[i + 1];
                    if (pairCorpus.containsKey(words_ij)) {
                        pairCorpus.put(words_ij, pairCorpus.get(words_ij) + 1);
                    } else {
                        pairCorpus.put(words_ij, 1);
                    }
                }
            }

            // Filter words with fewer than 5 occurrences
            List<String> wordsToRemove = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : corpus.entrySet()) {
                if (entry.getValue() < 5) {
                    wordsToRemove.add(entry.getKey());
                }
            }
            
            // Remove infrequent words and rebuild vocab
            for (String word : wordsToRemove) {
                corpus.remove(word);
                vocab.remove(word);
            }
            
            // Rebuild vocab indices
            vocab.clear();
            int wordId = 0;
            for (String word : corpus.keySet()) {
                vocab.put(word, wordId++);
            }
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("Khong tim thay file du lieu.");
            fileNotFoundException.printStackTrace();
        }
    }

    public static void constructSingleProb() {
        // Determine the total number of words in the dataset
        int totalWords = 0;
        for (Map.Entry<String, Integer> item : corpus.entrySet()) {
            totalWords += item.getValue();
        }

        // Calculate the probability of each word
        probs = new Double[vocab.size()];
        for (Map.Entry<String, Integer> item : corpus.entrySet()) {
            String word = item.getKey();
            Integer wordCount = corpus.get(word);
            Integer wordId = vocab.get(word);
            
            // P(w) = count(w) / totalWords
            probs[wordId] = (double) wordCount / totalWords;
        }
    }

    public static void constructConditionalProb() {
        // Initialize the conditional probability matrix
        conditionalProbs = new Double[vocab.size()][vocab.size()];
        
        // Calculate conditional probabilities P(w_j | w_i)
        for (Map.Entry<String, Integer> item_i : corpus.entrySet()) {
            String word_i = item_i.getKey();
            Integer wordId_i = vocab.get(word_i);
            Integer count_i = corpus.get(word_i);
            
            for (Map.Entry<String, Integer> item_j : corpus.entrySet()) {
                String word_j = item_j.getKey();
                Integer wordId_j = vocab.get(word_j);
                
                // Get count of pair (word_i, word_j)
                String pairKey = word_i + "_" + word_j;
                int pairCount = pairCorpus.getOrDefault(pairKey, 0);
                
                // P(w_j | w_i) = count(w_i, w_j) / count(w_i)
                double probability = (pairCount > 0) ? (double) pairCount / count_i : 1e-10;
                conditionalProbs[wordId_i][wordId_j] = probability;
            }
        }
    }

    public static void training() {
        constructSingleProb();
        constructConditionalProb();
    }

    public static Vector<String> inferring(String w0) {
        // Check if word exists in vocabulary
        if (!vocab.containsKey(w0)) {
            System.out.println("Từ \"" + w0 + "\" khong co trong bo du lieu hoac xuat hien qua it.");
            return new Vector<String>();
        }
        
        Vector<String> words = new Vector<String>();
        words.add(w0);
        
        String currentWord = w0;
        Integer currentWordIdx = vocab.get(currentWord);
        
        // Generate 4 more words (total 5 including the first one)
        for (int t = 1; t < 5; t++) {
            // Find the next word with highest probability
            String nextWord = "";
            double maxProb = -1;
            
            for (Map.Entry<String, Integer> entry : vocab.entrySet()) {
                String candidateWord = entry.getKey();
                Integer candidateIdx = entry.getValue();
                double prob = conditionalProbs[currentWordIdx][candidateIdx];
                
                if (prob > maxProb) {
                    maxProb = prob;
                    nextWord = candidateWord;
                }
            }
            
            words.add(nextWord);
            currentWord = nextWord;
            currentWordIdx = vocab.get(currentWord);
        }
        
        return words;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Dang xay dung mo hinh tu du lieu cua UIT-ViOCD...");
        readFile();
        System.out.println("Dang huan luyen...");
        training();
        
        System.out.println("Mo hinh da duoc huan luyen voi " + vocab.size() + " tu.");
        System.out.print("Nhap goi y dau tien: ");
        String initialWord = scanner.nextLine().trim().toLowerCase();
        
        Vector<String> generatedSentence = inferring(initialWord);
        
        if (!generatedSentence.isEmpty()) {
            System.out.println("\nCau duoc sinh ra:");
            System.out.println(String.join(" ", generatedSentence));
        }
        
        scanner.close();
    }
}
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.*;

public class MeshExtractor {

    private static Set<String> stopwords = new HashSet<>(Arrays.asList("is", "are", "the", "was", "were", "on", "for", "by", "it"));

   private static Map<String, Set<String>> tokenToMeshKeywords = new HashMap<>();

    public static void main(String[] args) throws Exception {

        CsvParserSettings settings = new CsvParserSettings();
        BeanListProcessor<MeshVocab> rowProcessor = new BeanListProcessor<>(MeshVocab.class);

        settings.setProcessor(rowProcessor);

        settings.setHeaderExtractionEnabled(true);

        CsvParser parser = new CsvParser(settings);

        parser.parse(new InputStreamReader(MeshVocab.class.getResourceAsStream("/MeSH.csv")));

        List<MeshVocab> beans = rowProcessor.getBeans();

        Set<MeshVocab> beansOfInterest = new HashSet<>();

        Set<String> semanticTypes = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(MeshParser.class.getResourceAsStream("selected_semantic_types")))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                semanticTypes.add(line.toLowerCase());
            }
        }

        for (MeshVocab bean : beans) {
            List<String> types = Arrays.asList(bean.getSemanticTypes().split(";"));

            for (String type : types) {
                if (semanticTypes.contains(type.toLowerCase())) {
                    beansOfInterest.add(bean);
                    break;
                }

            }

        }

        beansOfInterest.forEach(bean -> {
                    String preferred = bean.getPreferred().toLowerCase();

                    List<String> synonyms = Arrays.asList(bean.getSynonyms().toLowerCase().split(";"));
                    List<String> parents = Arrays.asList(bean.getParents().toLowerCase().split(";"));
                    Set<String> allTerms = bean.getAllTerms();

                    allTerms.add(preferred);
                    allTerms.addAll(synonyms);
                    allTerms.addAll(parents);
                    bean.setAllTerms(allTerms);
                }

        );


        System.out.println(beansOfInterest.size());
        System.out.println(beans.size());


        CsvParserSettings settings2 = new CsvParserSettings();
        BeanListProcessor<GenderedSentence> rowProcessor2 = new BeanListProcessor<>(GenderedSentence.class);

        settings2.setProcessor(rowProcessor2);

        settings2.setHeaderExtractionEnabled(true);

        CsvParser parser2 = new CsvParser(settings2);

        parser2.parse(new InputStreamReader(MeshVocab.class.getResourceAsStream("/gendered_sentences.csv")));

        List<GenderedSentence> beans2 = rowProcessor2.getBeans();

        CsvWriter csvWriter = new CsvWriter(new FileWriter("/home/ubuntu/IdeaProjects/nlp/src/mesh_extracted.csv"), new CsvWriterSettings());

        csvWriter.writeHeaders("filenamne", "Text", "mesh");

        for (GenderedSentence genderBean : beans2) {
            String text = genderBean.getSentence();

            Set<String> meshKeyWords = getMeshKeyWords(text, beansOfInterest);

            if (!meshKeyWords.isEmpty())

                System.out.println(genderBean + ": " + meshKeyWords);
        }


    }

    private static Set<String> getMeshKeyWords(String text, Set<MeshVocab> beans) {
        System.out.println("Processing: " + text);
        Set<String> keywords = new TreeSet<>();

        Set<String> tokens = new HashSet<>(Arrays.asList(text.replace(".", "").split(" ")));
        tokens.remove("");
        removeStopWords(tokens);

        System.out.println(tokens);

        List<MeshVocab> matchedBeans = new ArrayList<>();
        for (String token : tokens) {
            if(tokenToMeshKeywords.get(token) == null) {
                int max = 0;
                MeshVocab matchedBean = null;

                for (MeshVocab bean : beans) {

                    Set<String> terms = bean.getAllTerms();
                    for (String term : terms) {

                        int lcs = longestCommonSubsequend(term, token);
                        if (lcs > max) {
                            max = lcs;
                            matchedBean = bean;
                        }
                    }
                }

                if (matchedBean != null) {
                    matchedBeans.add(matchedBean);
                    tokenToMeshKeywords.computeIfAbsent(token, k -> new HashSet<>()).add(matchedBean.getPreferred());
                }
            }
        }

        if (!matchedBeans.isEmpty()) {
            matchedBeans.forEach(bean -> keywords.add(bean.getPreferred()));
        }

        return keywords;

    }

    public static int longestCommonSubsequend(String seq1, String seq2) {
        int m = seq1.length();
        int n = seq2.length();
        int[][] matrix = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0 || j == 0) {
                    matrix[i][j] = 0;
                }
                else if (seq1.charAt(i - 1) == seq2.charAt(j - 1)) {
                    matrix[i][j] = matrix[i - 1][j - 1] + 1;
                }
                else {
                    matrix[i][j] = Integer.max(matrix[i - 1][j], matrix[i][j - 1]);
                }
            }
        }
        return matrix[m][n];
    }

    private static void removeStopWords(Set<String> tokens) {
        tokens.removeAll(stopwords);
    }
}

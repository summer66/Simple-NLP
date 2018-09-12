import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.*;

public class MeshExtractor {

	private static Set<String> stopwords = new HashSet<>(
			Arrays.asList("is", "are", "the", "was", "were", "on", "for", "by", "it", "we",
					"he", "she", "as", "and", "thank", "like", "of", "", "to", "dr", "author", "kind", "university", "also", "at", "in", "wish", "thankful", "authors", "his", "her", "she", "he"));

	private static Map<String, Set<String>> tokenToMeshKeywords = new HashMap<>();

	public static void main(String[] args) throws Exception {

//		CsvParserSettings settings = new CsvParserSettings();
//		BeanListProcessor<MeshVocab> rowProcessor = new BeanListProcessor<>(MeshVocab.class);
//
//		settings.setProcessor(rowProcessor);
//
//		settings.setHeaderExtractionEnabled(true);
//
//		CsvParser parser = new CsvParser(settings);
//
//		parser.parse(new FileReader("/home/hao/Documents/Simple-NLP/resources/selected_mesh.csv"));
//
//		List<MeshVocab> beansOfInterest = rowProcessor.getBeans();

		Set<String> keywords = new HashSet<>();

		try(BufferedReader reader = new BufferedReader(new FileReader("/home/hao/Documents/Simple-NLP/resources/candidate_keywords"))) {
			String line = null;
			while( (line = reader.readLine()) != null) {
				keywords.add(line);
			}
		}

		keywords.remove("");

		CsvParserSettings settings2 = new CsvParserSettings();
		BeanListProcessor<GenderedSentence> rowProcessor2 = new BeanListProcessor<>(GenderedSentence.class);

		settings2.setProcessor(rowProcessor2);

		settings2.setHeaderExtractionEnabled(true);

		CsvParser parser2 = new CsvParser(settings2);

		parser2.parse(new FileReader("/home/hao/Documents/Simple-NLP/resources/gendered_sentences.csv"));

		List<GenderedSentence> beans2 = rowProcessor2.getBeans();

		CsvWriter csvWriter = new CsvWriter(new FileWriter("/home/hao/Documents/Simple-NLP/resources/mesh_extracted.csv"), new CsvWriterSettings());

		csvWriter.writeHeaders("filenamne", "Text", "keywords");

		int count = 0;

		for (GenderedSentence genderBean : beans2) {
			String text = genderBean.getSentence();

//			Set<String> meshKeyWords = getMeshKeyWords(text, beansOfInterest);

			Set<String> extractedKeywords = getKeywords(text, keywords);

			if (!extractedKeywords.isEmpty()) {
				count++;
				System.out.println(genderBean.getSentence() + ": " + extractedKeywords);
				csvWriter.writeRow(genderBean.getFilnName(), genderBean.getSentence(), String.join(";", extractedKeywords));
			}
		}

		csvWriter.close();

		System.out.println("Number of sentences: " + beans2.size());

		System.out.println(count);



	}

	private static Set<String> getMeshKeyWords(String text, List<MeshVocab> beans) {
//		System.out.println("Processing: " + text);
		Set<String> keywords = new TreeSet<>();

		Set<String> tokens = new HashSet<>(Arrays.asList(text.toLowerCase().split(" ")));

		removeStopWords(tokens);

//		System.out.println(tokens);

		List<MeshVocab> matchedBeans = new ArrayList<>();
		for (String token : tokens) {
			if (tokenToMeshKeywords.get(token) == null) {
				double max = 0;
				MeshVocab matchedBean = null;

				for (MeshVocab bean : beans) {

					System.out.println("bean all terms: " + bean.getAllTermsStr());

					Set<String> terms = new HashSet<>(Arrays.asList(bean.getAllTermsStr().split(";")));
					for (String term : terms) {

						int lcs = longestCommonSubsequence(term, token);
						double ratio = (double) lcs/token.length();
						if (ratio > max && ratio > 0.2) {
							max = ratio;
							matchedBean = bean;
						}
					}
				}

				if (matchedBean != null) {
					matchedBeans.add(matchedBean);
					tokenToMeshKeywords.computeIfAbsent(token, k -> new HashSet<>()).add(matchedBean.getPreferred());
					System.out.println("MATCHED: " + token + " | " + matchedBean.getPreferred());
				}
			}
		}

		if (!matchedBeans.isEmpty()) {
			matchedBeans.forEach(bean -> keywords.add(bean.getPreferred()));
		}

		return keywords;

	}

	private static Set<String> getKeywords(String text, Set<String> keywords) {

		Set<String> words = new TreeSet<>();

		Set<String> tokens = new HashSet<>(Arrays.asList(text.toLowerCase().split("\\s+")));

		for(String token : tokens) {
			token = token.replaceAll("[.,()]", "");
			if(keywords.contains(token)) {
				words.add(token);
			}
		}

		return words;

	}

	public static int longestCommonSubsequence(String seq1, String seq2) {
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

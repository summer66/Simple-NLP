import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectedMesh {

	public static void main(String[] args) throws Exception {

		CsvParserSettings settings = new CsvParserSettings();
		BeanListProcessor<MeshVocab> rowProcessor = new BeanListProcessor<>(MeshVocab.class);

		settings.setProcessor(rowProcessor);

		settings.setHeaderExtractionEnabled(true);

		CsvParser parser = new CsvParser(settings);

		parser.parse(new FileReader("/home/hao/Documents/Simple-NLP/resources/MeSH.csv"));

		List<MeshVocab> beans = rowProcessor.getBeans();

		Set<MeshVocab> beansOfInterest = new HashSet<>();

		Set<String> semanticTypes = new HashSet<>();

		try (BufferedReader reader = new BufferedReader(new FileReader("/home/hao/Documents/Simple-NLP/resources/selected_semantic_types"))) {

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
					bean.setAllTermsStr(String.join(";", allTerms));
				}

		);

		CsvWriter csvWriter = new CsvWriter(new FileWriter("/home/hao/Documents/Simple-NLP/resources/selected_mesh.csv"), new CsvWriterSettings());

		csvWriter.writeHeaders("Preferred", "Synonyms", "Parents", "Semantic types", "allTerms");

		beansOfInterest.forEach(bean -> {
			csvWriter.writeRow(bean.getPreferred(), bean.getSynonyms(), bean.getParents(), bean.getSemanticTypes(), bean.getAllTermsStr());
		});

		csvWriter.close();

	}
}

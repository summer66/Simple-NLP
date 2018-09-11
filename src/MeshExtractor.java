import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MeshExtractor {

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

            for(String type : types) {
                if (semanticTypes.contains(type.toLowerCase())) {
                    beansOfInterest.add(bean);
                    break;
                }

            }

        }

        beansOfInterest.forEach(bean ->
                System.out.println(bean)
        );


        System.out.println(beansOfInterest.size());
        System.out.println(beans.size());
    }
}

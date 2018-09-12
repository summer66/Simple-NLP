import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MeshParser {


    public static void main(String[] args) throws Exception {

        CsvParserSettings settings = new CsvParserSettings();
        BeanListProcessor<MeshVocab> rowProcessor = new BeanListProcessor<>(MeshVocab.class);

        settings.setProcessor(rowProcessor);

        settings.setHeaderExtractionEnabled(true);

        CsvParser parser = new CsvParser(settings);

        parser.parse(new InputStreamReader(MeshVocab.class.getResourceAsStream("/MeSH.csv")));

        List<MeshVocab> beans = rowProcessor.getBeans();

        Set<String> semanticTypes = new HashSet<>();

        try(BufferedWriter writer = new BufferedWriter(new FileWriter("/home/ubuntu/IdeaProjects/nlp/resources/semantic_types"))) {

            for(MeshVocab bean : beans) {
                String typeStr = bean.getSemanticTypes();
                List<String> types = Arrays.asList(typeStr.split(";")).stream().map(String::toLowerCase).collect(Collectors.toList());

                semanticTypes.addAll(types);
            }

            semanticTypes.forEach(type -> {
                try {
                    writer.write(type);
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        }
    }
}

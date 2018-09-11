import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.InputStreamReader;
import java.util.List;

public class MeshParser {

    public static void main(String[] args) {

        CsvParserSettings settings = new CsvParserSettings();
        BeanListProcessor<MeshVocab> rowProcessor = new BeanListProcessor<>(MeshVocab.class);

        settings.setProcessor(rowProcessor);

        settings.setHeaderExtractionEnabled(true);

        CsvParser parser = new CsvParser(settings);

        parser.parse(new InputStreamReader(MeshVocab.class.getResourceAsStream("/MeSH.csv")));

        List<MeshVocab> beans = rowProcessor.getBeans();

        
    }
}

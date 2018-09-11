import com.univocity.parsers.annotations.Parsed;

public class MeshVocab {

    @Parsed(index = 0)
    private String preferred;

    @Parsed(index = 1)
    private String synonyms;

    @Parsed(index = 2)
    private String parents;

    @Parsed(index = 3)
    private String semanticTypes;

    @Override
    public String toString() {
        return "MeshVocab{" +
                "preferred='" + preferred + '\'' +
                ", synonyms='" + synonyms + '\'' +
                ", parents='" + parents + '\'' +
                ", semanticTypes='" + semanticTypes + '\'' +
                '}';
    }
}

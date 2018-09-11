import com.univocity.parsers.annotations.Parsed;

import java.util.HashSet;
import java.util.Set;

public class MeshVocab {

    @Parsed(index = 0)
    private String preferred;

    @Parsed(index = 1)
    private String synonyms;

    @Parsed(index = 2)
    private String parents;

    @Parsed(index = 3)
    private String semanticTypes;

    private Set<String> allTerms;

    @Override
    public String toString() {
        return "MeshVocab{" +
                "preferred='" + preferred + '\'' +
                ", synonyms='" + synonyms + '\'' +
                ", parents='" + parents + '\'' +
                ", semanticTypes='" + semanticTypes + '\'' +
                '}';
    }

    public String getPreferred() {
        return preferred;
    }

    public void setPreferred(String preferred) {
        this.preferred = preferred;
    }

    public String getSynonyms() {
        if(synonyms == null) {
            synonyms = "";
        }
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }

    public String getParents() {
        if(parents == null) {
            parents = "";
        }
        return parents;
    }

    public void setParents(String parents) {
        this.parents = parents;
    }

    public String getSemanticTypes() {
        return semanticTypes;
    }

    public void setSemanticTypes(String semanticTypes) {
        this.semanticTypes = semanticTypes;
    }

    public Set<String> getAllTerms() {
        if(allTerms == null) {
            allTerms = new HashSet<>();
        }
        return allTerms;
    }

    public void setAllTerms(Set<String> allTerms) {
        this.allTerms = allTerms;
    }
}

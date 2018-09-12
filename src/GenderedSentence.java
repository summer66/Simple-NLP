import com.univocity.parsers.annotations.Parsed;

public class GenderedSentence {

    @Parsed (field = "filename")
    private String filnName;

    @Parsed (field = "Is Female")
    private String female;

    @Parsed (field = "Names")
    private String names;

    @Parsed (field = "Nouns")
    private String nouns;

    @Parsed (field = "Organizations")
    private String orgs;

    @Parsed (field = "Verbs")
    private String verbs;

    @Parsed (field = "Text")
    private String sentence;

    @Override
    public String toString() {
        return "GenderedSentence{" +
                "filnName='" + filnName + '\'' +
                ", sentence='" + sentence + '\'' +
                '}';
    }

    public String getFilnName() {
        return filnName;
    }

    public void setFilnName(String filnName) {
        this.filnName = filnName;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getFemale() {
        return female;
    }

    public void setFemale(String female) {
        this.female = female;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getNouns() {
        return nouns;
    }

    public void setNouns(String nouns) {
        this.nouns = nouns;
    }

    public String getOrgs() {
        return orgs;
    }

    public void setOrgs(String orgs) {
        this.orgs = orgs;
    }

    public String getVerbs() {
        return verbs;
    }

    public void setVerbs(String verbs) {
        this.verbs = verbs;
    }
}

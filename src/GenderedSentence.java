import com.univocity.parsers.annotations.Parsed;

public class GenderedSentence {

    @Parsed (field = "filename")
    private String filnName;

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
}

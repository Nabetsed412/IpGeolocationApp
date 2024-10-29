import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)

// Response para la informacion del pais
class CountryResponse {

    @JsonProperty("name")
    private Name name;  // La clase Name

    @JsonProperty("languages")
    private Map<String, String> languages;

    @JsonProperty("currencies")
    private Map<String, Currency> currencies;

    @JsonProperty("timezones")
    private List<String> timezones;

    @JsonProperty("latlng")
    private List<Double> latlng;


    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public List<String> getTimezones() {
        return timezones;
    }

    public void setTimezones(List<String> timezones) {
        this.timezones = timezones;
    }

    public Map<String, String> getLanguages() {
        return languages;
    }

    public void setLanguages(Map<String, String> languages) {
        this.languages = languages;
    }

    public Map<String, Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Map<String, Currency> currencies) {
        this.currencies = currencies;
    }

    public List<Double> getLatlng() {
        return latlng;
    }

    public void setLatlng(List<Double> latlng) {
        this.latlng = latlng;
    }
}

//nombre
class Name {

    @JsonProperty("common")
    private String common;

    @JsonProperty("official")
    private String official;

    @JsonProperty("nativeName")
    private Map<String, NativeName> nativeName;

    // Getters y setters
    public String getCommon() {
        return common;
    }

    public void setCommon(String common) {
        this.common = common;
    }

    public String getOfficial() {
        return official;
    }

    public void setOfficial(String official) {
        this.official = official;
    }

    public Map<String, NativeName> getNativeName() {
        return nativeName;
    }

    public void setNativeName(Map<String, NativeName> nativeName) {
        this.nativeName = nativeName;
    }
}

//Nombre Nativo
class NativeName {

    @JsonProperty("official")
    private String official;

    @JsonProperty("common")
    private String common;

    // Getters y setters
    public String getOfficial() {
        return official;
    }

    public void setOfficial(String official) {
        this.official = official;
    }

    public String getCommon() {
        return common;
    }

    public void setCommon(String common) {
        this.common = common;
    }
}
//Moneda
class Currency {

    @JsonProperty("name")
    private String name;

    @JsonProperty("symbol")
    private String symbol;

    // Getters y setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}

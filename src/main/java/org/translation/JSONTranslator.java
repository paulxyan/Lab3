package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {
    public static final List<JSONObject> TRANSLATIONS = new ArrayList<>();

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);
            TRANSLATIONS.clear();

            for (int i = 0; i < jsonArray.length(); i++) {
                TRANSLATIONS.add(jsonArray.getJSONObject(i));
            }

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        List<String> lanList = new ArrayList<>(TRANSLATIONS.get(getCountryIndex(country)).keySet());
        lanList.remove("alpha2");
        lanList.remove("alpha3");
        lanList.remove("id");

        return lanList;
    }

    @Override
    public List<String> getCountries() {
        List<String> temp = new ArrayList<>();
        for (JSONObject translation : TRANSLATIONS) {
            temp.add(translation.getString("alpha3"));
        }
        System.out.println("temp size: " + temp);
        return temp;
    }

    @Override
    public String translate(String country, String language) {
        return TRANSLATIONS.get(getCountryIndex(country)).getString(language);
    }

    private int getCountryIndex(String country) {
        CountryCodeConverter ccc = new CountryCodeConverter();
        int countryIndex = 0;
        int i = 0;
        while (i < TRANSLATIONS.size()) {
            JSONObject lanCodes = TRANSLATIONS.get(i);
            if (lanCodes.get("en").equals(ccc.fromCountryCode(country))) {
                countryIndex = i;
                i = TRANSLATIONS.size() + 1;
            }
            i++;
        }
        return countryIndex;
    }
}

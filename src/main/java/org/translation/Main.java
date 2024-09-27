package org.translation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Main class for this program.
 * Complete the code according to the "to do" notes.<br/>
 * The system will:<br/>
 * - prompt the user to pick a country name from a list<br/>
 * - prompt the user to pick the language they want it translated to from a list<br/>
 * - output the translation<br/>
 * - at any time, the user can type quit to quit the program<br/>
 */
public class Main {

    public static final String QUIT = "quit";

    /**
     * This is the main entry point of our Translation System!<br/>
     * A class implementing the Translator interface is created and passed into a call to runProgram.
     * @param args not used by the program
     */
    public static void main(String[] args) {
        Translator translator = new JSONTranslator();
        runProgram(translator);
    }

    /**
     * This is the method which we will use to test your overall program, since
     * it allows us to pass in whatever translator object that we want!
     * See the class Javadoc for a summary of what the program will do.
     * @param translator the Translator implementation to use in the program
     */
    public static void runProgram(Translator translator) {
        CountryCodeConverter ccc = new CountryCodeConverter();
        LanguageCodeConverter lcc = new LanguageCodeConverter();

        while (true) {
            String country = ccc.fromCountry(promptForCountry(translator));

            if (QUIT.equals(country)) {
                break;
            }
            String language = lcc.fromLanguage(promptForLanguage(translator, country));
            if (QUIT.equals(language)) {
                break;
            }
            System.out.println(country + " in " + lcc.fromLanguageCode(language) + " is "
                    + translator.translate(country, language));
            System.out.println("Press enter to continue or quit to exit.");
            Scanner s = new Scanner(System.in);
            String textTyped = s.nextLine();

            if (QUIT.equals(textTyped)) {
                break;
            }
        }
    }

    // Note: CheckStyle is configured so that we don't need javadoc for private methods
    private static String promptForCountry(Translator translator) {
        List<String> countries = translator.getCountries();
        List<String> sortedCountries = new ArrayList<>();
        CountryCodeConverter ccc = new CountryCodeConverter();

        for (String country: countries) {
            sortedCountries.add(ccc.fromCountryCode(country));
        }
        Collections.sort(sortedCountries);

        for (String sortedCountry: sortedCountries) {
            System.out.println(sortedCountry);
        }
        System.out.println("\n" + "select a country from above:");

        Scanner s = new Scanner(System.in);
        return s.nextLine();

    }

    // Note: CheckStyle is configured so that we don't need javadoc for private methods
    private static String promptForLanguage(Translator translator, String country) {
        List<String> languageCode = translator.getCountryLanguages(country);
        List<String> sortedLanguages = new ArrayList<>();
        LanguageCodeConverter lcc = new LanguageCodeConverter();

        for (String language: languageCode) {
            sortedLanguages.add(lcc.fromLanguageCode(language));
        }
        Collections.sort(sortedLanguages);

        for (String sortedLanguage: sortedLanguages) {
            System.out.println(sortedLanguage);
        }

        System.out.println("\n" + "select a language from above:");

        Scanner s = new Scanner(System.in);
        return s.nextLine();
    }
}

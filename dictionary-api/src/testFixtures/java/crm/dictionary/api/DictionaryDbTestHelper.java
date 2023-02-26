package crm.dictionary.api;

public interface DictionaryDbTestHelper {

  Country newCountry();

  Country newCountry(String name);

  Country newCountry(String name, String flagCode, String phoneCode);

  String nextCountryName();
  String nextIndustryName();
}

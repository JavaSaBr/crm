
class EnvConfig {
  static const baseUrl = String.fromEnvironment("base_url");
  static const registrationUrl = String.fromEnvironment("registration_url", defaultValue: "$baseUrl/registration-api");
  static const dictionaryUrl = String.fromEnvironment("dictionary_url", defaultValue: "$baseUrl/dictionary-api");
  static const clientUrl = String.fromEnvironment("client_url", defaultValue: "$baseUrl/client-api");
}
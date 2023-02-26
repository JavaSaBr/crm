export const environment = {
    production: false,
    dictionaryUrl: 'http://localhost:8090/dictionary',
    registrationUrl: 'http://localhost:8090/registration',
    clientUrl: 'http://localhost:8090/client',

    // validation
    orgNameMinLength: 2,
    orgNameMaxLength: 20,
    emailMinLength: 6,
    emailMaxLength: 45,
    phoneNumberMinLength: 6,
    phoneNumberMaxLength: 16,
    otherUserNameMinLength: 2,
    otherUserNameMaxLength: 45,
    passwordMinLength: 6,
    passwordMaxLength: 25,

    // contact
    contactEmailMaxLength: 45,
    contactMessengerMaxLength: 45,
    contactSiteMaxLength: 100,
    contactNameMaxLength: 45,
    contactCompanyMaxLength: 100,
};

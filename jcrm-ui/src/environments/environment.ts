// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

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

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.

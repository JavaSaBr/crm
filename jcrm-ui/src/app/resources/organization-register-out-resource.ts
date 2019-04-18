
export class OrganizationRegisterOutResource {

    orgName: string;
    email: string;
    activationCode: string;
    password: string;
    firstName: string;
    secondName: string;
    phoneNumber: string;
    subscribe: boolean;
    countryId: number;

    constructor(
        orgName: string,
        email: string,
        activationCode: string,
        password: string,
        firstName: string,
        secondName: string,
        phoneNumber: string,
        subscribe: boolean,
        countryId: number
    ) {
        this.orgName = orgName;
        this.countryId = countryId;
        this.firstName = firstName;
        this.secondName = secondName;
        this.email = email;
        this.activationCode = activationCode;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.subscribe = subscribe;
    }
}

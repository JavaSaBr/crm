export class CreateContactOutResource {

    firstName: string;
    secondName: string;
    thirdName: string;

    constructor(fistName: string, secondName: string, thirdName: string) {
        this.firstName = fistName;
        this.secondName = secondName;
        this.thirdName = thirdName;
    }
}

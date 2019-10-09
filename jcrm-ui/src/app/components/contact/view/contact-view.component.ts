import {AfterViewInit, Component, Input} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {TranslateService} from '@ngx-translate/core';
import {Contact} from '@app/entity/contact';
import {ContactRepository} from '@app/repository/contact/contact.repository';
import {Utils} from '@app/util/utils';
import {UserRepository} from '@app/repository/user/user.repository';
import {User} from '@app/entity/user';
import {ErrorService} from '@app/service/error.service';
import {ErrorResponse} from '@app/error/error-response';
import {environment} from '@app/env/environment';

@Component({
    selector: 'app-contact-view',
    templateUrl: './contact-view.component.html',
    styleUrls: ['./contact-view.component.scss'],
})
export class ContactViewComponent implements AfterViewInit {

    readonly contactNameMaxLength = environment.contactNameMaxLength;
    readonly contactCompanyMaxLength = environment.contactCompanyMaxLength;

    readonly contactInfoFormGroup: FormGroup;

    readonly assigner: FormControl;
    readonly curators: FormControl;
    readonly firstName: FormControl;
    readonly secondName: FormControl;
    readonly thirdName: FormControl;
    readonly birthday: FormControl;
    readonly phoneNumbers: FormControl;
    readonly emails: FormControl;
    readonly sites: FormControl;
    readonly messengers: FormControl;
    readonly company: FormControl;

    contact: Contact | null;

    @Input("startEditableState")
    startEditableState: boolean;

    editContactInfo: boolean;

    disabled: boolean;
    hasChangesInContactInfo: boolean;

    constructor(
        formBuilder: FormBuilder,
        private readonly translateService: TranslateService,
        private readonly contactRepository: ContactRepository,
        private readonly errorService: ErrorService,
        private readonly userRepository: UserRepository
    ) {
        this.disabled = false;
        this.hasChangesInContactInfo = false;
        this.contact = null;
        this.startEditableState = false;
        this.editContactInfo = !this.startEditableState;
        this.contactInfoFormGroup = formBuilder.group({
            assigner: ['', [
                Validators.required
            ]],
            curators: [],
            firstName: ['', [
                Validators.required
            ]],
            secondName: ['', [
                Validators.required
            ]],
            thirdName: [''],
            birthday: [],
            phoneNumbers: [],
            emails: [],
            sites: [],
            messengers: [],
            company: [],
        });
        this.contactInfoFormGroup.valueChanges.subscribe(() => {
            this.hasChangesInContactInfo = true;
        });

        const contactInfoControls = this.contactInfoFormGroup.controls;

        this.assigner = contactInfoControls['assigner'] as FormControl;
        this.curators = contactInfoControls['curators'] as FormControl;
        this.firstName = contactInfoControls['firstName'] as FormControl;
        this.secondName = contactInfoControls['secondName'] as FormControl;
        this.thirdName = contactInfoControls['thirdName'] as FormControl;
        this.birthday = contactInfoControls['birthday'] as FormControl;
        this.phoneNumbers = contactInfoControls['phoneNumbers'] as FormControl;
        this.emails = contactInfoControls['emails'] as FormControl;
        this.sites = contactInfoControls['sites'] as FormControl;
        this.messengers = contactInfoControls['messengers'] as FormControl;
        this.company = contactInfoControls['company'] as FormControl;
    }

    reload(contact: Contact | null): void {

        this.contact = Contact.create(contact);
        this.assigner.setValue(null);
        this.curators.setValue([]);
        this.firstName.setValue(Utils.emptyIfNull(this.contact.firstName));
        this.secondName.setValue(Utils.emptyIfNull(this.contact.secondName));
        this.thirdName.setValue(Utils.emptyIfNull(this.contact.thirdName));
        this.birthday.setValue(this.contact.birthday);
        this.phoneNumbers.setValue(Utils.copyArray(this.contact.phoneNumbers));
        this.emails.setValue(Utils.copyArray(this.contact.emails));
        this.sites.setValue(Utils.copyArray(this.contact.sites));
        this.messengers.setValue(Utils.copyArray(this.contact.messengers));
        this.company.setValue(Utils.emptyIfNull(this.contact.company));

        const assignerId = this.contact.assigner;
        const curatorIds = this.contact.curators;

        if (assignerId != null) {
            this.userRepository.findById(assignerId)
                .then(value => this.assigner.setValue(value))
                .then(() => this.hasChangesInContactInfo = false);
        }

        if(curatorIds != null) {
            this.userRepository.findByIds(curatorIds)
                .then(value => this.curators.setValue(value))
                .then(() => this.hasChangesInContactInfo = false);
        }

        this.hasChangesInContactInfo = false;
    }

    ngAfterViewInit(): void {
        setTimeout(() => {

            if (!this.startEditableState) {
                this.switchEditContactInfo(false);
            }

            this.reload(this.contact)
        });
    }

    switchEditContactInfo(canEdit: boolean): void {

        this.editContactInfo = canEdit;

        let formGroup = this.contactInfoFormGroup;
        let controls = formGroup.controls;

        if (!canEdit) {
            Object.keys(controls)
                .forEach(key => controls[key].disable());
        } else {
            Object.keys(controls)
                .forEach(key => controls[key].enable());
        }
    }

    createContact(): void {
        this.disabled = true;
        this.contactRepository.create(this.syncContactWithForm(this.contact))
            .then(result => {
                if (result instanceof ErrorResponse) {
                    this.errorService.showErrorResponse(result);
                } else if (result instanceof Contact) {
                    this.reload(result);
                }
                this.disabled = false;
            });
    }

    updateContact(): void {
        this.disabled = true;
        this.contactRepository.update(this.syncContactWithForm(this.contact))
            .then(result => {
                if (result instanceof ErrorResponse) {
                    this.errorService.showErrorResponse(result);
                } else if (result instanceof Contact) {
                    this.reload(result);
                }
                this.disabled = false;
            });
    }

    syncContactWithForm(contact: Contact): Contact {

        const assigner = this.assigner.value;
        const curators = this.curators.value as User[];

        contact.assigner = assigner instanceof User ? assigner.id : null;
        contact.curators = curators != null && curators.length > 0 ? curators.map(value => value.id) : [];
        contact.firstName = this.firstName.value;
        contact.secondName = this.secondName.value;
        contact.thirdName = this.thirdName.value;
        contact.company = this.company.value;
        contact.birthday = this.birthday.value as Date;
        contact.phoneNumbers = this.phoneNumbers.value;
        contact.emails = this.emails.value;
        contact.sites = this.sites.value;
        contact.messengers = this.messengers.value;

        return contact;
    }
}

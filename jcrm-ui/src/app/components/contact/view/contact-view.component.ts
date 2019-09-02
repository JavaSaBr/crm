import {AfterViewInit, Component, Input} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {UiUtils} from '@app/util/ui-utils';
import {TranslateService} from '@ngx-translate/core';
import {Contact} from '@app/entity/contact';
import {ContactService} from '@app/service/contact.service';

@Component({
    selector: 'app-contact-view',
    templateUrl: './contact-view.component.html',
    styleUrls: ['./contact-view.component.scss'],
})
export class ContactViewComponent implements AfterViewInit {

    readonly rowHeight = UiUtils.FORM_ROW_HEIGHT;
    readonly gutterSize = UiUtils.FORM_GUTTER_SIZE;

    readonly contactInfoFormGroup: FormGroup;

    //readonly assigner: FormControl;
    //readonly curators: FormControl;

    readonly firstName: FormControl;
    readonly secondName: FormControl;
    readonly thirdName: FormControl;
    //readonly birthday: FormControl;

    //readonly phoneNumber: FormControl;
    //readonly email: FormControl;
    //readonly site: FormControl;
    //readonly messenger: FormControl;
    //readonly company: FormControl;

    @Input("contact")
    contact: Contact | null;

    @Input("startEditableState")
    startEditableState: boolean;

    editContactInfo: boolean;

    disabled: boolean;
    hastChangesInContactInfo: boolean;

    constructor(
        formBuilder: FormBuilder,
        private readonly translateService: TranslateService,
        private readonly contactService: ContactService
    ) {
        this.disabled = false;
        this.hastChangesInContactInfo = false;
        this.contact = null;
        this.startEditableState = false;
        this.editContactInfo = !this.startEditableState;
        this.contactInfoFormGroup = formBuilder.group({
            assigner: [''],
            curators: [''],
            firstName: ['', [
                Validators.required
            ]],
            secondName: ['', [
                Validators.required
            ]],
            thirdName: ['', [
                Validators.required
            ]],
            birthday: ['', ],
        });
        this.contactInfoFormGroup.valueChanges.subscribe(() => {
            this.hastChangesInContactInfo = true;
        });

        this.firstName = this.contactInfoFormGroup.controls['firstName'] as FormControl;
        this.secondName = this.contactInfoFormGroup.controls['secondName'] as FormControl;
        this.thirdName = this.contactInfoFormGroup.controls['thirdName'] as FormControl;
    }

    reload(contact: Contact): void {
        this.contact = contact;
        this.firstName.setValue(contact.firstName);
        this.secondName.setValue(contact.secondName);
        this.thirdName.setValue(contact.thirdName);
        this.hastChangesInContactInfo = false;
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

    create(): void {

        this.disabled = true;

        const firstName = this.firstName.value;
        const secondName = this.secondName.value;
        const thirdName = this.thirdName.value;

        this.contactService.create(
            firstName,
            secondName,
            thirdName
        )
            .then(contact => {
                this.reload(contact);
                this.disabled = false;
            })
            .catch(() => this.disabled = false);
    }

    saveContactInfo(): void {

    }
}

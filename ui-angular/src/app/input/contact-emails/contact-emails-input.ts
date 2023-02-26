import {Component, ElementRef} from '@angular/core';
import {FormControl, NgControl, Validators} from '@angular/forms';
import {FocusMonitor} from '@angular/cdk/a11y';
import {MultiFieldsMultiEntityInput} from '@app/input/multi-fields-multi-entity-input';
import {ContactEmail, EmailType} from '@app/entity/contact-email';
import {ContactEmailValidator} from '@app/util/validator/contact-email-validator';
import {TranslateService} from '@ngx-translate/core';
import {environment} from '@app/env/environment';
import {MatFormFieldControl} from '@app/node-modules/@angular/material/form-field';
import {MatSelectChange} from '@app/node-modules/@angular/material/select';

@Component({
    selector: 'contact-emails-input',
    templateUrl: './contact-emails-input.html',
    styleUrls: ['./contact-emails-input.scss'],
    providers: [{provide: MatFormFieldControl, useExisting: ContactEmailsInput}],
    host: {
        '[id]': 'id',
        '[attr.aria-describedby]': 'describedBy',
        '(focusout)': 'onTouched()',
    }
})
export class ContactEmailsInput extends MultiFieldsMultiEntityInput<ContactEmail> {

    readonly maxLength = environment.contactEmailMaxLength;

    readonly availableEmailTypes: EmailType[] = [
        EmailType.HOME,
        EmailType.WORK
    ];

    constructor(
        ngControl: NgControl,
        focusMonitor: FocusMonitor,
        elementRef: ElementRef<HTMLElement>,
        translateService: TranslateService
    ) {
        super(ngControl, focusMonitor, elementRef, translateService);
    }

    get controlType(): string {
        return 'contact-emails-input';
    }

    protected createFormControls(contactEmail: ContactEmail): FormControl[] {

        const emailControl = new FormControl(contactEmail.email, {
            validators: [
                Validators.required,
                ContactEmailValidator.FUN
            ]
        });
        const typeControl = new FormControl(contactEmail.type, {
            validators: [Validators.required]
        });

        emailControl.valueChanges
            .subscribe(value => contactEmail.email = value);
        typeControl.valueChanges
            .subscribe(value => contactEmail.type = value);

        return [
            emailControl,
            typeControl
        ];
    }

    addNew(): void {
        this.addEntity(new ContactEmail(null, EmailType.WORK));
    }

    changeEmailType(contactEmail: ContactEmail, event: MatSelectChange): void {
        contactEmail.type = event.value as EmailType;
    }

    getEmailErrorMessage(control: FormControl): string {
        return ContactEmailValidator.getEmailErrorDescription(control, this.translateService);
    }

    getEmailTypeDescription(emailType: EmailType): string {
        return this.translateService.instant(`ENUM.EMAIL_TYPE.${emailType}`);
    }
}

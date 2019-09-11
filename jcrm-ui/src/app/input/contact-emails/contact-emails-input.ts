import {Component, ElementRef} from '@angular/core';
import {MatFormFieldControl, MatSelectChange} from '@angular/material';
import {FormControl, NgControl, Validators} from '@angular/forms';
import {FocusMonitor} from '@angular/cdk/a11y';
import {MultiFieldsMultiEntityInput} from '@app/input/multi-fields-multi-entity-input';
import {ContactEmail, EmailType} from '@app/entity/contact-email';

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

    readonly availableEmailTypes: EmailType[] = [
        EmailType.HOME,
        EmailType.WORK
    ];

    constructor(
        ngControl: NgControl,
        focusMonitor: FocusMonitor,
        elementRef: ElementRef<HTMLElement>
    ) {
        super(ngControl, focusMonitor, elementRef);
    }

    get controlType(): string {
        return 'contact-emails-input';
    }

    protected createFormControls(entity: ContactEmail): FormControl[] {
        return [
            new FormControl(entity.email, {
                validators: [Validators.required]
            }),
            new FormControl(entity.type, {
                validators: [Validators.required]
            })
        ];
    }

    addNew() {
        this.addEntity(new ContactEmail('', EmailType.WORK));
    }

    changeEmailType(contactEmail: ContactEmail, event: MatSelectChange) {
        contactEmail.type = event.value as EmailType;
    }
}

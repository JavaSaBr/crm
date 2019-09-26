import {Component, ElementRef} from '@angular/core';
import {MatFormFieldControl, MatSelectChange} from '@angular/material';
import {FormControl, NgControl, Validators} from '@angular/forms';
import {FocusMonitor} from '@angular/cdk/a11y';
import {MultiFieldsMultiEntityInput} from '@app/input/multi-fields-multi-entity-input';
import {ContactMessenger, MessengerType} from '@app/entity/contact-messenger';
import {TranslateService} from '@ngx-translate/core';
import {environment} from '@app/env/environment';

@Component({
    selector: 'contact-messengers-input',
    templateUrl: './contact-messengers-input.html',
    styleUrls: ['./contact-messengers-input.scss'],
    providers: [{provide: MatFormFieldControl, useExisting: ContactMessengersInput}],
    host: {
        '[id]': 'id',
        '[attr.aria-describedby]': 'describedBy',
        '(focusout)': 'onTouched()',
    }
})
export class ContactMessengersInput extends MultiFieldsMultiEntityInput<ContactMessenger> {

    readonly availableEmailTypes: MessengerType[] = [
        MessengerType.SKYPE,
        MessengerType.TELEGRAM,
        MessengerType.WHATS_UP,
        MessengerType.VIBER,
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
        return 'contact-messengers-input';
    }

    protected createFormControls(entity: ContactMessenger): FormControl[] {
        return [
            new FormControl(entity.login, {
                validators: [
                    Validators.required,
                    Validators.maxLength(environment.contactMessengerMaxLength)
                ]
            }),
            new FormControl(entity.type, {
                validators: [Validators.required]
            })
        ];
    }

    addNew() {
        this.addEntity(new ContactMessenger('', MessengerType.SKYPE));
    }

    changeEmailType(contactEmail: ContactMessenger, event: MatSelectChange) {
        contactEmail.type = event.value as MessengerType;
    }

    getLoginErrorMessage(control: FormControl) {

        if (control.hasError('required')) {
            return this.translateService.instant('FORMS.ERROR.MESSENGER.LOGIN.REQUIRED');
        } else if (control.hasError('maxlength')) {
            return this.translateService.instant('FORMS.ERROR.MESSENGER.LOGIN.TOO_LONG');
        }

        return null;
    }

    getMessengerTypeDescription(messengerType: MessengerType) {
        return this.translateService.instant(`ENUM.MESSENGER_TYPE.${messengerType}`);
    }
}

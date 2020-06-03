import {Component, ElementRef} from '@angular/core';
import {FormControl, NgControl, Validators} from '@angular/forms';
import {FocusMonitor} from '@angular/cdk/a11y';
import {MultiFieldsMultiEntityInput} from '@app/input/multi-fields-multi-entity-input';
import {ContactMessenger, MessengerType} from '@app/entity/contact-messenger';
import {TranslateService} from '@ngx-translate/core';
import {environment} from '@app/env/environment';
import {MatFormFieldControl} from '@app/node-modules/@angular/material/form-field';
import {MatSelectChange} from '@app/node-modules/@angular/material/select';

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

    readonly maxLength = environment.contactMessengerMaxLength;

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

    protected createFormControls(messenger: ContactMessenger): FormControl[] {

        const loginControl = new FormControl(messenger.login, {
            validators: [
                Validators.required,
                Validators.maxLength(environment.contactMessengerMaxLength)
            ]
        });
        const typeControl = new FormControl(messenger.type, {
            validators: [Validators.required]
        });

        loginControl.valueChanges
            .subscribe(value => messenger.login = value);
        typeControl.valueChanges
            .subscribe(value => messenger.type = value);

        return [
            loginControl,
            typeControl
        ];
    }

    addNew(): void {
        this.addEntity(new ContactMessenger('', MessengerType.SKYPE));
    }

    changeMessengerType(contactMessenger: ContactMessenger, event: MatSelectChange): void {
        contactMessenger.type = event.value as MessengerType;
    }

    getLoginErrorMessage(control: FormControl): string {

        if (control.hasError('required')) {
            return this.translateService.instant('FORMS.ERROR.MESSENGER.LOGIN.REQUIRED');
        } else if (control.hasError('maxlength')) {
            return this.translateService.instant('FORMS.ERROR.MESSENGER.LOGIN.TOO_LONG');
        }

        return null;
    }

    getMessengerTypeDescription(messengerType: MessengerType): string {
        return this.translateService.instant(`ENUM.MESSENGER_TYPE.${messengerType}`);
    }
}

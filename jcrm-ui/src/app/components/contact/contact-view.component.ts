import {Component} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {UiUtils} from '../../utils/ui-utils';
import {TranslateService} from '@ngx-translate/core';

@Component({
    selector: 'app-contact-view',
    templateUrl: './contact-view.component.html',
    styleUrls: ['./contact-view.component.scss'],
})
export class ContactViewComponent {

    readonly rowHeight = UiUtils.FORM_ROW_HEIGHT;
    readonly gutterSize = UiUtils.FORM_GUTTER_SIZE;

    readonly contactInfoFormGroup: FormGroup;

    readonly assigner: FormControl;
    readonly curators: FormControl;

    readonly firstName: FormControl;
    readonly secondName: FormControl;
    readonly thirdName: FormControl;
    readonly birthday: FormControl;

    readonly phoneNumber: FormControl;
    readonly email: FormControl;
    readonly site: FormControl;
    readonly messenger: FormControl;
    readonly company: FormControl;

    disabled: boolean;

    constructor(
        formBuilder: FormBuilder,
        private readonly translateService: TranslateService,
    ) {
        this.contactInfoFormGroup = formBuilder.group({
            assigner: ['', [
                Validators.required
            ]],
            curators: ['', [
                Validators.required
            ]],
            firstName: ['', [
                Validators.required
            ]],
            secondName: ['', [
                Validators.required
            ]],
            thirdName: ['', [
                Validators.required
            ]],
            birthday: ['', [
                Validators.required
            ]],
        });
    }
}

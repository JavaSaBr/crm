import {Component} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {CountryValidator} from '../../input/country/country-validator';
import {UiUtils} from '../../utils/ui-utils';
import {OrganizationValidator} from '../../utils/validator/organization-validator';
import {TranslateService} from '@ngx-translate/core';

@Component({
    selector: 'app-contact-view',
    templateUrl: './contact-view.component.html',
    styleUrls: ['./contact-view.component.css'],
})
export class ContactViewComponent {

    readonly rowHeight = UiUtils.FORM_ROW_HEIGHT;
    readonly rowRadioButtonHeight = UiUtils.FORM_ROW_RADIO_BUTTON_HEIGHT;
    readonly gutterSize = UiUtils.FORM_GUTTER_SIZE;

    readonly orgFormGroup: FormGroup;

    readonly orgName: FormControl;

    disabled: boolean;

    constructor(
        formBuilder: FormBuilder,
        private readonly translateService: TranslateService,
    ) {
        this.orgFormGroup = formBuilder.group({
            orgName: ['', [
                Validators.required
            ], []],
            country: ['', [
                Validators.required,
                CountryValidator.instance
            ]]
        });
        this.orgName = this.orgFormGroup.controls['orgName'] as FormControl;
    }

    getOrgNameErrorMessage() {
        return OrganizationValidator.getNameErrorDescription(this.orgName, this.translateService);
    }
}

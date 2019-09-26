import {Component, ElementRef} from '@angular/core';
import {MatFormFieldControl, MatSelectChange} from '@angular/material';
import {FormControl, NgControl, Validators} from '@angular/forms';
import {FocusMonitor} from '@angular/cdk/a11y';
import {MultiFieldsMultiEntityInput} from '@app/input/multi-fields-multi-entity-input';
import {ContactSite, SiteType} from '@app/entity/contact-site';
import {TranslateService} from '@ngx-translate/core';

@Component({
    selector: 'contact-sites-input',
    templateUrl: './contact-sites-input.html',
    styleUrls: ['./contact-sites-input.scss'],
    providers: [{provide: MatFormFieldControl, useExisting: ContactSitesInput}],
    host: {
        '[id]': 'id',
        '[attr.aria-describedby]': 'describedBy',
        '(focusout)': 'onTouched()',
    }
})
export class ContactSitesInput extends MultiFieldsMultiEntityInput<ContactSite> {

    readonly availableSiteTypes: SiteType[] = [
        SiteType.HOME,
        SiteType.WORK
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
        return 'contact-sites-input';
    }

    protected createFormControls(entity: ContactSite): FormControl[] {
        return [
            new FormControl(entity.url, {
                validators: [Validators.required]
            }),
            new FormControl(entity.type, {
                validators: [Validators.required]
            })
        ];
    }

    addNew() {
        this.addEntity(new ContactSite('', SiteType.WORK));
    }

    changeEmailType(contactEmail: ContactSite, event: MatSelectChange) {
        contactEmail.type = event.value as SiteType;
    }
}

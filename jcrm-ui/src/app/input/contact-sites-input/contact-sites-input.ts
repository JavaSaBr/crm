import {Component, ElementRef} from '@angular/core';
import {MatFormFieldControl, MatSelectChange} from '@angular/material';
import {FormControl, NgControl, Validators} from '@angular/forms';
import {FocusMonitor} from '@angular/cdk/a11y';
import {MultiFieldsMultiEntityInput} from '@app/input/multi-fields-multi-entity-input';
import {ContactSite, SiteType} from '@app/entity/contact-site';
import {TranslateService} from '@ngx-translate/core';
import {environment} from '@app/env/environment';

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

    readonly maxLength = environment.contactSiteMaxLength;

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

    protected createFormControls(site: ContactSite): FormControl[] {

        const urlControl = new FormControl(site.url, {
            validators: [
                Validators.required,
                Validators.maxLength(environment.contactSiteMaxLength)
            ]
        });
        const typeControl = new FormControl(site.type, {
            validators: [Validators.required]
        });

        urlControl.valueChanges
            .subscribe(value => site.url = value);
        typeControl.valueChanges
            .subscribe(value => site.type = value);

        return [
            urlControl,
            typeControl
        ];
    }

    addNew():void {
        this.addEntity(new ContactSite('', SiteType.WORK));
    }

    changeSiteType(contactSite: ContactSite, event: MatSelectChange):void {
        contactSite.type = event.value as SiteType;
    }

    getSiteErrorMessage(control: FormControl): string {

        if (control.hasError('required')) {
            return this.translateService.instant('FORMS.ERROR.SITE.URL.REQUIRED');
        } else if (control.hasError('maxlength')) {
            return this.translateService.instant('FORMS.ERROR.SITE.URL.TOO_LONG');
        }

        return null;
    }

    getSiteTypeDescription(siteType: SiteType): string {
        return this.translateService.instant(`ENUM.SITE_TYPE.${siteType}`);
    }
}

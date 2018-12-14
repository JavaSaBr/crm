import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Observable} from 'rxjs';
import {map, startWith} from 'rxjs/operators';

@Component({
    selector: 'app-register-new-organization',
    templateUrl: './register-new-organization.component.html',
    styleUrls: ['./register-new-organization.component.scss']
})
export class RegisterNewOrganizationComponent implements OnInit {

    orgFormGroup: FormGroup;

    ownerFormGroup: FormGroup;
    options: string[] = ['Belarus', 'Russia', 'US'];
    filteredOptions: Observable<string[]>;

    constructor(private formBuilder: FormBuilder) {
        this.orgFormGroup = this.formBuilder.group({
            orgName: ['', Validators.required],
            country: ['', Validators.required]
        });
        this.ownerFormGroup = this.formBuilder.group({
            firstName: ['', Validators.required],
            secondName: ['', Validators.required],
            thirdName: [''],
            email: ['', [
                Validators.required,
                Validators.email
            ]],
            phoneNumber: ['', [
                Validators.required,
                Validators.pattern('[0-9]{5}[-][0-9]{7}[-][0-9]{1}')
            ]]
        });
    }

    ngOnInit() {
        this.filteredOptions = this.orgFormGroup.controls['country'].valueChanges
            .pipe(
                startWith(''),
                map(value => this._filter(value))
            );
    }

    private _filter(value: string): string[] {
        const filterValue = value.toLowerCase();
        return this.options.filter(option => option.toLowerCase().includes(filterValue));
    }
}

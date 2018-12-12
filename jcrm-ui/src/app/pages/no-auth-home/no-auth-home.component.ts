import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
    selector: 'app-no-auth-home',
    templateUrl: './no-auth-home.component.html',
    styleUrls: ['./no-auth-home.component.scss']
})
export class NoAuthHomeComponent implements OnInit {

    private currentSubPage: string = null;


    firstFormGroup: FormGroup;
    secondFormGroup: FormGroup;

    constructor(private _formBuilder: FormBuilder) {
        this.firstFormGroup = this._formBuilder.group({
            firstCtrl: ['', Validators.required]
        });
        this.secondFormGroup = this._formBuilder.group({
            secondCtrl: ['', Validators.required]
        });
    }

    ngOnInit() {
    }

    hasSubPage() {
        return this.currentSubPage != null;
    }

    activateSubPage(page: string) {
        this.currentSubPage = page;
    }

    getCurrentSubPage() {
        return this.currentSubPage;
    }
}

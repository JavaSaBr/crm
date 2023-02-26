import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import {RegisterNewOrganizationComponent} from './register-new-organization.component';

describe('RegisterNewOrganizationComponent', () => {
    let component: RegisterNewOrganizationComponent;
    let fixture: ComponentFixture<RegisterNewOrganizationComponent>;

    beforeEach(waitForAsync(() => {
        TestBed.configureTestingModule({
            declarations: [RegisterNewOrganizationComponent]
        }).compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(RegisterNewOrganizationComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});

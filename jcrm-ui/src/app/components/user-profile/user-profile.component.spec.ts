import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import {UserProfileComponent} from './user-profile.component';

describe('RootComponent', () => {
    let component: UserProfileComponent;
    let fixture: ComponentFixture<UserProfileComponent>;

    beforeEach(waitForAsync(() => {
        TestBed.configureTestingModule({declarations: [UserProfileComponent]})
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(UserProfileComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});

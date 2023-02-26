import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import {UserWorkspaceComponent} from './user-workspace.component';

describe('RootComponent', () => {
    let component: UserWorkspaceComponent;
    let fixture: ComponentFixture<UserWorkspaceComponent>;

    beforeEach(waitForAsync(() => {
        TestBed.configureTestingModule({
            declarations: [UserWorkspaceComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(UserWorkspaceComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});

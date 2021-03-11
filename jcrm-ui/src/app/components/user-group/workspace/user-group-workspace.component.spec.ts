import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import {UserGroupWorkspaceComponent} from './user-group-workspace.component';

describe('RootComponent', () => {
    let component: UserGroupWorkspaceComponent;
    let fixture: ComponentFixture<UserGroupWorkspaceComponent>;

    beforeEach(waitForAsync(() => {
        TestBed.configureTestingModule({
            declarations: [UserGroupWorkspaceComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(UserGroupWorkspaceComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});

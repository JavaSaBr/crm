import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {UserWorkspaceComponent} from './user-workspace.component';

describe('RootComponent', () => {
    let component: UserWorkspaceComponent;
    let fixture: ComponentFixture<UserWorkspaceComponent>;

    beforeEach(async(() => {
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

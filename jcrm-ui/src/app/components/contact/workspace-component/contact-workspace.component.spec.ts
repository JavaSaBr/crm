import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ContactWorkspaceComponent} from './contact-workspace.component';

describe('RootComponent', () => {
    let component: ContactWorkspaceComponent;
    let fixture: ComponentFixture<ContactWorkspaceComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [ContactWorkspaceComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(ContactWorkspaceComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});

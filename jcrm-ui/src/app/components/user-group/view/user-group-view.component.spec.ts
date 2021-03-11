import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import {UserGroupViewComponent} from './user-group-view.component';

describe('RootComponent', () => {

    let component: UserGroupViewComponent;
    let fixture: ComponentFixture<UserGroupViewComponent>;

    beforeEach(waitForAsync(() => {

        TestBed
            .configureTestingModule({
                declarations: [UserGroupViewComponent]
            })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(UserGroupViewComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});

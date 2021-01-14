import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import {ContactViewComponent} from './contact-view.component';

describe('RootComponent', () => {
    let component: ContactViewComponent;
    let fixture: ComponentFixture<ContactViewComponent>;

    beforeEach(waitForAsync(() => {
        TestBed.configureTestingModule({
            declarations: [ContactViewComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(ContactViewComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});

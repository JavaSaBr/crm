import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {FabButtonComponent} from './fab-button.component';

describe('FabButtonComponent', () => {
    let component: FabButtonComponent;
    let fixture: ComponentFixture<FabButtonComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [FabButtonComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(FabButtonComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});

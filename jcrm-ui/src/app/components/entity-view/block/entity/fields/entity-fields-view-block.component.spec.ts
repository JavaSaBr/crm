import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {EntityFieldsViewBlockComponent} from './entity-fields-vew-block.component';

describe('RootComponent', () => {
    let component: EntityFieldsViewBlockComponent;
    let fixture: ComponentFixture<EntityFieldsViewBlockComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [EntityFieldsViewBlockComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(EntityFieldsViewBlockComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});

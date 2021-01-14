import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import {EntityFieldsViewBlockComponent} from './entity-fields-vew-block.component';

describe('EntityFieldsViewBlockComponent', () => {

    let component: EntityFieldsViewBlockComponent;
    let fixture: ComponentFixture<EntityFieldsViewBlockComponent>;

    beforeEach(waitForAsync(() => {
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

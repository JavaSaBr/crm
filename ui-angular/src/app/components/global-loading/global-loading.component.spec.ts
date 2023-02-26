import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import {GlobalLoadingComponent} from './global-loading.component';

describe('RootComponent', () => {
    let component: GlobalLoadingComponent;
    let fixture: ComponentFixture<GlobalLoadingComponent>;

    beforeEach(waitForAsync(() => {
        TestBed.configureTestingModule({
            declarations: [GlobalLoadingComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(GlobalLoadingComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});

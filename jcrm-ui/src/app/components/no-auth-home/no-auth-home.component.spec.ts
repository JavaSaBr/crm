import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import {NoAuthHomeComponent} from './no-auth-home.component';

describe('NoAuthHomeComponent', () => {

    let component: NoAuthHomeComponent;
    let fixture: ComponentFixture<NoAuthHomeComponent>;

    beforeEach(waitForAsync(() => {

        let bedStatic = TestBed.configureTestingModule({
            declarations: [NoAuthHomeComponent]
        });

        bedStatic.compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(NoAuthHomeComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});

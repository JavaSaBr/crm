import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BlankCustomerRegisterComponent } from './blank-customer-register.component';

describe('BlankCustomerRegisterComponent', () => {
  let component: BlankCustomerRegisterComponent;
  let fixture: ComponentFixture<BlankCustomerRegisterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BlankCustomerRegisterComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BlankCustomerRegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});

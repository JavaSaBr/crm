import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PhoneNumberControl } from './phone-number.control';

describe('PhoneNumberControl', () => {
  let component: PhoneNumberControl;
  let fixture: ComponentFixture<PhoneNumberControl>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PhoneNumberControl ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PhoneNumberControl);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

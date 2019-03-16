import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PhoneNumberInput } from './phone-number-input.component';

describe('PhoneNumberInput', () => {
  let component: PhoneNumberInput;
  let fixture: ComponentFixture<PhoneNumberInput>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PhoneNumberInput ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PhoneNumberInput);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

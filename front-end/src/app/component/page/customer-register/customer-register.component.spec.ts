import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {CustomerRegisterComponent} from './customer-register.component';

describe('CustomerRegisterComponent', () => {
  let component: CustomerRegisterComponent;
  let fixture: ComponentFixture<CustomerRegisterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CustomerRegisterComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomerRegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

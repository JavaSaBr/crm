import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { WorkspaceNavBarComponent } from './workspace-nav-bar.component';

describe('WorkspaceNavBarComponent', () => {
  let component: WorkspaceNavBarComponent;
  let fixture: ComponentFixture<WorkspaceNavBarComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ WorkspaceNavBarComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkspaceNavBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

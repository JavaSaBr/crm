import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { WorkspaceSideNavComponent } from './workspace-side-nav.component';

describe('WorkspaceSideNavComponent', () => {
  let component: WorkspaceSideNavComponent;
  let fixture: ComponentFixture<WorkspaceSideNavComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ WorkspaceSideNavComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkspaceSideNavComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

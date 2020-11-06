import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ResetDetailsComponent } from './reset-details.component';

describe('ResetDetailsComponent', () => {
  let component: ResetDetailsComponent;
  let fixture: ComponentFixture<ResetDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ResetDetailsComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ResetDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

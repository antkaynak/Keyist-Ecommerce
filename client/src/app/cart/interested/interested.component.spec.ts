import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InterestedComponent } from './interested.component';

describe('InterestedComponent', () => {
  let component: InterestedComponent;
  let fixture: ComponentFixture<InterestedComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [InterestedComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InterestedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

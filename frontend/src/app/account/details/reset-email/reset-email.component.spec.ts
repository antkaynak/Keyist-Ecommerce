import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ResetEmailComponent} from './reset-email.component';

describe('ResetEmailComponent', () => {
  let component: ResetEmailComponent;
  let fixture: ComponentFixture<ResetEmailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ResetEmailComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ResetEmailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {EmailResetVerificationComponent} from './email-reset-verification.component';

describe('EmailResetVerificationComponent', () => {
  let component: EmailResetVerificationComponent;
  let fixture: ComponentFixture<EmailResetVerificationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [EmailResetVerificationComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EmailResetVerificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PasswordForgotVerificationComponent } from './password-forgot-verification.component';

describe('PasswordForgotVerificationComponent', () => {
  let component: PasswordForgotVerificationComponent;
  let fixture: ComponentFixture<PasswordForgotVerificationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [PasswordForgotVerificationComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PasswordForgotVerificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

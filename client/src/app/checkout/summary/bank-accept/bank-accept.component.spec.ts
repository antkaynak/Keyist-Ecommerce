import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BankAcceptComponent } from './bank-accept.component';

describe('BankAcceptComponent', () => {
  let component: BankAcceptComponent;
  let fixture: ComponentFixture<BankAcceptComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [BankAcceptComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BankAcceptComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

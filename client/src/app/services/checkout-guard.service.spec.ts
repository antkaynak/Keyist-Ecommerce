import { inject, TestBed } from '@angular/core/testing';

import { CheckoutGuardService } from './checkout-guard.service';

describe('CheckoutGuardService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CheckoutGuardService]
    });
  });

  it('should be created', inject([CheckoutGuardService], (service: CheckoutGuardService) => {
    expect(service).toBeTruthy();
  }));
});

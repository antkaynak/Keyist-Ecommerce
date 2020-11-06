import { OrderState } from './../../store/order/order.reducer';
import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import * as fromApp from '../../store/app.reducers';

@Component({
  selector: 'app-progress-bar',
  templateUrl: './progress-bar.component.html',
  styleUrls: ['./progress-bar.component.scss']
})
export class ProgressBarComponent implements OnInit {

  activeStep = 0;
  activeValue = 25;
  orderState: Observable<OrderState>;

  constructor(private store: Store<fromApp.AppState>) {
  }

  ngOnInit() {
    this.store.select('order').subscribe((orderState: OrderState) => {
      this.activeStep = orderState.checkoutStep;
      if (orderState.checkoutStep === 0) {
        this.activeValue = 25;
      } else if (orderState.checkoutStep === 1) {
        this.activeValue = 50;
      } else if (orderState.checkoutStep === 2) {
        this.activeValue = 75;
      } else if (orderState.checkoutStep === 3) {
        this.activeValue = 100;
      }
    });
  }

}

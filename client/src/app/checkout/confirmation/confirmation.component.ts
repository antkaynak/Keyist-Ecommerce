import { CartState } from './../../store/cart/cart.reducer';
import { OrderState } from './../../store/order/order.reducer';
import * as OrderActions from '../../store/order/order.actions';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import * as fromApp from '../../store/app.reducers';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { take } from 'rxjs/operators';

@Component({
  selector: 'app-confirmation',
  templateUrl: './confirmation.component.html',
  styleUrls: ['./confirmation.component.scss']
})
export class ConfirmationComponent implements OnInit {

  cartState: Observable<CartState>;

  orderState: Observable<OrderState>;

  innerLoading = false;


  constructor(private store: Store<fromApp.AppState>, private router: Router) {
  }

  ngOnInit() {
    this.store.dispatch(new OrderActions.SetCheckoutStep(3));
    this.cartState = this.store.select('cart');
    this.cartState.pipe(take(1)).subscribe((cartState: CartState) => {
      if (cartState == null || cartState.cart == null) {
        this.router.navigate(['/cart']);
      }
    });

    this.orderState = this.store.select('order');
    this.orderState.pipe(take(1)).subscribe((orderState: OrderState) => {

      if (!orderState || !orderState.payment || !orderState.personal || !orderState.shipping) {
        this.router.navigate(['/cart']);
      }
    });
  }
}

import { CartState } from './../store/cart/cart.reducer';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import * as fromApp from '../store/app.reducers';
import { Store } from '@ngrx/store';
import * as CartActions from '../store/cart/cart.actions';
import * as OrderActions from '../store/order/order.actions';
import { take } from 'rxjs/operators';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit, OnDestroy {

  cartState: Observable<CartState>;
  cartItemCountSubscription: Subscription;
  showDiscountInput = false;

  applyCodeShow = false;
  cartItemCount = 0;

  constructor(private store: Store<fromApp.AppState>, private router: Router, private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.cartState = this.store.select('cart');
    this.cartItemCountSubscription = this.cartState.subscribe(data => {
      if (data.cart && data.cart.cartItems.length) {
        this.cartItemCount = data.cart.cartItems.reduce((total, cartItem) => total + cartItem.amount, 0);
      } else {
        this.cartItemCount = 0;
      }
    });
  }

  ngOnDestroy() {
    if (this.cartItemCountSubscription) {
      this.cartItemCountSubscription.unsubscribe();
    }
  }

  applyCode(discountCodeField: HTMLInputElement) {
    const discountCode = discountCodeField.value;
    this.store.dispatch(new CartActions.ApplyDiscount(discountCode));

  }

  goToItem(productUrl) {
    this.router.navigate(['/detail/', productUrl], { relativeTo: this.route });
  }

  removeFromCart(id: number) {
    this.store.dispatch(new CartActions.RemoveFromCart(id));
  }

  amountIncrement(id: number) {
    this.store.dispatch(new CartActions.IncrementCart({ id, amount: '1' }));
  }

  amountDecrement(id: number) {
    this.store.dispatch(new CartActions.DecrementCart({ id, amount: '1' }));
  }

  activatePurchase() {
    this.store.select('auth')
      .pipe(take(1))
      .subscribe(data => {
        if (data.isActive) {
          this.store.dispatch(new OrderActions.IsCheckoutActive(true));
          this.router.navigate(['/checkout/personal'], { relativeTo: this.route });
        } else {
          alert('Your account is inactive. You must activate your account in order to purchase.\nPlease check your email.');
        }
      });
  }
}

import { AuthState } from './../store/auth/auth.reducer';
import { CartState } from './../store/cart/cart.reducer';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import * as fromApp from '../store/app.reducers';
import * as CartActions from '../store/cart/cart.actions';
import * as OrderActions from '../store/order/order.actions';
import * as AuthActions from '../store/auth/auth.actions';
import { Observable, Subscription } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbDropdownConfig } from '@ng-bootstrap/ng-bootstrap';
import { take } from 'rxjs/operators';


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  providers: [NgbDropdownConfig]
})
export class HeaderComponent implements OnInit, OnDestroy {

  cartState: Observable<CartState>;
  authState: Observable<AuthState>;

  cartItemCountSubscription: Subscription;
  cartItemCount = 0;
  isCollapsed = true;

  authStateSubscription: Subscription;

  constructor(
    private store: Store<fromApp.AppState>,
    private router: Router,
    private route: ActivatedRoute,
    public dropdownConfig: NgbDropdownConfig) {
    dropdownConfig.placement = 'bottom-right';
  }

  ngOnInit() {

    this.authState = this.store.select('auth');
    this.cartState = this.store.select('cart');

    this.authStateSubscription = this.authState
      .subscribe((data) => {
        if (data.authenticated) {
          this.store.dispatch(new CartActions.FetchCart());
          this.cartItemCountSubscription = this.cartState.subscribe(data => {
            if (data.cart && data.cart.cartItems.length) {
              this.cartItemCount = data.cart.cartItems.reduce((total, cartItem) => total + cartItem.amount, 0);
            } else {
              this.cartItemCount = 0;
            }
          });
        }
        else {
          if (this.cartItemCountSubscription) {
            this.cartItemCountSubscription.unsubscribe();
          }
        }
      }
      );

  }

  ngOnDestroy() {
    if (this.authStateSubscription) {
      this.authStateSubscription.unsubscribe();
    }
    if (this.cartItemCountSubscription) {
      this.cartItemCountSubscription.unsubscribe();
    }
  }


  userSignOut() {
    this.store.dispatch(new AuthActions.SignOut());
    this.router.navigate(['/']);
  }


  searchProduct(search: HTMLInputElement) {
    if (search.value.trim().length) {
      const url = '/search/' + search.value;
      this.router.navigate([url]);
    }
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

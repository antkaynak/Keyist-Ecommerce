import { CartState } from './../store/cart/cart.reducer';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, CanActivateChild, CanDeactivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import * as fromApp from '../store/app.reducers';
import { Store } from '@ngrx/store';
import { forkJoin, Observable } from 'rxjs';
import * as fromOrder from '../store/order/order.reducer';
import * as fromCart from '../store/cart/cart.reducer';
import * as OrderActions from '../store/order/order.actions';
import { CheckoutComponent } from '../checkout/checkout.component';
import { map, take } from 'rxjs/operators';

@Injectable()
export class CheckoutGuardService implements CanActivate, CanActivateChild, CanDeactivate<CheckoutComponent> {
  constructor(private store: Store<fromApp.AppState>, private router: Router) {
  }

  canCheckoutOrder() {
    return this.store.select('order')
      .pipe(take(1),
        map((orderState: fromOrder.OrderState) => orderState.isCheckoutActive));
  }

  canCheckoutCart() {
    return this.store.select('cart')
      .pipe(take(1),
        map((cartState: fromCart.CartState) => cartState.cart && cartState.cart.cartItems.length !== 0));
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
    return forkJoin([this.canCheckoutOrder(), this.canCheckoutCart()]).pipe(
      map(([a, b]) => {
        if (a && b) {
          return true;
        }
        this.router.navigate(['/']);
        return false;
      })
    );
  }

  canActivateChild(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    return forkJoin([this.canCheckoutOrder(), this.canCheckoutCart()]).pipe(
      map(([a, b]) => a && b)
    );
  }

  canDeactivate(component: CheckoutComponent, currentRoute: ActivatedRouteSnapshot, currentState: RouterStateSnapshot, nextState?: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    this.store.dispatch(new OrderActions.IsCheckoutActive(false));
    return true;
  }


}

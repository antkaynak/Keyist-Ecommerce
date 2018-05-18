import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivateChild, CanDeactivate, Router, RouterStateSnapshot} from "@angular/router";
import * as fromApp from "../store/app.reducers";
import {Store} from "@ngrx/store";
import {Observable} from "rxjs/Observable";
import * as fromOrder from "../store/order/order.reducer";
import * as OrderActions from "../store/order/order.actions";
import {CheckoutComponent} from "../checkout/checkout.component";

@Injectable()
export class CheckoutGuardService implements CanActivateChild, CanDeactivate<CheckoutComponent> {
  constructor(private store: Store<fromApp.AppState>, private router: Router) {
  }

  canActivateChild(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    if (route.url.length == 0) {
      return true; //ignore checking active if it is the default child
    }
    return this.store.select('order')
      .map((orderState: fromOrder.State) => {
        if (!orderState.isPurchaseActive) {
          this.router.navigate(["/"]);
        }
        return orderState.isPurchaseActive;
      });
  }

  canDeactivate(component: CheckoutComponent, currentRoute: ActivatedRouteSnapshot, currentState: RouterStateSnapshot, nextState?: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    this.store.dispatch(new OrderActions.IsPurchaseActive(false));
    return true;
  }


}

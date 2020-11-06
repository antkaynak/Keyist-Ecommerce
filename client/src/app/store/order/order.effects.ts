import { Checkout } from './../model';
import { Injectable } from '@angular/core';
import { Actions, Effect, ofType } from '@ngrx/effects';
import * as OrderActions from './order.actions';
import * as CartActions from '../cart/cart.actions';
import { OrderService } from '../../services/order.service';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';


@Injectable()
export class OrderEffects {

  @Effect()
  postOrder = this.actions$
    .pipe(ofType(OrderActions.POST_ORDER),
      map((action: OrderActions.PostOrder) => {
        return action.payload;
      }),
      switchMap((data: Checkout) => {
        return this.orderService.postOrder(data)
          .pipe(switchMap(res => {
            this.router.navigate(['/checkout/success']);
            return [
              { type: OrderActions.EMPTY_ORDER },
              { type: CartActions.EMPTY_CART_SUCCESS }];
          }), catchError(error => {
            return of(
              new OrderActions.OrderError(
                { error, errorEffect: OrderActions.POST_ORDER }));
          }));
      }));


  constructor(private actions$: Actions, private orderService: OrderService, private router: Router) {
  }
}

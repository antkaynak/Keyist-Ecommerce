import {Injectable} from '@angular/core';
import {Actions, Effect, ofType} from '@ngrx/effects';
import * as OrderActions from "./order.actions";
import * as CartActions from "../cart/cart.actions";
import {OrderService} from "../../services/order.service";
import {PostOrdersObject} from "./order.reducer";
import {Router} from "@angular/router";
import {of} from "rxjs";
import {catchError, map, switchMap} from "rxjs/operators";


@Injectable()
export class OrderEffects { //TODO ERROR HANDLING!!


  // @Effect() login$ = this.actions$
  //   .ofType(OrderActions.FETCH_ALL_ORDERS)
  //   .switchMap((action: OrderActions.FetchAllOrders) => {
  //       console.log('FETCH_ALL_ORDERS EFFECT');
  //       return this.orderService.getAllOrders()
  //         .map(res => ({type: OrderActions.FETCH_ALL_ORDERS_SUCCESS, payload: res}))
  //     });

  @Effect()
  postOrder = this.actions$
    .pipe(ofType(OrderActions.POST_ORDER),
      map((action: OrderActions.PostOrder) => {
        return action.payload
      }),
      switchMap((data: PostOrdersObject) => {
        return this.orderService.postOrder(data)
          .pipe(switchMap(res => {
            this.router.navigate(["/checkout/success"]);
            return [
              {type: OrderActions.EMPTY_ORDER},
              {type: CartActions.EMPTY_CART}]
          }), catchError(error => {
          return of(
            new OrderActions.OrderError(
              {error: error, errorEffect: OrderActions.POST_ORDER}));
        }));
      }));


  constructor(private actions$: Actions, private orderService: OrderService, private router: Router) {
  }
}

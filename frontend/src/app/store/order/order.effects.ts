import {Injectable} from '@angular/core';
import {Actions, Effect} from '@ngrx/effects';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/mergeMap';

import * as OrderActions from "./order.actions";
import * as CartActions from "../cart/cart.actions";
import {OrderService} from "../../services/order.service";
import {PostOrdersObject} from "./order.reducer";
import {Router} from "@angular/router";
import {Observable} from "rxjs/Observable";


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
    .ofType(OrderActions.POST_ORDER)
    .map((action: OrderActions.PostOrder) => {
      console.log('POST_ORDER EFFECT');
      return action.payload
    })
    .switchMap((data: PostOrdersObject) => {
      return this.orderService.postOrder(data)
        .switchMap(res => {
          this.router.navigate(["/checkout/success"]);
          return [
            {type: OrderActions.EMPTY_ORDER},
            {type: CartActions.EMPTY_CART}]
        }).catch(error => {
          return Observable.of(
            new OrderActions.OrderError(
              {error: error, errorEffect: OrderActions.POST_ORDER}));
        })
    });


  constructor(private actions$: Actions, private orderService: OrderService, private router: Router) {
  }
}

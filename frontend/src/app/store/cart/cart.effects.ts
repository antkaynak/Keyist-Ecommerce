import {Injectable} from '@angular/core';
import {Actions, Effect} from '@ngrx/effects';
import {Router} from '@angular/router';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/mergeMap';
import 'rxjs/add/observable/throw';
import 'rxjs/add/observable/of';

import * as CartActions from "./cart.actions";
import {Observable} from "rxjs/Observable";
import {CartService} from "../../services/cart.service";


@Injectable()
export class CartEffects {


  @Effect()
  fetchCart = this.actions$
    .ofType(CartActions.FETCH_CART)
    .switchMap((action: CartActions.FetchCart) => {
      return this.cartService.getCart()
        .map(res => {
          return {type: CartActions.FETCH_CART_SUCCESS, payload: {cart: res, effect: CartActions.FETCH_CART}}
        })
        .catch(error => {
          return Observable.of(
            new CartActions.CartError(
              {error: error, errorEffect: CartActions.FETCH_CART}));
        })
    });


  @Effect()
  addToCart = this.actions$
    .ofType(CartActions.ADD_TO_CART)
    .map((action: CartActions.AddToCart) => {
      return action.payload
    })
    .switchMap((payload: { id: number, amount: string }) => {
      return this.cartService.postCart(payload.id, payload.amount)
        .map(res => {
          this.router.navigate(["/checkout"]);
          return {type: CartActions.SET_CART, payload: {cart: res, effect: CartActions.ADD_TO_CART}}
        }).catch(error => {
          return Observable.of(
            new CartActions.CartError(
              {error: error, errorEffect: CartActions.ADD_TO_CART}));
        })
    });


  @Effect()
  removeFromCart = this.actions$
    .ofType(CartActions.REMOVE_FROM_CART)
    .map((action: CartActions.RemoveFromCart) => {
      return action.payload
    })
    .switchMap((id: number) => {
      return this.cartService.removeFromCart(id)
        .map(res => ({type: CartActions.SET_CART, payload: {cart: res, effect: CartActions.REMOVE_FROM_CART}}))
        .catch(error => {
          return Observable.of(
            new CartActions.CartError(
              {error: error, errorEffect: CartActions.REMOVE_FROM_CART}));
        })
    });

  @Effect()
  applyDiscountCode = this.actions$
    .ofType(CartActions.APPLY_DISCOUNT)
    .map((action: CartActions.ApplyDiscount) => {
      return action.payload
    })
    .switchMap((code: string) => {
      return this.cartService.applyDiscount(code)
        .map(res => ({type: CartActions.SET_CART, payload: {cart: res, effect: CartActions.APPLY_DISCOUNT}}))
        .catch(error => {
          return Observable.of(
            new CartActions.CartError(
              {error: error, errorEffect: CartActions.APPLY_DISCOUNT}));
        })
    });

  @Effect()
  emptyCart = this.actions$
    .ofType(CartActions.EMPTY_CART)
    .switchMap((action: CartActions.EmptyCart) => {
      return this.cartService.emptyCart()
        .map(res => {
          return {type: CartActions.EMPTY_CART_SUCCESS, payload: res}
        }).catch(error => {
          return Observable.of(
            new CartActions.CartError({error: error, errorEffect: CartActions.EMPTY_CART})
          )
        })
    });


  constructor(private actions$: Actions, private cartService: CartService, private router: Router) {
  }
}

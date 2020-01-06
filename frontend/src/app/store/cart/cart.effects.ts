import {Injectable} from '@angular/core';
import {Actions, Effect, ofType} from '@ngrx/effects';
import {Router} from '@angular/router';
import * as CartActions from "./cart.actions";
import {of} from "rxjs";
import {CartService} from "../../services/cart.service";
import {catchError, map, switchMap} from "rxjs/operators";


@Injectable()
export class CartEffects {


  @Effect()
  fetchCart = this.actions$
    .pipe(ofType(CartActions.FETCH_CART),
      switchMap((action: CartActions.FetchCart) => {
        return this.cartService.getCart()
          .pipe(map(res => {
            return {type: CartActions.FETCH_CART_SUCCESS, payload: {cart: res, effect: CartActions.FETCH_CART}}
          }), catchError(error => {
            return of(
              new CartActions.CartError(
                {error: error, errorEffect: CartActions.FETCH_CART}));
          }));
      }));


  @Effect()
  addToCart = this.actions$
    .pipe(ofType(CartActions.ADD_TO_CART),
      map((action: CartActions.AddToCart) => {
        return action.payload
      }),
      switchMap((payload: { id: number, amount: string }) => {
        return this.cartService.postCart(payload.id, payload.amount)
          .pipe(map(res => {
              this.router.navigate(["/checkout"]);
              return {type: CartActions.SET_CART, payload: {cart: res, effect: CartActions.ADD_TO_CART}}
            }),
            catchError(error => {
              return of(
                new CartActions.CartError(
                  {error: error, errorEffect: CartActions.ADD_TO_CART}));
            })
          );
      })
    );


  @Effect()
  removeFromCart = this.actions$
    .pipe(ofType(CartActions.REMOVE_FROM_CART),
      map((action: CartActions.RemoveFromCart) => {
        return action.payload
      }),
      switchMap((id: number) => {
        return this.cartService.removeFromCart(id)
          .pipe(map(res => ({type: CartActions.SET_CART, payload: {cart: res, effect: CartActions.REMOVE_FROM_CART}})),
            catchError(error => {
              return of(
                new CartActions.CartError(
                  {error: error, errorEffect: CartActions.REMOVE_FROM_CART}));
            })
          )
      })
    );

  @Effect()
  applyDiscountCode = this.actions$
    .pipe(ofType(CartActions.APPLY_DISCOUNT),
      map((action: CartActions.ApplyDiscount) => {
        return action.payload
      }),
      switchMap((code: string) => {
        return this.cartService.applyDiscount(code)
          .pipe(map(res => ({type: CartActions.SET_CART, payload: {cart: res, effect: CartActions.APPLY_DISCOUNT}})),
            catchError(error => {
              return of(
                new CartActions.CartError(
                  {error: error, errorEffect: CartActions.APPLY_DISCOUNT}));
            }));
      }));

  @Effect()
  emptyCart = this.actions$
    .pipe(ofType(CartActions.EMPTY_CART),
      switchMap((action: CartActions.EmptyCart) => {
        return this.cartService.emptyCart()
          .pipe(map(res => {
            return {type: CartActions.EMPTY_CART_SUCCESS, payload: res}
          }), catchError(error => {
          return of(
            new CartActions.CartError({error: error, errorEffect: CartActions.EMPTY_CART})
          )
        }));
      }));


  constructor(private actions$: Actions, private cartService: CartService, private router: Router) {
  }
}

import { Action } from '@ngrx/store';
import { HttpError } from '../app.reducers';
import { Cart } from '../model';

export const ADD_TO_CART = 'ADD_TO_CART';
export const INCREMENT_CART = 'INCREMENT_CART';
export const DECREMENT_CART = 'DECREMENT_CART';
export const REMOVE_FROM_CART = 'REMOVE_FROM_CART';
export const EMPTY_CART = 'EMPTY_CART';
export const EMPTY_CART_SUCCESS = 'EMPTY_CART_SUCCESS';
export const FETCH_CART = 'FETCH_CART';
export const FETCH_CART_SUCCESS = 'FETCH_CART_SUCCESS';
export const APPLY_DISCOUNT = 'APPLY_DISCOUNT';
export const APPLY_DISCOUNT_SUCCESS = 'APPLY_DISCOUNT_SUCCESS';
export const SET_CART = 'SET_CART';
export const CART_ERROR = 'CART_ERROR';


export class AddToCart implements Action {
  readonly type = ADD_TO_CART;

  constructor(public payload: { id: number, amount: string }) {
  }
}

export class IncrementCart implements Action {
  readonly type = INCREMENT_CART;

  constructor(public payload: { id: number, amount: string }) {
  }
}

export class DecrementCart implements Action {
  readonly type = DECREMENT_CART;

  constructor(public payload: { id: number, amount: string }) {
  }
}

export class SetCart implements Action {
  readonly type = SET_CART;

  constructor(public payload: { cart: Cart, effect: string }) {
  }
}

export class RemoveFromCart implements Action {
  readonly type = REMOVE_FROM_CART;

  constructor(public payload: number) {
  } // index of the product in the list to be deleted
}

export class EmptyCart implements Action {
  readonly type = EMPTY_CART;
}

export class EmptyCartSuccess implements Action {
  readonly type = EMPTY_CART_SUCCESS;
}


export class FetchCart implements Action {
  readonly type = FETCH_CART;
}

export class FetchCartSuccess implements Action {
  readonly type = FETCH_CART_SUCCESS;

  constructor(public payload: { cart: Cart, effect: string }) {
  }
}

export class ApplyDiscount implements Action {
  readonly type = APPLY_DISCOUNT;

  constructor(public payload: string) {
  }
}

export class ApplyDiscountSuccess implements Action {
  readonly type = APPLY_DISCOUNT_SUCCESS;

  constructor(public payload: { effect: string }) {
  }
}

export class CartError implements Action {
  readonly type = CART_ERROR;

  constructor(public payload: HttpError) {
  }
}


export type CartActions = SetCart | IncrementCart | DecrementCart | FetchCart | FetchCartSuccess | AddToCart
  | RemoveFromCart | EmptyCart | EmptyCartSuccess | ApplyDiscount | ApplyDiscountSuccess | CartError;

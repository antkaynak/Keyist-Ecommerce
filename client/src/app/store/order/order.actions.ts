import { Shipping, Payment } from './../model';

import { Action } from '@ngrx/store';
import { HttpError } from '../app.reducers';
import { Checkout, Personal } from '../model';

export const IS_CHECKOUT_ACTIVE = 'IS_CHECKOUT_ACTIVE';
export const SET_CHECKOUT_STEP = 'SET_CHECKOUT_STEP';
export const POST_PERSONAL = 'POST_PERSONAL';
export const POST_SHIPPING = 'POST_SHIPPING';
export const POST_PAYMENT = 'POST_PAYMENT';
export const POST_ORDER = 'POST_ORDER';
export const EMPTY_ORDER = 'EMPTY_ORDER';
export const ORDER_ERROR = 'ORDER_ERROR';


export class IsCheckoutActive implements Action {
  readonly type = IS_CHECKOUT_ACTIVE;

  constructor(public payload: boolean) {
  }
}

export class SetCheckoutStep implements Action {
  readonly type = SET_CHECKOUT_STEP;

  constructor(public payload: number) {
  }
}

export class PostOrder implements Action {
  readonly type = POST_ORDER;

  constructor(public payload: Checkout) {
  }
}


export class PostPersonal implements Action {
  readonly type = POST_PERSONAL;

  constructor(public payload: Personal) {
  }
}

export class PostShipping implements Action {
  readonly type = POST_SHIPPING;

  constructor(public payload: Shipping) {
  }
}

export class PostPayment implements Action {
  readonly type = POST_PAYMENT;

  constructor(public payload: Payment) {
  }
}


export class EmptyOrder implements Action {
  readonly type = EMPTY_ORDER;
}

export class OrderError implements Action {
  readonly type = ORDER_ERROR;

  constructor(public payload: HttpError) {
  }
}


export type OrderActions = IsCheckoutActive | PostOrder |
  SetCheckoutStep | PostPersonal | PostShipping | PostPayment | OrderError | EmptyOrder;

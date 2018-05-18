import {Action} from '@ngrx/store';
import {PaymentObject, PostOrdersObject} from "./order.reducer";
import {HttpError} from "../app.reducers";

export const IS_PURCHASE_ACTIVE = 'IS_PURCHASE_ACTIVE';
export const POST_ORDER_FORM = 'POST_ORDER_FORM';
export const POST_PAYMENT = 'POST_PAYMENT';
export const POST_ORDER = 'POST_ORDER';
export const EMPTY_ORDER = 'EMPTY_ORDER';
export const ORDER_ERROR = 'ORDER_ERROR';


export class IsPurchaseActive implements Action {
  readonly type = IS_PURCHASE_ACTIVE;

  constructor(public payload: boolean) {
  }
}

export class PostOrder implements Action {
  readonly type = POST_ORDER;

  constructor(public payload: PostOrdersObject) {
  }
}


export class PostOrderForm implements Action {
  readonly type = POST_ORDER_FORM;

  constructor(public payload: PostOrdersObject) {
  }
}

export class PostPayment implements Action {
  readonly type = POST_PAYMENT;

  constructor(public payload: PaymentObject) {
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


export type OrderActions = IsPurchaseActive | PostOrder |
  PostOrderForm | PostPayment | OrderError | EmptyOrder;

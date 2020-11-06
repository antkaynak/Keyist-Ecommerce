import { Action } from '@ngrx/store';
import { HttpError } from '../app.reducers';
import { Product, ProductVariantResponse } from '../model';

export const FETCH_NEWLY_ADDED = 'FETCH_NEWLY_ADDED';
export const FETCH_NEWLY_ADDED_SUCCESS = 'FETCH_NEWLY_ADDED_SUCCESS';
export const FETCH_MOST_SELLING = 'FETCH_MOST_SELLING';
export const FETCH_MOST_SELLING_SUCCESS = 'FETCH_MOST_SELLING_SUCCESS';
export const FETCH_INTERESTED = 'FETCH_INTERESTED';
export const FETCH_INTERESTED_SUCCESS = 'FETCH_INTERESTED_SUCCESS';
export const EMPTY_INTERESTED = 'EMPTY_INTERESTED';
export const SHOWCASE_ERROR = 'SHOWCASE_ERROR';


export class FetchNewlyAdded implements Action {
  readonly type = FETCH_NEWLY_ADDED;
}

export class FetchNewlyAddedSuccess implements Action {
  readonly type = FETCH_NEWLY_ADDED_SUCCESS;

  constructor(public payload: { res: Array<Product>, effect: string }) {
  }
}


export class FetchMostSelling implements Action {
  readonly type = FETCH_MOST_SELLING;
}

export class FetchMostSellingSuccess implements Action {
  readonly type = FETCH_MOST_SELLING_SUCCESS;

  constructor(public payload: { res: Array<ProductVariantResponse>, effect: string }) {
  }
}

export class FetchInterested implements Action {
  readonly type = FETCH_INTERESTED;
}

export class FetchInterestedSuccess implements Action {
  readonly type = FETCH_INTERESTED_SUCCESS;

  constructor(public payload: { res: Array<Product>, effect: string }) {
  }
}

export class EmptyInterested implements Action {
  readonly type = EMPTY_INTERESTED;
}

export class ShowcaseError implements Action {
  readonly type = SHOWCASE_ERROR;

  constructor(public payload: HttpError) {
  }
}


export type ShowcaseActions = FetchNewlyAdded | FetchNewlyAddedSuccess | FetchMostSelling | FetchMostSellingSuccess
  | FetchInterested | FetchInterestedSuccess | EmptyInterested | ShowcaseError;

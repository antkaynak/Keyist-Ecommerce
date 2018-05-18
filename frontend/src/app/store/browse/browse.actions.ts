import {Action} from '@ngrx/store';
import {ProductDisplay} from "../cart/cart.reducer";
import {Category} from "./browse.reducer";
import {HttpError} from "../app.reducers";

export const FETCH_PRODUCTS = 'FETCH_PRODUCTS';
export const FETCH_PRODUCTS_SUCCESS = 'FETCH_PRODUCTS_SUCCESS';
export const FETCH_PRODUCTS_APPEND = 'FETCH_PRODUCTS_APPEND';
export const FETCH_PRODUCTS_APPEND_SUCCESS = 'FETCH_PRODUCTS_APPEND_SUCCESS';
export const FETCH_CATEGORY = 'FETCH_CATEGORY';
export const FETCH_CATEGORY_SUCCESS = 'FETCH_CATEGORY_SUCCESS';
export const BROWSE_ERROR = 'BROWSE_ERROR';


export class FetchProducts implements Action {
  readonly type = FETCH_PRODUCTS;

  constructor(public payload: { page: number, sort: string, category: string }) {
  }
}

export class FetchProductsSuccess implements Action {
  readonly type = FETCH_PRODUCTS_SUCCESS;

  constructor(public payload: { res: ProductDisplay[], selectedPage: number, selectedSort: string, selectedCategory: string }) {
  }
}

export class FetchProductsAppend implements Action {
  readonly type = FETCH_PRODUCTS_APPEND;

  constructor(public payload: { page: number, sort: string, category: string }) {
  }
}

export class FetchProductAppendSuccess implements Action {
  readonly type = FETCH_PRODUCTS_APPEND_SUCCESS;

  constructor(public payload: { res: ProductDisplay[], selectedPage: number, selectedSort: string, selectedCategory: string }) {
  }
}

export class FetchCategory implements Action {
  readonly type = FETCH_CATEGORY;
}

export class FetchCategorySuccess implements Action {
  readonly type = FETCH_CATEGORY_SUCCESS;

  constructor(public payload: Category[]) {
  }
}

export class BrowseError implements Action {
  readonly type = BROWSE_ERROR;

  constructor(public payload: HttpError) {
  }
}


export type BrowseActions = FetchProducts | FetchProductsSuccess
  | FetchProductsAppend | FetchProductAppendSuccess
  | FetchCategory | FetchCategorySuccess | BrowseError;

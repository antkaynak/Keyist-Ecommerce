import { Action } from '@ngrx/store';
import { HttpError } from '../app.reducers';
import { ProductVariantResponse, Category, Colors } from '../model';

export const FETCH_PRODUCTS = 'FETCH_PRODUCTS';
export const FETCH_PRODUCTS_SUCCESS = 'FETCH_PRODUCTS_SUCCESS';
export const FETCH_PRODUCTS_APPEND = 'FETCH_PRODUCTS_APPEND';
export const FETCH_PRODUCTS_APPEND_SUCCESS = 'FETCH_PRODUCTS_APPEND_SUCCESS';
export const FETCH_PRODUCTS_COUNT = 'FETCH_PRODUCTS_COUNT';
export const FETCH_PRODUCTS_COUNT_SUCCESS = 'FETCH_PRODUCTS_COUNT_SUCCESS';
export const FETCH_CATEGORY = 'FETCH_CATEGORY';
export const FETCH_CATEGORY_SUCCESS = 'FETCH_CATEGORY_SUCCESS';
export const FETCH_COLORS = 'FETCH_COLORS';
export const FETCH_COLORS_SUCCESS = 'FETCH_COLORS_SUCCESS';
export const BROWSE_ERROR = 'BROWSE_ERROR';


export class FetchProducts implements Action {
  readonly type = FETCH_PRODUCTS;

  constructor(public payload: { page: number, sort: string, category: string, color: string, minPrice: string, maxPrice: string }) {
  }
}

export class FetchProductsSuccess implements Action {
  readonly type = FETCH_PRODUCTS_SUCCESS;

  constructor(public payload: { res: Array<ProductVariantResponse>, effect: string, selectedPage: number, selectedSort: string, selectedCategory: string, selectedColor: string, minPrice: string, maxPrice: string }) {
  }
}

export class FetchProductsAppend implements Action {
  readonly type = FETCH_PRODUCTS_APPEND;

  constructor(public payload: { page: number, sort: string, category: string, color: string, minPrice: string, maxPrice: string }) {
  }
}

export class FetchProductAppendSuccess implements Action {
  readonly type = FETCH_PRODUCTS_APPEND_SUCCESS;

  constructor(public payload: { res: Array<ProductVariantResponse>, effect: string, selectedPage: number, selectedSort: string, selectedCategory: string, selectedColor: string, minPrice: string, maxPrice: string }) {
  }
}

export class FetchProductsCount implements Action {
  readonly type = FETCH_PRODUCTS_COUNT;

  constructor(public payload: { category: string, color: string, minPrice: string, maxPrice: string }) {
  }
}

export class FetchProductsCountSuccess implements Action {
  readonly type = FETCH_PRODUCTS_COUNT_SUCCESS;

  constructor(public payload: { res: number, effect: string }) {
  }
}

export class FetchCategory implements Action {
  readonly type = FETCH_CATEGORY;
}

export class FetchCategorySuccess implements Action {
  readonly type = FETCH_CATEGORY_SUCCESS;

  constructor(public payload: { res: Array<Category>, effect: string }) {
  }
}


export class FetchColors implements Action {
  readonly type = FETCH_COLORS;
}

export class FetchColorsSuccess implements Action {
  readonly type = FETCH_COLORS_SUCCESS;

  constructor(public payload: { res: Array<Colors>, effect: string }) {
  }
}


export class BrowseError implements Action {
  readonly type = BROWSE_ERROR;

  constructor(public payload: HttpError) {
  }
}


export type BrowseActions = FetchProducts | FetchProductsSuccess |
  FetchProductsAppend | FetchProductAppendSuccess |
  FetchProductsCount | FetchProductsCountSuccess |
  FetchCategory | FetchCategorySuccess |
  FetchColors | FetchColorsSuccess | BrowseError;

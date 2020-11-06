import { Injectable } from '@angular/core';
import { Actions, Effect, ofType } from '@ngrx/effects';
import * as BrowseActions from './browse.actions';
import { ProductService } from '../../services/product.service';
import { of } from 'rxjs';
import { catchError, map, mergeMap, switchMap } from 'rxjs/operators';


@Injectable()
export class BrowseEffects {

  @Effect()
  fetchProducts = this.actions$
    .pipe(ofType(BrowseActions.FETCH_PRODUCTS),
      map((action: BrowseActions.FetchProducts) => {
        return action.payload;
      }),
      switchMap((params: { page: number, sort: string, category: string, color: string, minPrice: string, maxPrice: string }) => {
        return this.productService.getProducts(params.page, params.sort, params.category, params.color, params.minPrice, params.maxPrice)
          .pipe(map(res => {
            return {
              type: BrowseActions.FETCH_PRODUCTS_SUCCESS,
              payload: {
                res,
                effect: BrowseActions.FETCH_PRODUCTS,
                selectedPage: params.page + 1,
                selectedSort: params.sort,
                selectedCategory: params.category,
                selectedColor: params.color,
                minPrice: params.minPrice,
                maxPrice: params.maxPrice
              }
            };
          }), catchError(error => of(new BrowseActions.BrowseError({ error, errorEffect: BrowseActions.FETCH_PRODUCTS }))));
      })
    );

  @Effect()
  fetchProductsAppend = this.actions$
    .pipe(ofType(BrowseActions.FETCH_PRODUCTS_APPEND),
      map((action: BrowseActions.FetchProductsAppend) => {
        return action.payload;
      }),
      mergeMap((params: { page: number, sort: string, category: string, color: string, minPrice: string, maxPrice: string }) => {
        return this.productService.getProducts(params.page, params.sort, params.category, params.color, params.minPrice, params.maxPrice)
          .pipe(map(res => {
            return {
              type: BrowseActions.FETCH_PRODUCTS_APPEND_SUCCESS,
              payload: {
                res,
                effect: BrowseActions.FETCH_PRODUCTS_APPEND,
                selectedPage: params.page + 1,
                selectedSort: params.sort,
                selectedCategory: params.category,
                selectedColor: params.color,
                minPrice: params.minPrice,
                maxPrice: params.maxPrice
              }
            };
          }), catchError(error => of(new BrowseActions.BrowseError({ error, errorEffect: BrowseActions.FETCH_PRODUCTS_APPEND }))));
      })
    );

  @Effect()
  fetchProductsCount = this.actions$
    .pipe(ofType(BrowseActions.FETCH_PRODUCTS_COUNT),
      map((action: BrowseActions.FetchProductsCount) => {
        return action.payload;
      }),
      switchMap((params: { category: string, color: string, minPrice: string, maxPrice: string }) => {
        return this.productService.getProductsCount(params.category, params.color, params.minPrice, params.maxPrice)
          .pipe(map(res => {
            return {
              type: BrowseActions.FETCH_PRODUCTS_COUNT_SUCCESS,
              payload: {
                res,
                effect: BrowseActions.FETCH_PRODUCTS_COUNT
              }
            };
          }), catchError(error => of(new BrowseActions.BrowseError({ error, errorEffect: BrowseActions.FETCH_PRODUCTS_COUNT }))));
      })
    );

  @Effect()
  fetchCategory = this.actions$
    .pipe(ofType(BrowseActions.FETCH_CATEGORY),
      switchMap((action: BrowseActions.FetchCategory) => {
        return this.productService.getCategory()
          .pipe(map(res => {
            return { type: BrowseActions.FETCH_CATEGORY_SUCCESS, payload: { res, effect: BrowseActions.FETCH_CATEGORY } };
          }), catchError(error => of(new BrowseActions.BrowseError({ error, errorEffect: BrowseActions.FETCH_CATEGORY }))));
      })
    );

  @Effect()
  fetchColor = this.actions$
    .pipe(ofType(BrowseActions.FETCH_COLORS),
      switchMap((action: BrowseActions.FetchColors) => {
        return this.productService.getColors()
          .pipe(map(res => {
            return { type: BrowseActions.FETCH_COLORS_SUCCESS, payload: { res, effect: BrowseActions.FETCH_COLORS } };
          }), catchError(error => of(new BrowseActions.BrowseError({ error, errorEffect: BrowseActions.FETCH_COLORS }))));
      })
    );

  constructor(private actions$: Actions, private productService: ProductService) {
  }
}

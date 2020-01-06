import {Injectable} from '@angular/core';
import {Actions, Effect, ofType} from '@ngrx/effects';
import * as BrowseActions from "./browse.actions";
import {ProductService} from "../../services/product.service";
import {of} from "rxjs";
import {catchError, map, mergeMap, switchMap} from "rxjs/operators";


@Injectable()
export class BrowseEffects {

  /*
   * FetchProducts and FetchProductsAppend are different in the
   * way that FetchProducts use switchMap but FetchProductsAppend
   * use mergeMap. So when user tries to append the requests will
   * go through one by one but if user wants to fetch the products
   * from the page 0 with different parameters the switchMap will
   * cancel all other requests.
   * */

  @Effect()
  fetchProducts = this.actions$
    .pipe(ofType(BrowseActions.FETCH_PRODUCTS),
      map((action: BrowseActions.FetchProducts) => {
        return action.payload;
      }),
      switchMap((params: { page: number, sort: string, category: string }) => {
        return this.productService.getProducts(params.page, params.sort, params.category)
          .pipe(map(res => {
            return {
              type: BrowseActions.FETCH_PRODUCTS_SUCCESS,
              payload: {
                res: res,
                selectedPage: ++params.page,
                selectedSort: params.sort,
                selectedCategory: params.category
              }
            }
          }), catchError(error => {
          return of(
            new BrowseActions.BrowseError(
              {error: error, errorEffect: BrowseActions.FETCH_PRODUCTS}));
        }));
      })
      );

  @Effect()
  fetchProductsAppend = this.actions$
    .pipe(ofType(BrowseActions.FETCH_PRODUCTS_APPEND),
      map((action: BrowseActions.FetchProducts) => {
        return action.payload;
      }),
      mergeMap((params: { page: number, sort: string, category: string }) => {
        return this.productService.getProducts(params.page, params.sort, params.category)
          .pipe(map(res => {
            return {
              type: BrowseActions.FETCH_PRODUCTS_APPEND_SUCCESS,
              payload: {
                res: res,
                selectedPage: ++params.page,
                selectedSort: params.sort,
                selectedCategory: params.category
              }
            }
          }), catchError(error => {
          return of(
            new BrowseActions.BrowseError(
              {error: error, errorEffect: BrowseActions.FETCH_PRODUCTS_APPEND}));
        }));
      })
    );

  @Effect()
  fetchCategory = this.actions$
    .pipe(ofType(BrowseActions.FETCH_CATEGORY),
      switchMap((action: BrowseActions.FetchCategory) => {
        return this.productService.getCategory()
          .pipe(map(res => {
            return {type: BrowseActions.FETCH_CATEGORY_SUCCESS, payload: res}
          }), catchError(error => {
          return of(
            new BrowseActions.BrowseError(
              {error: error, errorEffect: BrowseActions.FETCH_CATEGORY}));
        }));
      }));

  constructor(private actions$: Actions, private productService: ProductService) {
  }
}

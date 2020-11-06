import { Injectable } from '@angular/core';
import { Actions, Effect, ofType } from '@ngrx/effects';
import * as ShowcaseActions from './showcase.actions';
import { ProductService } from '../../services/product.service';
import { map, switchMap, catchError } from 'rxjs/operators';
import { of } from 'rxjs';


@Injectable()
export class ShowcaseEffects {


  @Effect()
  fetchNewlyAdded = this.actions$
    .pipe(ofType(ShowcaseActions.FETCH_NEWLY_ADDED),
      switchMap((action: ShowcaseActions.FetchNewlyAdded) => this.productService.getNewlyAdded()
        .pipe(map(res => ({ type: ShowcaseActions.FETCH_NEWLY_ADDED_SUCCESS, payload: { res, effect: ShowcaseActions.FETCH_NEWLY_ADDED } })),
          catchError(error => of(new ShowcaseActions.ShowcaseError({ error, errorEffect: ShowcaseActions.FETCH_NEWLY_ADDED }))))
      ));

  @Effect()
  fetchMostSelling = this.actions$
    .pipe(ofType(ShowcaseActions.FETCH_MOST_SELLING),
      switchMap((action: ShowcaseActions.FetchMostSelling) => this.productService.getMostSelling()
        .pipe(map(res => ({ type: ShowcaseActions.FETCH_MOST_SELLING_SUCCESS, payload: { res, effect: ShowcaseActions.FETCH_MOST_SELLING } })),
          catchError(error => of(new ShowcaseActions.ShowcaseError({ error, errorEffect: ShowcaseActions.FETCH_MOST_SELLING }))))
      ));

  @Effect()
  fetchInterested = this.actions$
    .pipe(ofType(ShowcaseActions.FETCH_INTERESTED),
      switchMap((action: ShowcaseActions.FetchInterested) => this.productService.getInterested()
        .pipe(map(res => ({ type: ShowcaseActions.FETCH_INTERESTED_SUCCESS, payload: { res, effect: ShowcaseActions.FETCH_INTERESTED } }),
          catchError(error => of(new ShowcaseActions.ShowcaseError({ error, errorEffect: ShowcaseActions.FETCH_INTERESTED })))))
      ));


  constructor(private actions$: Actions, private productService: ProductService) {
  }
}

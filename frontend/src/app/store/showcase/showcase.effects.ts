import {Injectable} from '@angular/core';
import {Actions, Effect} from '@ngrx/effects';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/mergeMap';

import * as ShowcaseActions from "./showcase.actions";
import {ProductService} from "../../services/product.service";


@Injectable()
export class ShowcaseEffects {


  @Effect()
  fetchNewlyAdded = this.actions$
    .ofType(ShowcaseActions.FETCH_NEWLY_ADDED)
    .switchMap((action: ShowcaseActions.FetchNewlyAdded) => this.productService.getNewlyAdded()
      .map(res => ({type: ShowcaseActions.FETCH_NEWLY_ADDED_SUCCESS, payload: res}))
    );

  @Effect()
  fetchMostSelling = this.actions$
    .ofType(ShowcaseActions.FETCH_MOST_SELLING)
    .switchMap((action: ShowcaseActions.FetchMostSelling) => this.productService.getMostSelling()
      .map(res => ({type: ShowcaseActions.FETCH_MOST_SELLING_SUCCESS, payload: res}))
    );

  @Effect()
  fetchInterested = this.actions$
    .ofType(ShowcaseActions.FETCH_INTERESTED)
    .switchMap((action: ShowcaseActions.FetchInterested) => this.productService.getInterested()
      .map(res => ({type: ShowcaseActions.FETCH_INTERESTED_SUCCESS, payload: res}))
    );


  constructor(private actions$: Actions, private productService: ProductService) {
  }
}

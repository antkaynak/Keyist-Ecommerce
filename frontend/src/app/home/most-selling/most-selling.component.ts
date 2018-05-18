import {Component, OnDestroy, OnInit} from '@angular/core';
import * as fromApp from "../../store/app.reducers";
import {Store} from "@ngrx/store";
import {Observable} from "rxjs/Observable";
import {ProductDisplay} from "../../store/cart/cart.reducer";
import {Subscription} from "rxjs/Subscription";
import * as ShowcaseActions from '../../store/showcase/showcase.actions';
import 'rxjs/add/operator/filter';

@Component({
  selector: 'app-most-selling',
  templateUrl: './most-selling.component.html',
  styleUrls: ['./most-selling.component.css']
})
export class MostSellingComponent implements OnInit, OnDestroy {


  showcaseState: Observable<{ newlyAdded: ProductDisplay[], mostSelling: ProductDisplay[], interested: ProductDisplay[] }>;
  showcaseSubscription: Subscription;
  isFetched: boolean = false;//prevents fetching twice.


  constructor(private store: Store<fromApp.AppState>) {
  }

  ngOnInit() {
    this.showcaseState = this.store.select('showcase');
    this.showcaseSubscription = this.showcaseState
      .filter(data => data.mostSelling.length == 0 && !this.isFetched)
      .subscribe(
        data => {
          this.store.dispatch(new ShowcaseActions.FetchMostSelling());
          this.isFetched = true;
        }
      );

  }

  ngOnDestroy(): void {
    if (this.showcaseSubscription != null) {
      this.showcaseSubscription.unsubscribe();
    }
  }

}

import {Component, OnDestroy, OnInit} from '@angular/core';
import {Observable, Subscription} from "rxjs";
import {Store} from "@ngrx/store";
import {ProductDisplay} from "../../../store/cart/cart.reducer";
import * as ShowcaseActions from "../../../store/showcase/showcase.actions";
import * as fromApp from "../../../store/app.reducers";
import {filter, take} from "rxjs/operators";

@Component({
  selector: 'app-interested',
  templateUrl: './interested.component.html',
  styleUrls: ['./interested.component.css']
})
export class InterestedComponent implements OnInit, OnDestroy {

  showcaseState: Observable<{ newlyAdded: ProductDisplay[], mostSelling: ProductDisplay[], interested: ProductDisplay[] }>;
  authState: Observable<{ authenticated: boolean }>;

  showcaseSubscription: Subscription;

  isFetched: boolean = false;


  constructor(private store: Store<fromApp.AppState>) {
  }

  ngOnInit() {
    this.showcaseState = this.store.select('showcase');
    this.authState = this.store.select("auth");
    this.authState.pipe(take(1)).subscribe(data => {
      if (data.authenticated) {
        this.showcaseSubscription = this.showcaseState.pipe(filter(data => data.interested.length == 0 && !this.isFetched))
          .subscribe(
            data => {
              this.store.dispatch(new ShowcaseActions.FetchInterested());
              this.isFetched = true;
            }
          );
      }
    });
  }

  ngOnDestroy(): void {
    if (this.showcaseSubscription != null) {
      this.showcaseSubscription.unsubscribe();
    }
  }

}

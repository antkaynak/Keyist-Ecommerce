import { ShowcaseState } from './../../store/showcase/showcase.reducer';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { Store } from '@ngrx/store';
import * as ShowcaseActions from '../../store/showcase/showcase.actions';
import * as fromApp from '../../store/app.reducers';
import { take } from 'rxjs/operators';

@Component({
  selector: 'app-interested',
  templateUrl: './interested.component.html',
  styleUrls: ['./interested.component.scss']
})
export class InterestedComponent implements OnInit {

  @ViewChild('interestedCards') interestedCards: ElementRef;

  paramSubscription: Subscription;

  showcaseState: Observable<ShowcaseState>;
  fetchError: HttpErrorResponse = null;
  productUrl: string;

  constructor(private store: Store<fromApp.AppState>) {
  }

  ngOnInit() {
    this.showcaseState = this.store.select('showcase');
    this.store.select('auth').pipe(take(1)).subscribe(auth => {
      if (auth.authenticated) {
        this.showcaseState.pipe(take(1))
          .subscribe(data => {
            if (data.interested.length === 0) {
              this.store.dispatch(new ShowcaseActions.FetchInterested());
            }
          });
      }
    });
  }

  scrollLeft() {
    this.interestedCards.nativeElement.scrollLeft -= 250;
  }

  scrollRight() {
    this.interestedCards.nativeElement.scrollLeft += 250;
  }
}

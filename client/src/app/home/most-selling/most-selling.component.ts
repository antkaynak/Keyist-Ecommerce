import { ShowcaseState } from './../../store/showcase/showcase.reducer';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import * as fromApp from '../../store/app.reducers';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import * as ShowcaseActions from '../../store/showcase/showcase.actions';
import { take } from 'rxjs/operators';

@Component({
  selector: 'app-most-selling',
  templateUrl: './most-selling.component.html',
  styleUrls: ['./most-selling.component.scss']
})
export class MostSellingComponent implements OnInit {

  @ViewChild('mostCards') mostCards: ElementRef;

  showcaseState: Observable<ShowcaseState>;

  constructor(private store: Store<fromApp.AppState>) {
  }

  ngOnInit() {
    this.showcaseState = this.store.select('showcase');
    this.showcaseState
      .pipe(take(1))
      .subscribe(
        data => {
          if (data.mostSelling.length === 0) {
            this.store.dispatch(new ShowcaseActions.FetchMostSelling());
          }
        }
      );
  }

  scrollLeft() {
    this.mostCards.nativeElement.scrollLeft -= 250;
  }

  scrollRight() {
    this.mostCards.nativeElement.scrollLeft += 250;
  }
}

import {Component, HostListener, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs/Subscription";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import * as fromApp from "../store/app.reducers";
import {HttpError} from "../store/app.reducers";
import {Store} from "@ngrx/store";
import * as BrowseActions from "../store/browse/browse.actions";
import {ProductDisplay} from "../store/cart/cart.reducer";
import {Observable} from "rxjs/Observable";
import {Category} from "../store/browse/browse.reducer";
import 'rxjs/add/operator/take';


@Component({
  selector: 'app-browse',
  templateUrl: './browse.component.html',
  styleUrls: ['./browse.component.css']
})
export class BrowseComponent implements OnInit, OnDestroy {


  sortBy: Object[] = [
    {
      display: 'Any',
      value: 'any'
    },
    {
      display: 'Lowest Price',
      value: 'lowest'
    },
    {
      display: 'Highest Price',
      value: 'highest'
    }
  ];

  browseOptionsForm: FormGroup;


  browseState: Observable<{
    products: ProductDisplay[], categories: Category[], canFetch: boolean,
    selectedPage: number, selectedSort: string, selectedCategory: string, errors: HttpError[]
  }>;
  canFetchSubscription: Subscription;

  canFetch: boolean = false;
  selectedPage: number = 0;
  selectedSort: string = 'any';
  selectedCategory: string = 'any';

  constructor(private store: Store<fromApp.AppState>) {
  }

  ngOnInit() {

    this.browseState = this.store.select('browse');
    this.canFetchSubscription = this.browseState.subscribe(data => {
      this.canFetch = data.canFetch;
    });

    this.browseOptionsForm = new FormGroup({
      'sortBy': new FormControl(null, Validators.required),
      'category': new FormControl(null, Validators.required)
    });

    this.browseState.take(1).subscribe(data => {
      this.selectedPage = data.selectedPage;
      this.selectedSort = data.selectedSort;
      this.selectedCategory = data.selectedCategory;
      this.browseOptionsForm.patchValue({
        sortBy: data.selectedSort,
        category: data.selectedCategory
      });
      if (data.categories.length == 0) {
        this.store.dispatch(new BrowseActions.FetchCategory());
      }
      if (data.products.length == 0) {
        this.getProducts(this.selectedPage, this.selectedSort, this.selectedCategory);
      }
    });


  }

  ngOnDestroy(): void {
    if (this.canFetchSubscription != null) {
      this.canFetchSubscription.unsubscribe();
    }
  }

  @HostListener('window:scroll', ['$event'])
  onScroll($event: Event): void {
    if ((window.innerHeight + window.scrollY + 100) >= document.body.offsetHeight) {
      if (this.canFetch) {
        this.getProductsAppend(this.selectedPage, this.browseOptionsForm.value.sortBy, this.browseOptionsForm.value.category);
      }
    }
  }


  onSubmitted() {
    this.selectedPage = 0;
    this.getProducts(this.selectedPage, this.browseOptionsForm.value.sortBy, this.browseOptionsForm.value.category);
  }

  getProducts(page, sort, category) {
    this.store.dispatch(new BrowseActions.FetchProducts({page: page, sort: sort, category: category}));
    this.selectedPage++;
  }

  getProductsAppend(page, sort, category) {
    this.store.dispatch(new BrowseActions.FetchProductsAppend({page: page, sort: sort, category: category}));
    this.selectedPage++;
  }
}

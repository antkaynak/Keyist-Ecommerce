import { Category } from './../store/model';
import { BrowseState } from './../store/browse/browse.reducer';
import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import * as fromApp from '../store/app.reducers';
import { Store } from '@ngrx/store';
import * as BrowseActions from '../store/browse/browse.actions';
import { Observable, Subscription } from 'rxjs';
import { take } from 'rxjs/operators';


@Component({
  selector: 'app-browse',
  templateUrl: './browse.component.html',
  styleUrls: ['./browse.component.scss']
})
export class BrowseComponent implements OnInit, OnDestroy {


  sortBy = [
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

  browseState: Observable<BrowseState>;
  canFetchSubscription: Subscription;

  canFetch = false;
  selectedPage = 0;
  selectedSort = 'any';
  selectedCategory = 'any';
  selectedColor = 'any';
  minPrice = '0';
  maxPrice = '0';

  constructor(private store: Store<fromApp.AppState>) {
  }

  ngOnInit() {
    this.browseState = this.store.select('browse');
    this.canFetchSubscription = this.browseState.subscribe(data => {
      this.canFetch = data.canFetch;
    });

    this.browseState.pipe(take(1)).subscribe(data => {
      this.selectedPage = data.selectedPage;
      this.selectedSort = data.selectedSort;
      this.selectedCategory = data.selectedCategory;
      this.selectedColor = data.selectedColor;
      this.minPrice = data.minPrice;
      this.maxPrice = data.maxPrice;

      if (data.categories.length === 0) {
        this.store.dispatch(new BrowseActions.FetchCategory());
      }
      if (!data.colors || data.colors.length === 0) {
        this.store.dispatch(new BrowseActions.FetchColors());
      }
      if (data.products.length === 0) {
        this.getProducts();
      }
    });


  }

  ngOnDestroy(): void {
    if (this.canFetchSubscription) {
      this.canFetchSubscription.unsubscribe();
    }
  }

  @HostListener('window:scroll', ['$event'])
  onScroll($event: Event): void {
    if ((window.innerHeight + window.scrollY + 400) >= document.body.offsetHeight) {
      if (this.canFetch) {
        this.getProductsAppend();
      }
    }
  }

  selectMin(minPrice: string) {
    this.minPrice = minPrice.trim().length === 0 ? '0' : minPrice.trim();
    this.getProducts();
  }

  selectMax(maxPrice: string) {
    this.maxPrice = maxPrice.trim().length === 0 ? '0' : maxPrice.trim();
    this.getProducts();

  }

  selectCategory(category: string) {
    this.selectedCategory = category;
    this.getProducts();
  }

  selectColor(color: string) {
    this.selectedColor = color;
    this.getProducts();
  }

  selectSort(sort: string) {
    this.selectedSort = sort;
    this.getProducts();
  }

  clearCategory() {
    this.selectedCategory = 'any';
    this.getProducts();
  }


  clearPrice() {
    this.minPrice = '0';
    this.maxPrice = '0';
    this.getProducts();
  }

  clearColor() {
    this.selectedColor = 'any';
    this.getProducts();
  }


  getProducts() {
    this.selectedPage = 0;
    this.store.dispatch(new BrowseActions.FetchProducts({ page: this.selectedPage, sort: this.selectedSort, category: this.selectedCategory, color: this.selectedColor, minPrice: this.minPrice, maxPrice: this.maxPrice }));
    this.getProductsCount();
    this.selectedPage++;
  }

  getProductsCount() {
    this.store.dispatch(new BrowseActions.FetchProductsCount({ category: this.selectedCategory, color: this.selectedColor, minPrice: this.minPrice, maxPrice: this.maxPrice }));
  }

  getProductsAppend() {
    this.store.dispatch(new BrowseActions.FetchProductsAppend({ page: this.selectedPage, sort: this.selectedSort, category: this.selectedCategory, color: this.selectedColor, minPrice: this.minPrice, maxPrice: this.maxPrice }));
    this.selectedPage++;
  }
}

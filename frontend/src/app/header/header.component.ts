import {Component, OnDestroy, OnInit} from '@angular/core';
import {Store} from "@ngrx/store";
import * as fromApp from "../store/app.reducers";
import {HttpError} from "../store/app.reducers";
import * as CartActions from '../store/cart/cart.actions';
import * as AuthActions from '../store/auth/auth.actions';
import {Observable} from "rxjs/Observable";
import {Cart} from "../store/cart/cart.reducer";
import {Router} from "@angular/router";
import 'rxjs/add/operator/filter';
import 'rxjs/add/operator/take';
import {Subscription} from "rxjs/Subscription";
import {NgbDropdownConfig} from "@ng-bootstrap/ng-bootstrap";


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
  providers: [NgbDropdownConfig]
})
export class HeaderComponent implements OnInit, OnDestroy {

  cartState: Observable<{ cart: Cart, errors: HttpError[], loading: boolean }>;
  cartItemCountSubscription: Subscription;
  cartItemCount: number = 0;
  isCollapsed: boolean = true;

  authState: Observable<{ authenticated: boolean, isActive: boolean }>;
  authStateSubscription: Subscription;
  innerAuthState: boolean = false;

  constructor(private store: Store<fromApp.AppState>, private router: Router, public dropdownConfig: NgbDropdownConfig) {
    dropdownConfig.placement = 'bottom-right';
  }

  ngOnInit() {

    this.authState = this.store.select('auth');
    this.cartState = this.store.select('cart');

    this.authStateSubscription = this.authState
      .subscribe(
        (data) => {
          if (data.authenticated && !this.innerAuthState) {
            this.innerAuthState = true;
            this.store.dispatch(new CartActions.FetchCart());
            this.cartItemCountSubscription = this.cartState.subscribe(data => {
              let totalCount = 0;
              for (let i = 0; i < data.cart.cartItemList.length; i++) {
                totalCount += data.cart.cartItemList[i].amount;
              }
              this.cartItemCount = totalCount;
            });
          }
          else if (!data.authenticated) {
            this.innerAuthState = false;
            this.cartItemCount = 0;
            if (this.cartItemCountSubscription != null) {
              this.cartItemCountSubscription.unsubscribe();
            }
          }
        }
      );

  }

  ngOnDestroy() {
    if (this.authStateSubscription != null) {
      this.authStateSubscription.unsubscribe();
    }
    if (this.cartItemCountSubscription != null) {
      this.cartItemCountSubscription.unsubscribe();
    }
  }


  userSignOut() {
    this.store.dispatch(new AuthActions.SignOut());
    this.router.navigate(['/']);
  }


  searchProduct(search: HTMLInputElement) {
    if (search.value.trim().length === 0) {
      return;
    }
    let url = '/search/' + search.value;
    console.log(url);
    this.router.navigate([url]);
  }
}

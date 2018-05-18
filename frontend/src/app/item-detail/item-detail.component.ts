import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, NavigationEnd, Params, Router} from "@angular/router";
import {Subscription} from "rxjs/Subscription";
import {ProductService} from "../services/product.service";
import {Store} from "@ngrx/store";
import * as fromApp from '../store/app.reducers';
import {HttpError} from '../store/app.reducers';
import * as CartActions from '../store/cart/cart.actions';
import {Cart, Product} from "../store/cart/cart.reducer";
import {Observable} from "rxjs/Observable";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {LocationStrategy} from "@angular/common";
import 'rxjs/add/operator/take';
import 'rxjs/add/operator/catch';
import {HttpErrorResponse} from "@angular/common/http";


@Component({
  selector: 'app-item-detail',
  templateUrl: './item-detail.component.html',
  styleUrls: ['./item-detail.component.css']
})
export class ItemDetailComponent implements OnInit, OnDestroy {

  paramSubscription: Subscription;

  product: Product;

  cartState: Observable<{ cart: Cart, errors: HttpError[], loading: boolean }>;
  inlineLoading: boolean = true;


  id: number;

  isPopState = false;
  fetchError: HttpErrorResponse = null;
  routerSubscription: Subscription;


  constructor(private router: Router, private route: ActivatedRoute,
              private locStrat: LocationStrategy,
              private productService: ProductService,
              private store: Store<fromApp.AppState>, private modalService: NgbModal) {
  }

  ngOnInit() {
    this.locStrat.onPopState(() => {
      this.isPopState = true;
    });

    this.routerSubscription = this.router.events.subscribe(event => {
      // Scroll to top if accessing a page, not via browser history stack
      if (event instanceof NavigationEnd && !this.isPopState) {
        window.scrollTo(0, 0);
        this.isPopState = false;
      }

      // Ensures that isPopState is reset
      if (event instanceof NavigationEnd) {
        this.isPopState = false;
      }
    });

    this.cartState = this.store.select('cart');
    this.paramSubscription = this.route.params.subscribe(
      (params: Params) => {
        this.id = params['id'];
        this.inlineLoading = true;
        this.productService.getFullProduct(this.id)
          .take(1)
          .catch(error => {
            this.fetchError = error;
            this.inlineLoading = false;
            return Observable.throw(error);
          }).subscribe(
          (data: Product) => {
            this.product = data;
            this.inlineLoading = false;
          }
        );
      }
    );

  }

  ngOnDestroy() {
    if (this.paramSubscription != null) {
      this.paramSubscription.unsubscribe();
    }
    if (this.routerSubscription != null) {
      this.routerSubscription.unsubscribe();
    }

  }

  addToCart(amount: HTMLInputElement) {
    const val = amount.value;
    let reg = new RegExp('^[0-9]+$');
    if (!reg.test(val) || parseInt(val) == 0) {
      alert("Please enter a valid amount.");
      return;
    }

    this.store.select('auth')
      .take(1)
      .subscribe(data => {
        if (data.authenticated) {
          this.store.dispatch(new CartActions.AddToCart({id: this.id, amount: val}));
        }
        else {
          this.router.navigate(["/login"]);
        }
      });

  }


  open(content) {
    this.modalService.open(content);
  }
}

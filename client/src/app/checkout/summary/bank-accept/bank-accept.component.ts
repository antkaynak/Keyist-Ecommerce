import * as CartActions from './../../../store/cart/cart.actions';
import { CartState } from './../../../store/cart/cart.reducer';
import { Checkout, Cart } from './../../../store/model';
import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import * as fromApp from '../../../store/app.reducers';
import { Store } from '@ngrx/store';
import * as OrderActions from '../../../store/order/order.actions';
import { Router } from '@angular/router';
import { CartService } from '../../../services/cart.service';
import { Observable, throwError } from 'rxjs';
import { OrderState } from 'src/app/store/order/order.reducer';
import { catchError, take } from 'rxjs/operators';

@Component({
  selector: 'app-bank-accept',
  templateUrl: './bank-accept.component.html'
})
export class BankAcceptComponent implements OnInit {


  cartState: Observable<CartState>;
  orderState: Observable<OrderState>;

  postOrder: Checkout;
  postCart: Cart;

  constructor(public activeModal: NgbActiveModal, private store: Store<fromApp.AppState>, private router: Router,
              private cartService: CartService) {
  }

  ngOnInit() {
    this.cartState = this.store.select('cart');
    this.orderState = this.store.select('order');


    this.orderState.subscribe((orderState: OrderState) => {
      this.postOrder = {
        shipName: orderState.personal.shipName,
        shipAddress: orderState.shipping.shipAddress,
        billingAddress: orderState.shipping.billingAddress ?
          orderState.shipping.billingAddress : orderState.shipping.shipAddress,
        city: orderState.shipping.city,
        state: orderState.shipping.state,
        zip: orderState.shipping.zip,
        country: orderState.shipping.country,
        phone: orderState.personal.phone
      };
    });

    this.cartState.subscribe((cartState: CartState) => {
      this.postCart = cartState.cart;
    });
  }


  paymentConfirm() {
    this.cartService.confirmCart(this.postCart).pipe(take(1), catchError(
      error => {
        alert('An error occurred. Operation cancelled. Please try again.');
        if (error.status === 400) {
          this.store.dispatch(new CartActions.FetchCart());
        }
        this.router.navigate(['/cart']);
        return throwError(error);
      }
    )).subscribe(() => {
      this.store.dispatch(new OrderActions.PostOrder(this.postOrder));
    });
    this.activeModal.close('Close click');
  }
}

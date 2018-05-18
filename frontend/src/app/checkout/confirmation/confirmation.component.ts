import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import * as fromApp from "../../store/app.reducers";
import {Store} from "@ngrx/store";
import {Observable} from "rxjs/Observable";
import {Cart} from "../../store/cart/cart.reducer";
import {PaymentObject, PostOrdersObject} from "../../store/order/order.reducer";
import {NgbModal, NgbModalOptions} from "@ng-bootstrap/ng-bootstrap";
import {BankAcceptComponent} from "./bank-accept/bank-accept.component";
import 'rxjs/add/operator/take';

@Component({
  selector: 'app-confirmation',
  templateUrl: './confirmation.component.html',
  styleUrls: ['./confirmation.component.css']
})
export class ConfirmationComponent implements OnInit {

  cartState: Observable<{ cart: Cart }>;
  fetchedCart: Cart = null;

  orderState: Observable<{ postOrders: PostOrdersObject, postPayment: PaymentObject }>;
  fetchedPostOrdersObject: PostOrdersObject = null;

  modalOption: NgbModalOptions = {};

  innerLoading: boolean = false;


  constructor(private store: Store<fromApp.AppState>, private router: Router, private modalService: NgbModal) {
  }

  ngOnInit() {
    this.cartState = this.store.select('cart');
    this.cartState.take(1).subscribe(data => {
      if (data == null || data.cart == null) {
        this.router.navigate(["/checkout"]);
      }
      this.fetchedCart = data.cart;
    });

    this.orderState = this.store.select('order');
    this.orderState.take(1).subscribe(data => {
      if (data == null || data.postPayment == null || data.postOrders == null) {
        this.router.navigate(["/checkout"]);
      } else {
        this.fetchedPostOrdersObject = data.postOrders;
      }
    });
  }

  openPaymentModal() {
    //TODO bank system is a demo so there might be bugs related to the payment.
    //TODO For a real system this whole logic must be changed in order to provide security.

    this.innerLoading = true;

    this.modalOption.backdrop = 'static';
    this.modalOption.keyboard = false;
    this.modalOption.centered = true;
    const modalRef = this.modalService.open(BankAcceptComponent, this.modalOption);
    modalRef.componentInstance.bank = 'Demo Bank';
    modalRef.componentInstance.totalPrice = this.fetchedCart.totalPrice;
    modalRef.componentInstance.postOrdersObject = this.fetchedPostOrdersObject;
    modalRef.componentInstance.postOrdersCartObject = this.fetchedCart;
  }
}

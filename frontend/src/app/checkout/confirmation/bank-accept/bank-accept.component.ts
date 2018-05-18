import {Component, Input, OnInit} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import * as fromApp from "../../../store/app.reducers";
import {Store} from "@ngrx/store";
import * as OrderActions from "../../../store/order/order.actions";
import * as CartActions from "../../../store/cart/cart.actions";
import {PostOrdersObject} from "../../../store/order/order.reducer";
import {Router} from "@angular/router";
import {Cart} from "../../../store/cart/cart.reducer";
import {CartService} from "../../../services/cart.service";
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/take';
import {Observable} from "rxjs/Observable";

@Component({
  selector: 'app-bank-accept',
  templateUrl: './bank-accept.component.html',
  styleUrls: ['./bank-accept.component.css']
})
export class BankAcceptComponent implements OnInit {

  @Input() bank;

  @Input() totalPrice;

  @Input() postOrdersObject: PostOrdersObject;

  @Input() postOrdersCartObject: Cart;


  constructor(public activeModal: NgbActiveModal, private store: Store<fromApp.AppState>, private router: Router,
              private cartService: CartService) {
  }

  ngOnInit() {
  }


  paymentConfirm() {
    this.cartService.confirmCart(this.postOrdersCartObject).take(1).catch(error => {
      alert("An error occurred. Operation cancelled. Please try again.");
      if (error.status === 400) {
        this.store.dispatch(new CartActions.FetchCart());
      }
      this.router.navigate(["/checkout"]);
      return Observable.throw(error);
    }).subscribe(() => {
      this.store.dispatch(new OrderActions.PostOrder(this.postOrdersObject));
    });
    this.activeModal.close('Close click');
  }
}

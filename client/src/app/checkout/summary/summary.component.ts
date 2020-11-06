import { TermsComponent } from './terms/terms.component';
import { CartState } from './../../store/cart/cart.reducer';
import { Component, OnInit, OnDestroy } from '@angular/core';
import * as fromApp from '../../store/app.reducers';
import { Store } from '@ngrx/store';
import { Observable, Subscription } from 'rxjs';
import { OrderState } from 'src/app/store/order/order.reducer';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { BankAcceptComponent } from './bank-accept/bank-accept.component';
import { NavigationStart, Router } from '@angular/router';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-summary',
  templateUrl: './summary.component.html',
  styleUrls: ['./summary.component.scss']
})
export class SummaryComponent implements OnInit, OnDestroy {

  cartState: Observable<CartState>;
  orderState: Observable<OrderState>;
  termsAccepted = false;

  routerSubscription: Subscription;

  constructor(private store: Store<fromApp.AppState>, private modalService: NgbModal, private router: Router) {
  }

  ngOnInit() {
    this.cartState = this.store.select('cart');
    this.orderState = this.store.select('order');

    this.routerSubscription = this.router.events.pipe(filter(event => event instanceof NavigationStart)).subscribe(event => { this.termsAccepted = false; });
  }

  ngOnDestroy() {
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
  }

  openBankModal() {
    this.modalService.open(BankAcceptComponent, {
      backdrop: 'static',
      keyboard: false,
      centered: true
    });
  }

  openTermsModel() {
    this.modalService.open(TermsComponent, {
      backdrop: 'static',
      keyboard: false,
      centered: true
    });
  }
}

import {Component, OnInit} from '@angular/core';
import {Subscription, throwError} from "rxjs";
import {Orders} from "../../store/order/order.reducer";
import {OrderService} from "../../services/order.service";
import {catchError, take} from "rxjs/operators";

@Component({
  selector: 'app-list-orders',
  templateUrl: './list-orders.component.html',
  styleUrls: ['./list-orders.component.css']
})
export class ListOrdersComponent implements OnInit {

  orders: Orders[];
  fetchError: boolean = false;
  noOrders: boolean = false;
  page: number = 1;
  pageSize: number = 3;
  collectionSize: number = 0;

  fetchSubscription: Subscription;

  inlineLoading: boolean = true;

  constructor(private orderService: OrderService) {
  }

  ngOnInit() {
    this.orderService.getAllOrdersCount()
      .pipe(take(1), catchError(
        error => {
          alert("An error occurred loading orders. Please refresh your page.");
          return throwError(error);
        }
      ))
      .subscribe(data => {
        if (data == 0) {
          this.noOrders = true;
          this.inlineLoading = false;
        } else {
          this.pageSize = this.orderService.getPageSize();
          this.collectionSize = data;
          this.pageNavigation();
        }
      });

  }

  pageNavigation() {
    this.orders = [];
    this.inlineLoading = true;
    if (this.fetchSubscription != null) {
      this.fetchSubscription.unsubscribe();
    }
    this.fetchSubscription = this.orderService.getAllOrders(this.page - 1)
      .pipe(catchError(
        error => {
          this.fetchError = true;
          this.inlineLoading = false;
          return throwError("");
        }
      ))
      .subscribe(data => {
        this.orders = data;
        this.inlineLoading = false;
      });
  }
}

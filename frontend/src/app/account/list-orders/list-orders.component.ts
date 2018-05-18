import {Component, OnInit} from '@angular/core';
import {Observable} from "rxjs/Observable";
import {Orders} from "../../store/order/order.reducer";
import {OrderService} from "../../services/order.service";
import 'rxjs/add/operator/take';
import 'rxjs/add/operator/catch';
import {Subscription} from "rxjs/Subscription";

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
      .take(1)
      .catch(error => {
        alert("An error occurred loading orders. Please refresh your page.");
        return Observable.throw(error);
      })
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
    console.log(this.page);

    this.orders = [];
    this.inlineLoading = true;
    if (this.fetchSubscription != null) {
      this.fetchSubscription.unsubscribe();
    }
    this.fetchSubscription = this.orderService.getAllOrders(this.page - 1)
      .catch(error => {
        this.fetchError = true;
        this.inlineLoading = false;
        return Observable.throw("");
      })
      .subscribe(data => {
        this.orders = data;
        this.inlineLoading = false;
      });
  }
}

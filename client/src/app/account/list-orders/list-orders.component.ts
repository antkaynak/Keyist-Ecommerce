import { Orders } from './../../store/model';
import { Component, OnInit } from '@angular/core';
import { throwError } from 'rxjs';
import { OrderService } from '../../services/order.service';
import { catchError, take } from 'rxjs/operators';
import { Router } from '@angular/router';
import { getLocaleDate } from 'src/utils/date';

@Component({
  selector: 'app-list-orders',
  templateUrl: './list-orders.component.html',
  styleUrls: ['./list-orders.component.scss']
})
export class ListOrdersComponent implements OnInit {

  orders: Array<Orders>;
  fetchError = false;
  noOrders = false;
  page = 1;
  pageSize = 3;
  collectionSize = 0;

  innerLoading = true;

  constructor(private orderService: OrderService, private router: Router) {
  }

  ngOnInit() {
    this.orderService.getAllOrdersCount()
      .pipe(take(1), catchError(
        error => {
          alert('An error occurred loading orders. Please refresh your page.');
          return throwError(error);
        }
      ))
      .subscribe((data: number) => {
        if (data === 0) {
          this.noOrders = true;
          this.innerLoading = false;
        } else {
          this.pageSize = this.orderService.getPageSize();
          this.collectionSize = data;
          this.pageNavigation();
        }
      });

  }

  convertDate(date: number) {
    return getLocaleDate(date);
  }

  pageNavigation() {
    this.orders = [];
    this.innerLoading = true;
    this.orderService.getAllOrders(this.page - 1)
      .pipe(take(1), catchError(
        error => {
          this.fetchError = true;
          this.innerLoading = false;
          return throwError(error);
        }
      ))
      .subscribe(data => {
        this.orders = data;
        this.innerLoading = false;
      });
  }

  goToItem(productUrl) {
    this.router.navigate(['/detail/', productUrl]);
  }
}

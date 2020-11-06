import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { config } from '../../config/local';
import { Orders, Checkout } from '../store/model';


@Injectable()
export class OrderService {

  url = `${config.apiUrl}/api/order`;
  private pageSize = 3;

  constructor(private httpClient: HttpClient) {
  }

  getAllOrdersCount() {
    return this.httpClient.get<number>(this.url + '/count');
  }

  getAllOrders(page: number) {
    let params = new HttpParams();
    params = params.set('page', page.toString());
    params = params.set('size', this.pageSize.toString());
    return this.httpClient.get<Array<Orders>>(this.url, {
      params
    });
  }

  postOrder(data: Checkout) {
    return this.httpClient.post<Orders>(this.url, data);
  }

  getPageSize() {
    return this.pageSize;
  }

}

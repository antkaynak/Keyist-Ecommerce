import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Cart} from "../store/cart/cart.reducer";


@Injectable()
export class CartService {

  securedUrl: string = 'backendurl/api/secured/cart';

  constructor(private httpClient: HttpClient) {
  }

  getCart() {
    return this.httpClient.get<Cart>(this.securedUrl);
  }

  postCart(productId: number, amount: string) {
    return this.httpClient.post<Cart>(this.securedUrl, {
      productId: productId,
      amount: amount
    });
  }

  removeFromCart(id: number) {
    return this.httpClient.delete<Cart>(this.securedUrl, {
      params: new HttpParams().set('id', id.toString())
    })
  }

  confirmCart(cart: Cart) {
    return this.httpClient.post(this.securedUrl + '/confirm', cart);
  }

  applyDiscount(code: string) {
    return this.httpClient.get<Cart>('backendurl/api/secured/cart/discount', {
      params: new HttpParams().set('code', code)
    });
  }

  emptyCart() {
    return this.httpClient.delete(this.securedUrl);
  }


}

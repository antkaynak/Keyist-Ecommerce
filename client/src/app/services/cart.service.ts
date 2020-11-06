import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { config } from '../../config/local';
import { Cart } from '../store/model';


@Injectable()
export class CartService {

  url = `${config.apiUrl}/api/cart`;

  constructor(private httpClient: HttpClient) {
  }

  getCart() {
    return this.httpClient.get<Cart>(this.url);
  }

  postCart(productVariantId: number, amount: string) {
    return this.httpClient.post<Cart>(this.url, {
      productVariantId,
      amount
    });
  }

  incrementCartItem(cartItemId: number, amount: string) {
    return this.httpClient.post<Cart>(`${this.url}/increment`, {
      cartItemId,
      amount: Number(amount)
    });
  }

  decrementCartItem(cartItemId: number, amount: string) {
    return this.httpClient.post<Cart>(`${this.url}/decrement`, {
      cartItemId,
      amount: Number(amount)
    });
  }

  removeFromCart(id: number) {
    return this.httpClient.post<Cart>(this.url + '/remove', {
      cartItemId: id
    });
  }

  confirmCart(cart: Cart) {
    return this.httpClient.post(this.url + '/confirm', cart);
  }

  applyDiscount(code: string) {
    return this.httpClient.post<Cart>(`${this.url}/discount`, {
      code
    });
  }

  emptyCart() {
    return this.httpClient.delete(this.url);
  }


}

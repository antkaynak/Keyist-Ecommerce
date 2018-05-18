import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Product, ProductDisplay} from "../store/cart/cart.reducer";


@Injectable()
export class ProductService {

  url: string = 'backendurl/api/product';
  categoryUrl: string = 'backendurl/api/category';

  browsePageSize: number = 20;
  searchPageSize: number = 10;

  constructor(private httpClient: HttpClient) {
  }

  getProducts(page: number, sort: string, category: string) {
    if (page === undefined && page === null && page < 0) {
      return;
    }
    let params = new HttpParams();
    params = params.set('page', page.toString());
    params = params.set('size', this.browsePageSize.toString());
    if (sort !== undefined && sort !== null && sort != 'any') {
      params = params.set('sort', sort);
    }
    if (category !== undefined && category !== null && category != 'any') {
      params = params.set('category', category);
    }

    return this.httpClient.get<Object[]>(this.url,
      {
        params: params
      });
  }

  getFullProduct(id: number) {
    return this.httpClient.get<Product>(this.url,
      {
        params: new HttpParams().set('id', id.toString())
      });
  }

  getRelatedProducts(id: number) {
    return this.httpClient.get<ProductDisplay[]>(this.url + '/related',
      {
        params: new HttpParams().set('id', id.toString())
      });
  }

  getNewlyAdded() {
    return this.httpClient.get<ProductDisplay[]>(this.url + '/recent');
  }

  getMostSelling() {
    return this.httpClient.get<ProductDisplay[]>(this.url + '/mostselling');
  }

  getInterested() {
    return this.httpClient.get<ProductDisplay[]>(this.url + '/interested');
  }

  searchProduct(page: number, keyword: string) {
    let params = new HttpParams();
    params = params.append('page', page.toString());
    params = params.append('keyword', keyword);
    params = params.set('size', this.searchPageSize.toString());
    return this.httpClient.get<ProductDisplay[]>(this.url + '/search', {
      params: params
    })
  }

  getCategory() {
    return this.httpClient.get<Object[]>(this.categoryUrl);
  }

}

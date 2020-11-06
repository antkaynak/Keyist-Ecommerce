import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { config } from '../../config/local';
import { ProductDetail, Product, ProductVariantResponse, Category, Colors } from '../store/model';

@Injectable()
export class ProductService {

  publicUrl = `${config.apiUrl}/api/public/product`;
  categoryUrl = `${config.apiUrl}/api/public/category`;
  colorUrl = `${config.apiUrl}/api/public/colors`;

  browsePageSize = 20;
  searchPageSize = 10;

  constructor(private httpClient: HttpClient) {
  }

  getProducts(page: number, sort: string, category: string, color: string, minPrice: string, maxPrice: string) {
    if (page === undefined && page === null && page < 0) {
      return;
    }
    let params = new HttpParams();
    params = params.set('page', page.toString());
    params = params.set('size', this.browsePageSize.toString());
    if (sort !== undefined && sort !== null && sort !== 'any') {
      params = params.set('sort', sort);
    }
    if (category && category !== 'any') {
      params = params.set('category', category);
    }

    if (color && color !== 'any') {
      params = params.set('color', color);
    }

    if (minPrice && minPrice !== '0') {
      params = params.set('minPrice', minPrice);
    }

    if (maxPrice && maxPrice !== '0') {
      params = params.set('maxPrice', maxPrice);
    }

    return this.httpClient.get<Object[]>(this.publicUrl,
      {
        params
      });
  }

  getProductsCount(category: string, color: string, minPrice: string, maxPrice: string) {
    let params = new HttpParams();
    if (category && category !== 'any') {
      params = params.set('category', category);
    }

    if (color && color !== 'any') {
      params = params.set('color', color);
    }

    if (minPrice && minPrice !== '0') {
      params = params.set('minPrice', minPrice);
    }

    if (maxPrice && maxPrice !== '0') {
      params = params.set('maxPrice', maxPrice);
    }

    return this.httpClient.get<number>(`${this.publicUrl}/count`,
      {
        params
      });
  }

  getFullProduct(productUrl: string) {
    return this.httpClient.get<ProductDetail>(`${this.publicUrl}/${productUrl}`);
  }

  getRelatedProducts(productUrl: string) {
    return this.httpClient.get<Array<Product>>(`${this.publicUrl}/related/${productUrl}`);
  }

  getNewlyAdded() {
    return this.httpClient.get<Array<Product>>(this.publicUrl + '/recent');
  }

  getMostSelling() {
    return this.httpClient.get<Array<ProductVariantResponse>>(this.publicUrl + '/mostselling');
  }

  getInterested() {
    return this.httpClient.get<Array<Product>>(this.publicUrl + '/interested');
  }

  searchProduct(page: number, keyword: string) {
    let params = new HttpParams();
    params = params.append('page', page.toString());
    params = params.append('keyword', keyword);
    params = params.set('size', this.searchPageSize.toString());
    return this.httpClient.get<Array<Product>>(this.publicUrl + '/search', {
      params
    });
  }

  getCategory() {
    return this.httpClient.get<Array<Category>>(this.categoryUrl);
  }

  getColors() {
    return this.httpClient.get<Array<Colors>>(this.colorUrl);
  }

}

import { Product } from './../store/model';
import { Component, HostListener, OnInit } from '@angular/core';
import { ProductService } from '../services/product.service';
import { ActivatedRoute, Params } from '@angular/router';
import { Subscription, throwError } from 'rxjs';
import { catchError, take } from 'rxjs/operators';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {

  querySubscribe: Subscription;
  page = 0;
  keyword: string;
  canFetch = false;

  products: Array<Product> = [];

  constructor(private productService: ProductService, private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.querySubscribe = this.route.params.subscribe((params: Params) => {
      this.canFetch = false;
      this.keyword = params.keyword;
      this.page = 0;
      this.productService.searchProduct(this.page, this.keyword)
        .pipe(take(1), catchError(
          error => {
            this.canFetch = false;
            return throwError(error);
          }
        ))
        .subscribe(data => {
          this.products = data;
          this.page++;
          this.canFetch = true;
          if (data.length !== 0) {
            this.canFetch = true;
          }
        });
    });
  }

  @HostListener('window:scroll', ['$event'])
  onScroll($event: Event): void {
    if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight && this.canFetch) {
      this.canFetch = false;
      if (this.canFetch) {
        this.productService.searchProduct(this.page, this.keyword)
          .pipe(take(1), catchError(
            error => {
              this.canFetch = false;
              return throwError(error);
            }
          ))
          .subscribe(data => {
            this.products.push(...data);
            this.page++;
            this.canFetch = true;
            if (data.length === 0) {
              this.canFetch = false;
            }
          });
      }
    }
  }
}

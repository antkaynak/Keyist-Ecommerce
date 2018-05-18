import {Component, HostListener, OnInit} from '@angular/core';
import {ProductService} from "../services/product.service";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {Subscription} from "rxjs/Subscription";
import 'rxjs/add/operator/take';
import 'rxjs/add/operator/catch';
import {ProductDisplay} from "../store/cart/cart.reducer";
import {Observable} from "rxjs/Observable";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  querySubscribe: Subscription;
  page: number = 0;
  keyword: string;
  canFetch: boolean = false;

  products: ProductDisplay[] = [];

  constructor(private productService: ProductService, private router: Router, private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.querySubscribe = this.route.params.subscribe((params: Params) => {
      this.keyword = params['keyword'];
      this.productService.searchProduct(this.page, this.keyword)
        .take(1)
        .catch(error => {
          this.canFetch = false;
          return Observable.throw(error);
        })
        .subscribe(data => {
          this.products = data;
          this.page++;
          if (data.length != 0) {
            this.canFetch = true;
          }
        });
    });
  }

  @HostListener('window:scroll', ['$event'])
  onScroll($event: Event): void {
    if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight) {
      console.log(this.canFetch);
      if (this.canFetch) {
        this.productService.searchProduct(this.page, this.keyword)
          .take(1)
          .catch(error => {
            this.canFetch = false;
            return Observable.throw(error);
          })
          .subscribe(data => {
            this.products.push(...data);
            this.page++;
            if (data.length == 0) {
              this.canFetch = false;
            }
          });
      }
    }
  }


}

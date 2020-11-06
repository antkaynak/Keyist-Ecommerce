import { Product } from './../../store/model';
import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { Subscription, throwError } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { catchError, take } from 'rxjs/operators';

@Component({
  selector: 'app-related',
  templateUrl: './related.component.html',
  styleUrls: ['./related.component.scss']
})
export class RelatedComponent implements OnInit, OnDestroy {

  @ViewChild('relatedCards') relatedCards: ElementRef;

  paramSubscription: Subscription;

  relatedProducts: Array<Product>;
  fetchError: HttpErrorResponse = null;
  productUrl: string;

  innerLoading = true;

  constructor(private route: ActivatedRoute, private productService: ProductService) {
  }

  ngOnInit() {
    this.paramSubscription = this.route.params.subscribe(
      (params: Params) => {

        this.innerLoading = true;
        this.productUrl = params.productUrl;
        this.productService.getRelatedProducts(this.productUrl)
          .pipe(take(1), catchError(
            error => {
              this.fetchError = error;
              this.innerLoading = false;
              return throwError(error);
            }
          ))
          .subscribe(
            (data: Array<Product>) => {
              this.relatedProducts = data;
              this.innerLoading = false;
            });
      }
    );
  }

  ngOnDestroy() {
    if (this.paramSubscription) {
      this.paramSubscription.unsubscribe();
    }
  }

  scrollLeft() {
    this.relatedCards.nativeElement.scrollLeft -= 250;
  }

  scrollRight() {
    this.relatedCards.nativeElement.scrollLeft += 250;
  }

}

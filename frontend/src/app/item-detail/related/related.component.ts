import {Component, OnDestroy, OnInit} from '@angular/core';
import {ProductDisplay} from "../../store/cart/cart.reducer";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {ProductService} from "../../services/product.service";
import {Subscription, throwError} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";
import {catchError, take} from "rxjs/operators";

@Component({
  selector: 'app-related',
  templateUrl: './related.component.html',
  styleUrls: ['./related.component.css']
})
export class RelatedComponent implements OnInit, OnDestroy {

  paramSubscription: Subscription;

  relatedProducts: ProductDisplay[];
  fetchError: HttpErrorResponse = null;
  id: number;

  innerLoading: boolean = true;

  constructor(private router: Router, private route: ActivatedRoute, private productService: ProductService) {
  }

  ngOnInit() {
    this.paramSubscription = this.route.params.subscribe(
      (params: Params) => {

        this.id = params['id'];
        this.productService.getRelatedProducts(this.id)
          .pipe(take(1), catchError(
            error => {
              this.fetchError = error;
              this.innerLoading = false;
              return throwError(error);
            }
          ))
          .subscribe(
            (data: ProductDisplay[]) => {
              this.relatedProducts = data;
              this.innerLoading = false;
            }
          )

      }
    );
  }

  ngOnDestroy() {
    if (this.paramSubscription != null) {
      this.paramSubscription.unsubscribe();
    }
  }
}

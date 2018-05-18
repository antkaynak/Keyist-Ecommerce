import {Component, OnDestroy, OnInit} from '@angular/core';
import {ProductDisplay} from "../../store/cart/cart.reducer";
import {Subscription} from "rxjs/Subscription";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {ProductService} from "../../services/product.service";
import {Observable} from "rxjs/Observable";
import 'rxjs/add/operator/take';
import 'rxjs/add/operator/catch';
import {HttpErrorResponse} from "@angular/common/http";

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
          .take(1)
          .catch(error => {
            this.fetchError = error;
            this.innerLoading = false;
            return Observable.throw(error);
          })
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

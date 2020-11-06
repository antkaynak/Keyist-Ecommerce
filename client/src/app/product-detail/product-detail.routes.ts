import { Routes } from '@angular/router';
import { ProductDetailComponent } from './product-detail.component';


export const ProductDetailRoutes: Routes = [
  { path: 'detail/:productUrl', component: ProductDetailComponent },
  { path: 'detail/:productUrl/:variant', component: ProductDetailComponent }
];


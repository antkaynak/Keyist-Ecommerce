import {Routes} from "@angular/router";
import {ItemDetailComponent} from "./item-detail.component";


export const ItemDetailRoutes: Routes = [
  {path: 'detail/:id', component: ItemDetailComponent}
];


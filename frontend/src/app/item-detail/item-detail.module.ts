import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ItemDetailComponent} from "./item-detail.component";
import {RouterModule} from "@angular/router";
import {ItemDetailRoutes} from "./item-detail.routes";
import {RelatedComponent} from './related/related.component';
import {ReactiveFormsModule} from "@angular/forms";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(ItemDetailRoutes),
    ReactiveFormsModule,
    NgbModule
  ],
  declarations: [ItemDetailComponent, RelatedComponent],
  providers: []
})
export class ItemDetailModule {
}

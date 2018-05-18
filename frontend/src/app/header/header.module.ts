import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HeaderComponent} from './header.component';
import {SearchbarComponent} from './searchbar/searchbar.component';
import {RouterModule} from "@angular/router";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    NgbModule
  ],
  declarations: [HeaderComponent, SearchbarComponent],
  exports: [HeaderComponent]
})
export class HeaderModule {
}

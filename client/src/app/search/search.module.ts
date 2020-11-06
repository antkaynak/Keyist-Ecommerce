import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SearchComponent } from './search.component';
import { RouterModule } from '@angular/router';
import { SearchRoutes } from './search.routes';

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(SearchRoutes)
  ],
  declarations: [SearchComponent]
})
export class SearchModule {
}

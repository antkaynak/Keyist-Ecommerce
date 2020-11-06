import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FaqComponent } from './faq.component';
import { FaqRoutes } from './faq.routes';


@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(FaqRoutes),
    NgbModule
  ],
  declarations: [FaqComponent]
})
export class FaqModule {
}

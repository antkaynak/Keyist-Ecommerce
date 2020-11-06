import { BannerComponent } from './banner/banner.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home.component';
import { RouterModule } from '@angular/router';
import { HomeRoutes } from './home.routes';
import { NewlyAddedComponent } from './newly-added/newly-added.component';
import { MostSellingComponent } from './most-selling/most-selling.component';
import { ImageSliderComponent } from './image-slider/image-slider.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(HomeRoutes),
    NgbModule
  ],
  declarations: [
    HomeComponent,
    NewlyAddedComponent,
    MostSellingComponent,
    ImageSliderComponent,
    BannerComponent
  ]
})
export class HomeModule {
}

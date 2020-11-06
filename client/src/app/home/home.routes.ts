import { HomeComponent } from './home.component';
import { Routes } from '@angular/router';

export const HomeRoutes: Routes = [
  { path: '', component: HomeComponent, pathMatch: 'full' }
];

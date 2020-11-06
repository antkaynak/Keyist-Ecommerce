import { Routes } from '@angular/router';
import { FaqComponent } from './faq.component';


export const FaqRoutes: Routes = [
  { path: '', component: FaqComponent } // default because it is lazy loading
];

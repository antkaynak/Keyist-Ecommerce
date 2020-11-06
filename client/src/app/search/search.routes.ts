import { Routes } from '@angular/router';
import { SearchComponent } from './search.component';

export const SearchRoutes: Routes = [
  { path: 'search/:keyword', component: SearchComponent }
];

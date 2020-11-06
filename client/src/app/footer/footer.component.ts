import { Component, OnInit } from '@angular/core';
import * as fromApp from '../store/app.reducers';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {

  authState: Observable<{ authenticated: boolean, isActive: boolean }>;

  constructor(private store: Store<fromApp.AppState>, ) {
  }

  ngOnInit() {
    this.authState = this.store.select('auth');
  }

}

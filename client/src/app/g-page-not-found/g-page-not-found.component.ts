import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Data } from '@angular/router';

@Component({
  selector: 'app-g-page-not-found',
  templateUrl: './g-page-not-found.component.html',
  styleUrls: ['./g-page-not-found.component.scss']
})
export class GPageNotFoundComponent implements OnInit {

  errorMessage: string;

  constructor(private route: ActivatedRoute) {
  }

  ngOnInit() {
    // this.errorMessage = this.route.snapshot.data['message'];
    this.route.data.subscribe(
      (data: Data) =>
        this.errorMessage = data.message
    );
  }

}

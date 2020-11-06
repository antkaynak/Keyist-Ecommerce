import { config } from './../../../config/local';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-banner',
  templateUrl: './banner.component.html',
  styleUrls: ['./banner.component.scss']
})
export class BannerComponent implements OnInit {

  bannerUrl: string;

  constructor() {
  }

  ngOnInit() {
    this.bannerUrl = config.bannerUrl;
  }
}

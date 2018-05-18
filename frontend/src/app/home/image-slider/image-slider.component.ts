import {Component, OnInit} from '@angular/core';
import {NgbCarouselConfig} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-image-slider',
  templateUrl: './image-slider.component.html',
  styleUrls: ['./image-slider.component.css'],
  providers: [NgbCarouselConfig]
})
export class ImageSliderComponent implements OnInit {

  images = [
    'https://images.pexels.com/photos/941947/pexels-photo-941947.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260',
    'https://images.pexels.com/photos/101808/pexels-photo-101808.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260',
    'https://images.pexels.com/photos/533141/pexels-photo-533141.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260s'
  ];

  constructor(config: NgbCarouselConfig) {
    config.interval = 10000;
    config.wrap = true;
    config.keyboard = false;
  }

  ngOnInit() {
  }

}

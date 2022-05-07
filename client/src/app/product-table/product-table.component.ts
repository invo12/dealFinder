import {Component, Input, OnInit} from '@angular/core';
import {Product} from "../model/product";

@Component({
  selector: 'app-product-table',
  templateUrl: './product-table.component.html',
  styleUrls: ['./product-table.component.css']
})
export class ProductTableComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

  @Input() products: Product[] | undefined;

  goToWebsite(url: string) {
    window.open(url, '_blank');
  }
}
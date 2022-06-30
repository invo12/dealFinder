import {Component, Input, OnInit} from '@angular/core';
import {Product} from "../model/product";
import {Sort} from '@angular/material/sort';
import {MatTableDataSource} from "@angular/material/table";

@Component({
  selector: 'app-product-table',
  templateUrl: './product-table.component.html',
  styleUrls: ['./product-table.component.css']
})
export class ProductTableComponent implements OnInit {

  constructor() {
    this.products = []
  }

  ngOnInit(): void {
    this.dataSource = new MatTableDataSource<Product>(this.products)
    this.dataSource.filterPredicate = function (record, filter) {
      return record.name.toLowerCase().includes(filter.toLowerCase());
    }
  }

  @Input() products: Product[];
  columnsToDisplay = ['name', 'oldPrice', 'price'];
  dataSource = new MatTableDataSource<Product>()

  goToWebsite(url: string) {
    window.open(url, '_blank');
  }

  search(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  sortData(sort: Sort) {
    if (this.products != null) {
      const data = this.products.slice();
      if (!sort.active || sort.direction === '') {
        this.products = data;
        return;
      }

      this.products = data.sort((a, b) => {
        const isAsc = sort.direction === 'asc';
        switch (sort.active) {
          case 'name':
            return compare(a.name, b.name, isAsc);
          case 'oldPrice':
            return compare(a.oldPrice, b.oldPrice, isAsc);
          case 'price':
            return compare(a.price, b.price, isAsc);
          default:
            return 0;
        }
      });
    }

    function compare(a: number | string, b: number | string, isAsc: boolean) {
      return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
    }
  }
}

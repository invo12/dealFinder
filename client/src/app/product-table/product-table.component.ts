import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {Product} from "../model/product";
import {Sort} from '@angular/material/sort';
import {MatTableDataSource} from "@angular/material/table";
import {Utils} from "../Utils";

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

      let showReduced = filter.includes("reduced")
      if (showReduced) {
        return record.name.toLowerCase().includes(filter.slice(7).toLowerCase())
          && record.price < record.oldPrice;
      }
      return record.name.toLowerCase().includes(filter.toLowerCase())
    }
  }

  @Input() products: Product[];
  columnsToDisplay = ['name', 'oldPrice', 'price'];
  dataSource = new MatTableDataSource<Product>()
  showOnlyReduced = false
  @ViewChild("link", {static: true}) input!: ElementRef;
  @ViewChild("searchTable", {static: true}) searchTable!: ElementRef;

  addProductToTable(product: string) {

    try {
      this.products = Utils.addProduct(this.products, Utils.parseProduct(product));
      if (this.dataSource.data.length != this.products.length) {
        localStorage.setItem('products', JSON.stringify(this.products));
        this.dataSource.data = this.products;
      }

      this.input.nativeElement.value = "";
    } catch (e) {
      this.input.nativeElement.value = "Invalid product";
    }
  }

  clear() {
    if (this.input.nativeElement.value === "Invalid product")
      this.input.nativeElement.value = "";
  }

  goToWebsite(url: string) {
    window.open(url, '_blank');
  }

  search(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    const showReduced = this.showOnlyReduced ? "reduced" : ""
    this.dataSource.filter = showReduced + filterValue.trim().toLowerCase();
  }

  showReducedItems(show: boolean) {
    this.showOnlyReduced = show
    if (show && !this.dataSource.filter.includes("reduced")) {
      this.dataSource.filter = "reduced" + this.dataSource.filter;
    } else {
      this.dataSource.filter = this.searchTable.nativeElement.value
    }
  }

  addProduct() {

    const link = this.input.nativeElement.value;
    if(this.products.filter(it => it.url === link).length !== 0) {
      this.input.nativeElement.value = "";
      return;
    }

    const myHeaders = new Headers();
    myHeaders.append("Content-Type", "text/plain");

    const requestOptions = {
      method: 'POST',
      headers: myHeaders,
      redirect: undefined
    };

    const userId = localStorage.getItem('userId')

    fetch("http://localhost:8080/products/users/" + userId + "?websiteLink=" + link, requestOptions)
      .then(response => response.text())
      .then(result => this.addProductToTable(result))
      .catch(error => console.log('error', error));
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

  removeElement(url: string) {

    this.products = Utils.removeProduct(this.products, url);
    if (this.products.length != this.dataSource.data.length) {

      this.dataSource.data = this.products;
      localStorage.setItem('products', JSON.stringify(this.products));
      const requestOptions = {
        method: 'DELETE',
        redirect: undefined
      };

      const userId = localStorage.getItem('userId');

      fetch("http://localhost:8080/products/users/" + userId + "?websiteLink=" + url, requestOptions)
        .then(response => response.text())
        .then(result => console.log(result))
        .catch(error => console.log('error', error));
    }
  }
}

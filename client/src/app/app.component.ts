import {Component} from '@angular/core';
import {Product} from "./model/product";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'client';
  message = "";
  haveData = false
  products: Product[] = []

  getProducts() {

    const myHeaders = new Headers();
    myHeaders.append("Content-Type", "text/plain");

    const requestOptions = {
      method: 'GET',
      headers: myHeaders,
      redirect: undefined
    };

    fetch("http://localhost:8080/products", requestOptions)
      .then(response => response.text())
      .then(result => this.parseData(result))
      .catch(error => console.log('error', error));
  }

  parseData(result: string){

    this.products = JSON.parse(result) as Product[]
    this.haveData = true
  }
}

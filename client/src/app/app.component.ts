import {Component} from '@angular/core';
import {Product} from "./model/product";
import {Utils} from "./Utils";

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

  ngOnInit() {

    if (localStorage.getItem('userId') !== null) {
      this.haveData = true;
      this.parseData(localStorage.getItem('products')!)
    }
  }

  getUserId() {
    var requestOptions1 = {
      method: 'GET',
      redirect: undefined
    };

    return fetch("http://localhost:8080/users?username=invo", requestOptions1)

  }

  getUserData() {

    this.getUserId().then(response => response.text())
      .then(result => { localStorage.setItem('userId', result); this.getProducts();})
      .catch(error => console.log('error', error));

  }

  getProducts() {

    const myHeaders = new Headers();
    myHeaders.append("Content-Type", "text/plain");

    const requestOptions = {
      method: 'GET',
      headers: myHeaders,
      redirect: undefined
    };

    const userId = localStorage.getItem('userId');

    fetch("http://localhost:8080/products/users/" + userId, requestOptions)
      .then(response => response.text())
      .then(result => {
        localStorage.setItem('products', result);
        this.parseData(result);
      })
      .catch(error => console.log('error', error));
  }

  parseData(products: string) {

    this.products = (JSON.parse(products) as Product[]).map(p => Utils.correctProduct(p));
    this.haveData = true;
    document.getElementsByTagName("button")[0].remove();
  }

}

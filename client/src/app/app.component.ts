import {Component, ElementRef, ViewChild} from '@angular/core';
import {Product} from "./model/product";
import {Utils} from "./Utils";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  haveData = false
  products: Product[] = []
  @ViewChild("username", {static: false}) usernameInput!: ElementRef;

  ngOnInit() {

    this.haveData = false;
    if (localStorage.getItem('userId') !== null) {
      this.haveData = true;
      this.parseData(localStorage.getItem('products')!)
    }
  }

  ngAfterViewInit() {
    console.log('on after view init', this.usernameInput);
    // this returns null
  }

  clear() {
    this.usernameInput.nativeElement.value = "";
  }

  getUserId(username: string) {

    const requestOptions1 = {
      method: 'GET',
      redirect: undefined
    };

    return fetch("http://localhost:8080/users?username=" + username, requestOptions1)

  }

  getUserData() {

    const username = this.usernameInput.nativeElement.value;
    if (username === "") {
      this.haveData = false;
      return;
    }

    this.getUserId(username).then(response => response.text())
      .then(result => {
        localStorage.setItem('userId', result);
        this.getProducts();
      })
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
  }

}

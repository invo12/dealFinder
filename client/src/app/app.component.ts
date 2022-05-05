import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'client';
  message = "";

  getProducts() {

    const myHeaders = new Headers();
    myHeaders.append("Content-Type", "text/plain");

    const raw = "https://www.thomann.de/ro/thomann_dp_26.htm";

    const requestOptions = {
      method: 'GET',
      headers: myHeaders,
      redirect: undefined
    };

    fetch("http://localhost:8080/products", requestOptions)
      .then(response => response.text())
      .then(result => this.message = result)
      .catch(error => console.log('error', error));
  }
}

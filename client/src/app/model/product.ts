export class Product {
  url: string;
  name: string;
  oldPrice: number;
  price: number;
  currency: string;

  constructor(url: string, name: string, oldPrice: number, price: number, currency: string) {

    this.url = url;
    this.name = name;
    this.oldPrice = oldPrice;
    this.price = price;
    this.currency = currency;
  }
}

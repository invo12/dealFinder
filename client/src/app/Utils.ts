import {Product} from "./model/product";

export class Utils {


  /**
   * get product from string
   */
  static parseProduct(productString: string): Product {

    let product = JSON.parse(productString) as Product;
    this.correctProduct(product)
    return product;
  }

  /**
   * if oldPrice is zero, change it to current price
   */
  static correctProduct(product: Product) {

    if (product.oldPrice == 0) {
      product.oldPrice = product.price;
    }
    return product;
  }

  /**
   * add product into list if is not already present
   */
  static addProduct(products: Product[], product: Product): Product[] {

    if (product.name !== "" && products.filter(p => p.name == product.name).length == 0) {
      return [product, ...products];
    } else if(product.name === "") {
      throw new Error("Invalid product")
    }
    return products;
  }

  static removeProduct(products: Product[], productName: string): Product[] {
    return products.filter(p => p.name !== productName);
  }
}

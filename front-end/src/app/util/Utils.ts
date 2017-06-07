import {Response} from "@angular/http";
import {Observable} from "rxjs";
import {ErrorObservable} from "rxjs/observable/ErrorObservable";

export class Utils {

  /**
   * The method to handle an error from http request.
   *
   * @param error the error.
   * @param handler the error message consumer.
   * @returns {ErrorObservable}
   */
  static handleErrorMessage(error: Response | any, handler: (value: string) => void): ErrorObservable {
    // In a real world app, you might use a remote logging infrastructure
    let errMsg: string;
    if (error instanceof Response) {
      errMsg = error.text();
    } else {
      errMsg = error.message ? error.message : error.toString();
    }
    console.error(errMsg);
    handler(errMsg);
    return Observable.throw(errMsg);
  }
}

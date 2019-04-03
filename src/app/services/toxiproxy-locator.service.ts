import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable()
export class ToxiproxyLocator {

  private _host: string;

  constructor(private http: HttpClient) {
    this.setHostBasedOnContext();
  }

  host(): string {
    return this._host;
  }

  generateBrowserBasedURL(): string {
    return this.getBrowserProtocol() + '//' + this.getBrowserHostname() + ':8474';
  }

  getBrowserHostname(): string {
    return window.location.hostname;
  }

  getBrowserProtocol(): string {
    return window.location.protocol;
  }

  verifyURL(url: string): Observable<string> {
    return this.http.get(url + '/version', {responseType: 'text'});
  }

  setHostBasedOnContext(): void {
    let hostSet = false;
    let sameHostURL = this.generateBrowserBasedURL();
    let verifyObservable = this.verifyURL(sameHostURL);

    if(verifyObservable) {
      verifyObservable.subscribe(
        () => {
          this._host = sameHostURL;
          hostSet = true;
        },
        () => {
        }
      );
    }

    if(!hostSet) {
      this._host = 'http://localhost:8474';
    }
  }
}

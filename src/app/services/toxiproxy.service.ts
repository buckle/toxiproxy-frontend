import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable()
export class ToxiproxyService {

  private readonly host: string;

  constructor(private http: HttpClient) {
    this.host = 'http://localhost:8474';
  }

  getProxyVersion(): Observable<string> {
    return this.http.get(this.host + '/version', {responseType: 'text'});
  }

  getProxies(): Observable<object> {
    return this.http.get<object>(this.host + '/proxies');
  }
}

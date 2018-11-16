import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Proxy} from './Proxy';

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

  getProxy(name: string): Observable<Proxy> {
    return this.http.get<Proxy>(this.host + '/proxies/' + name);
  }

  createProxy(proxy: Proxy): Observable<Proxy> {
    return this.http.post<Proxy>(this.host + '/proxies', proxy);
  }
}

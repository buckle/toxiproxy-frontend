import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Proxy} from './proxy';
import {Toxic} from './toxic';
import {environment} from '../../environments/environment';

@Injectable()
export class ToxiproxyService {

  private readonly _host: string;

  constructor(private http: HttpClient) {
    this._host = environment.toxiproxyHost;
  }

  getProxyVersion(): Observable<string> {
    return this.http.get(this.host + '/api/version', {responseType: 'text'});
  }

  getProxies(): Observable<object> {
    return this.http.get<object>(this.host + '/api/proxies');
  }

  getProxy(name: string): Observable<Proxy> {
    return this.http.get<Proxy>(this.host + '/api/proxies/' + name);
  }

  createProxy(proxy: Proxy): Observable<Proxy> {
    return this.http.post<Proxy>(this.host + '/api/proxies', proxy);
  }

  updateProxy(proxy: Proxy): Observable<Proxy> {
    return this.http.post<Proxy>(this.host + '/api/proxies/' + proxy.name, proxy);
  }

  deleteProxy(proxy: Proxy): Observable<object> {
    return this.http.delete(this.host + '/api/proxies/' + proxy.name);
  }

  addToxic(proxy: Proxy, toxic: Toxic): Observable<Toxic> {
    return this.http.post<Toxic>(this.host + '/api/proxies/' + proxy.name + '/toxics', toxic);
  }

  updateToxic(proxy: Proxy, toxic: Toxic): Observable<Toxic> {
    return this.http.post<Toxic>(this.host + '/api/proxies/' + proxy.name + '/toxics/' + toxic.name, toxic);
  }

  deleteToxic(proxy: Proxy, toxic: Toxic): Observable<object> {
    return this.http.delete(this.host + '/api/proxies/' + proxy.name + '/toxics/' + toxic.name);
  }

  get host(): string {
    return this._host;
  }
}

import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Proxy} from './proxy';
import {Toxic} from './toxic';

@Injectable()
export class ToxiproxyService {
  constructor(private http: HttpClient) {
  }

  getProxyVersion(): Observable<string> {
    return this.http.get('/api/service-version', {responseType: 'text'});
  }

  getProxies(): Observable<object> {
    return this.http.get<object>('/api/proxies');
  }

  getProxy(name: string): Observable<Proxy> {
    return this.http.get<Proxy>('/api/proxies/' + name);
  }

  createProxy(proxy: Proxy): Observable<Proxy> {
    return this.http.post<Proxy>('/api/proxies', proxy);
  }

  updateProxy(proxy: Proxy): Observable<Proxy> {
    return this.http.post<Proxy>('/api/proxies/' + proxy.name, proxy);
  }

  deleteProxy(proxy: Proxy): Observable<object> {
    return this.http.delete('/api/proxies/' + proxy.name);
  }

  addToxic(proxy: Proxy, toxic: Toxic): Observable<Toxic> {
    return this.http.post<Toxic>('/api/proxies/' + proxy.name + '/toxics', toxic);
  }

  updateToxic(proxy: Proxy, toxic: Toxic): Observable<Toxic> {
    return this.http.post<Toxic>('/api/proxies/' + proxy.name + '/toxics/' + toxic.name, toxic);
  }

  deleteToxic(proxy: Proxy, toxic: Toxic): Observable<object> {
    return this.http.delete('/api/proxies/' + proxy.name + '/toxics/' + toxic.name);
  }
}

import { TestBed } from '@angular/core/testing';

import { ToxiproxyService } from './toxiproxy.service';
import {HttpClient, HttpHandler} from '@angular/common/http';
import createSpyObj = jasmine.createSpyObj;
import SpyObj = jasmine.SpyObj;
import {of} from 'rxjs';
import {Proxy} from './proxy';
import {Toxic} from './toxic';

describe('ToxiproxyService', () => {

  let toxiproxyService: ToxiproxyService;
  let http: SpyObj<HttpClient>;

  beforeEach(() => {
    let httpClientSpy = createSpyObj('HttpClient', ['get', 'post', 'delete']);

    TestBed.configureTestingModule({
      providers: [
        {provide: HttpClient, useValue: httpClientSpy},
        ToxiproxyService,
        HttpHandler]
    });

    toxiproxyService = TestBed.get(ToxiproxyService);
    http = TestBed.get(HttpClient);
  });

  it('should be created', () => {
    expect(toxiproxyService).toBeTruthy();
  });

  it('should get proxy version', () => {
    http.get.and.returnValue(of('4.3.2.1'));

    let proxyVersionObservable = toxiproxyService.getProxyVersion();

    proxyVersionObservable.subscribe(version => {
      expect(version).toEqual('4.3.2.1');
    });
    expect(http.get).toHaveBeenCalledWith(toxiproxyService.getHost() + '/version', {responseType: 'text'})
  });

  it('should get proxies', () => {
    http.get.and.returnValue(of({"proxy":"test"}));

    let proxiesObservable = toxiproxyService.getProxies();

    proxiesObservable.subscribe(proxies => {
      expect(proxies).toEqual({"proxy":"test"});
    });
    expect(http.get).toHaveBeenCalledWith(toxiproxyService.getHost() + '/proxies');
  });

  it('should get proxy', () => {
    let proxy = new Proxy();
    http.get.and.returnValue(of(proxy));

    let proxyObservable = toxiproxyService.getProxy('bob');

    proxyObservable.subscribe(foundProxy => {
      expect(foundProxy).toEqual(proxy);
    });
    expect(http.get).toHaveBeenCalledWith(toxiproxyService.getHost() + '/proxies/bob');
  });

  it('should create proxy', () => {
    let proxy = new Proxy();
    http.post.and.returnValue(of(proxy));

    let proxyObservable = toxiproxyService.createProxy(proxy);

    proxyObservable.subscribe(returnedProxy => {
      expect(returnedProxy).toEqual(proxy);
    });
    expect(http.post).toHaveBeenCalledWith(toxiproxyService.getHost() + '/proxies', proxy);
  });

  it('should update proxy', () => {
    let proxy = new Proxy();
    proxy.name = 'bob';
    http.post.and.returnValue(of(proxy));

    let proxyObservable = toxiproxyService.updateProxy(proxy);

    proxyObservable.subscribe(returnedProxy => {
      expect(returnedProxy).toEqual(proxy);
    });
    expect(http.post).toHaveBeenCalledWith(toxiproxyService.getHost() + '/proxies/' + proxy.name, proxy);
  });

  it('should delete proxy', () => {
    let proxy = new Proxy();
    proxy.name = 'bob';
    http.delete.and.returnValue(of({}));

    let observable = toxiproxyService.deleteProxy(proxy);

    observable.subscribe(value => {
      expect(value).toEqual({});
    });
    expect(http.delete).toHaveBeenCalledWith(toxiproxyService.getHost() + '/proxies/' + proxy.name);
  });

  it('should add toxic', () => {
    let proxy = new Proxy();
    proxy.name = 'bob';
    let toxic = new Toxic();
    http.post.and.returnValue(of(proxy));

    let proxyObservable = toxiproxyService.addToxic(proxy, toxic);

    proxyObservable.subscribe(returnedProxy => {
      expect(returnedProxy).toEqual(proxy);
    });
    expect(http.post).toHaveBeenCalledWith(toxiproxyService.getHost() + '/proxies/' + proxy.name + '/toxics', toxic);
  });

  it('should update toxic', () => {
    let proxy = new Proxy();
    proxy.name = 'bob';
    let toxic = new Toxic();
    toxic.name = 'bob toxic';
    http.post.and.returnValue(of(proxy));

    let proxyObservable = toxiproxyService.updateToxic(proxy, toxic);

    proxyObservable.subscribe(returnedProxy => {
      expect(returnedProxy).toEqual(proxy);
    });
    expect(http.post).toHaveBeenCalledWith(toxiproxyService.getHost() + '/proxies/' + proxy.name + '/toxics/' + toxic.name, toxic);
  });

  it('should delete toxic', () => {
    let proxy = new Proxy();
    proxy.name = 'bob';
    let toxic = new Toxic();
    toxic.name = 'bob toxic';
    http.delete.and.returnValue(of({}));

    let observable = toxiproxyService.deleteToxic(proxy, toxic);

    observable.subscribe(value => {
      expect(value).toEqual({});
    });
    expect(http.delete).toHaveBeenCalledWith(toxiproxyService.getHost() + '/proxies/' + proxy.name + '/toxics/' + toxic.name);
  });
});

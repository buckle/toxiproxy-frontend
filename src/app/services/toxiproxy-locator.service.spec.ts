import {TestBed} from '@angular/core/testing';

import {ToxiproxyLocator} from './toxiproxy-locator.service';
import {HttpClient, HttpHandler} from '@angular/common/http';
import createSpyObj = jasmine.createSpyObj;
import SpyObj = jasmine.SpyObj;
import {of, throwError} from 'rxjs';

describe('ToxiproxyLocator', () => {

  let toxiproxyLocator: ToxiproxyLocator;
  let http: SpyObj<HttpClient>;

  beforeEach(() => {
    let httpClientSpy = createSpyObj('HttpClient', ['get']);

    TestBed.configureTestingModule({
      providers: [
        {provide: HttpClient, useValue: httpClientSpy},
        ToxiproxyLocator,
        HttpHandler]
    });

    toxiproxyLocator = TestBed.get(ToxiproxyLocator);
    http = TestBed.get(HttpClient);
  });

  it('should be created', () => {
    expect(toxiproxyLocator).toBeTruthy();
    expect(toxiproxyLocator.host()).toEqual('http://localhost:8474');
  });

  it('should generateBrowserBasedURL https', () => {
    spyOn(toxiproxyLocator, 'getBrowserHostname').and.returnValue('toxiproxy.domain.tld');
    spyOn(toxiproxyLocator, 'getBrowserProtocol').and.returnValue('https:');
    expect(toxiproxyLocator.generateBrowserBasedURL()).toEqual('https://toxiproxy.domain.tld:8474');
  });

  it('should generateBrowserBasedURL http', () => {
    spyOn(toxiproxyLocator, 'getBrowserHostname').and.returnValue('toxiproxy.domain.tld');
    spyOn(toxiproxyLocator, 'getBrowserProtocol').and.returnValue('http:');
    expect(toxiproxyLocator.generateBrowserBasedURL()).toEqual('http://toxiproxy.domain.tld:8474');
  });

  it('should getBrowserHostname', () => {
    let hostname = toxiproxyLocator.getBrowserHostname();
    expect(hostname).toEqual('localhost');
  });

  it('should getBrowserProtocol', () => {
    let browserProtocol = toxiproxyLocator.getBrowserProtocol();
    expect(browserProtocol).toEqual('http:');
  });

  it('should verifyURL', () => {
    http.get.and.returnValue(of({"proxy":"test"}));
    let url = 'https://localhost:8474';

    let observable = toxiproxyLocator.verifyURL(url);

    observable.subscribe(proxies => {
      expect(proxies).toBeTruthy();
    });
    expect(http.get).toHaveBeenCalledWith(url + '/version', {responseType: 'text'})
  });

  it('should setHostBasedOnContext when using browser domain', () => {
    let browserURL = 'https://somename.host.tld:8474';
    spyOn(toxiproxyLocator, 'generateBrowserBasedURL').and.returnValue(browserURL);
    spyOn(toxiproxyLocator, 'verifyURL').and.returnValue(of('some text'));

    toxiproxyLocator.setHostBasedOnContext();

    expect(toxiproxyLocator.host()).toEqual(browserURL);
  });

  it('should setHostBasedOnContext when using browser domain fails', function() {
    let browserURL = 'https://somename.host.tld:8474';
    spyOn(toxiproxyLocator, 'generateBrowserBasedURL').and.returnValue(browserURL);
    spyOn(toxiproxyLocator, 'verifyURL').and.returnValue(throwError("failed verification"));

    toxiproxyLocator.setHostBasedOnContext();

    expect(toxiproxyLocator.host()).toEqual('http://localhost:8474');
  });
});

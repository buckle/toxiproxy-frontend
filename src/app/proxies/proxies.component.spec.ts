import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ProxiesComponent} from './proxies.component';
import {
  MatDialogModule,
  MatDividerModule,
  MatFormFieldModule,
  MatIconModule,
  MatInputModule,
  MatPaginatorModule,
  MatTableModule
} from '@angular/material';
import {RouterTestingModule} from '@angular/router/testing';
import {FormsModule} from '@angular/forms';
import {ToxiproxyService} from '../services/toxiproxy.service';
import {HttpClient, HttpHandler} from '@angular/common/http';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import SpyObj = jasmine.SpyObj;
import {of} from 'rxjs';

describe('ProxiesComponent', () => {
  let component: ProxiesComponent;
  let fixture: ComponentFixture<ProxiesComponent>;
  let proxyService: SpyObj<ToxiproxyService>;

  beforeEach(async(() => {
    const toxiProxySpy = jasmine.createSpyObj('ToxiproxyService', ['getProxies']);

    TestBed.configureTestingModule({
      declarations: [ProxiesComponent],
      imports: [
        MatDividerModule,
        MatIconModule,
        MatFormFieldModule,
        MatTableModule,
        MatPaginatorModule,
        RouterTestingModule,
        FormsModule,
        MatDialogModule,
        MatInputModule,
        NoopAnimationsModule
      ],
      providers: [
        {provide: ToxiproxyService, useValue: toxiProxySpy},
        HttpClient,
        HttpHandler
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProxiesComponent);
    component = fixture.componentInstance;
    proxyService = TestBed.get(ToxiproxyService);

  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should init data', () => {
    let proxies = new Object();
    proxies['BarkerProxy'] = {
      "name": "BarkerProxy",
        "listen": "localhost:5002",
        "upstream": "mbp-stacyp.bkedev.com:5003",
        "enabled": false,
        "toxics": []
    };
    proxies['Bobs Burgers'] = {
      "name": "Bobs Burgers",
      "listen": "127.0.0.1:7779",
      "upstream": "mbp-stacyp.bkedev.com:8000",
      "enabled": true,
      "toxics": []
    };
    proxyService.getProxies.and.returnValue(of(proxies));
    fixture.detectChanges();

    expect(component.pageSize).toBe(25);
    expect(component.pageIndex).toBe(0);
    expect(component.totalItems).toBe(2);
    expect(component.proxies.data.filter(proxy => {proxy.name === 'BarkerProxy'})).toBeTruthy();
    expect(component.proxies.data.filter(proxy => {proxy.name === 'Bobs Burgers'})).toBeTruthy();
  });
});

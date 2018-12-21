import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ProxiesComponent} from './proxies.component';
import {
  MatDialogModule,
  MatDividerModule,
  MatFormFieldModule,
  MatIconModule,
  MatInputModule,
  MatPaginatorModule,
  MatProgressSpinnerModule,
  MatRadioModule,
  MatSnackBarModule,
  MatSort,
  MatTableDataSource,
  MatTableModule,
  PageEvent
} from '@angular/material';
import {RouterTestingModule} from '@angular/router/testing';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {ToxiproxyService} from '../services/toxiproxy.service';
import {HttpClient, HttpHandler} from '@angular/common/http';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import {of} from 'rxjs';
import {By} from '@angular/platform-browser';
import {BrowserDynamicTestingModule} from '@angular/platform-browser-dynamic/testing';
import {ProxyCreateDialogComponent} from './proxy-create-dialog/proxy-create-dialog.component';
import SpyObj = jasmine.SpyObj;

describe('ProxiesComponent', () => {
  let component: ProxiesComponent;
  let fixture: ComponentFixture<ProxiesComponent>;
  let proxyService: SpyObj<ToxiproxyService>;
  let proxies;

  beforeEach(async(() => {
    const toxiProxySpy = jasmine.createSpyObj('ToxiproxyService', ['getProxies']);

    TestBed.configureTestingModule({
      declarations: [ProxiesComponent, ProxyCreateDialogComponent],
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
        NoopAnimationsModule,
        ReactiveFormsModule,
        MatRadioModule,
        MatProgressSpinnerModule,
        MatSnackBarModule
      ],
      providers: [
        {provide: ToxiproxyService, useValue: toxiProxySpy},
        HttpClient,
        HttpHandler
      ]
    }).overrideModule(BrowserDynamicTestingModule, {
      set: {
        entryComponents: [ProxyCreateDialogComponent],
      }
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProxiesComponent);
    component = fixture.componentInstance;
    proxyService = TestBed.get(ToxiproxyService);

    proxies = new Object();
    proxies['BarkerProxy'] = {
      'name': 'BarkerProxy',
      'listen': 'localhost:5002',
      'upstream': 'mbp-stacyp.bkedev.com:5003',
      'enabled': false,
      'toxics': []
    };
    proxies['Bobs Burgers'] = {
      'name': 'Bobs Burgers',
      'listen': '127.0.0.1:7779',
      'upstream': 'mbp-stacyp.bkedev.com:8000',
      'enabled': true,
      'toxics': []
    };
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should init data', () => {
    proxyService.getProxies.and.returnValue(of(proxies));
    fixture.detectChanges();

    expect(component.pageSize).toBe(25);
    expect(component.pageIndex).toBe(0);
    expect(component.totalItems).toBe(2);
    expect(component.proxies.sort).toBeFalsy();
    expect(component.proxies.filter).toBeFalsy();
    expect(component.proxies.data.filter(proxy => {proxy.name === 'BarkerProxy';})).toBeTruthy();
    expect(component.proxies.data.filter(proxy => {proxy.name === 'Bobs Burgers';})).toBeTruthy();
  });

  it('should load proxies with page size', () => {
    proxyService.getProxies.and.returnValue(of(proxies));
    let event = new PageEvent();
    event.pageSize = 1;
    event.pageIndex = 0;

    component.loadProxies(event);

    expect(component.proxies.data.length).toBe(1);
    expect(component.proxies.data.filter(proxy => {proxy.name === 'BarkerProxy';})).toBeTruthy();
    expect(component.pageSize).toBe(1);
  });

  it('should load proxies with page index', () => {
    proxyService.getProxies.and.returnValue(of(proxies));
    let event = new PageEvent();
    event.pageSize = 1;
    event.pageIndex = 1;

    component.loadProxies(event);

    expect(component.proxies.data.length).toBe(1);
    expect(component.proxies.data.filter(proxy => {proxy.name === 'Bobs Burgers';})).toBeTruthy();
    expect(component.pageSize).toBe(1);
  });

  it('should load proxies and apply sort', () => {
    proxyService.getProxies.and.returnValue(of(proxies));
    let sort = new MatSort();
    component.sort = sort;

    component.loadProxies(null);

    expect(component.proxies.sort).toEqual(sort);
  });

  it('should apply filter', () => {
    component.filter = 'Something';
    component.proxies = new MatTableDataSource();

    component.applyFilter();

    expect(component.proxies.filter).toBe('Something');
  });

  it('should apply filter when falsy', () => {
    component.filter = null;
    component.proxies = new MatTableDataSource();

    component.applyFilter();

    expect(component.proxies.filter).toBe(null);
  });

  it('should open create proxy dialog and populate on close', () => {
    proxyService.getProxies.and.returnValue(of(proxies));
    expect(component.createProxyDialog).toBeFalsy();
    expect(component.proxies).toBeFalsy();

    component.openProxyCreate();

    expect(component.createProxyDialog).toBeTruthy();

    component.createProxyDialog.close();
  });

  it('render should call create proxy', () => {
    spyOn(component, 'openProxyCreate');
    let createButton = fixture.debugElement.query(By.css('button.create-button'));
    createButton.triggerEventHandler('click', null);
    expect(component.openProxyCreate).toHaveBeenCalledTimes(1);
  });
});

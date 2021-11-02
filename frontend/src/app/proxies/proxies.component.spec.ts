import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ProxiesComponent} from './proxies.component';
import { MatDialog, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatRadioModule } from '@angular/material/radio';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
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
import createSpyObj = jasmine.createSpyObj;

describe('ProxiesComponent', () => {
  let component: ProxiesComponent;
  let fixture: ComponentFixture<ProxiesComponent>;
  let proxyService: SpyObj<ToxiproxyService>;
  let dialog: SpyObj<MatDialog>;
  let proxies;

  beforeEach(() => {
    const toxiProxySpy = createSpyObj('ToxiproxyService', ['getProxies']);
    const dialogSpy = createSpyObj('MatDialog', ['afterClosed', 'open']);

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
        {provide: MatDialog, useValue: dialogSpy},
        HttpClient,
        HttpHandler
      ]
    }).overrideModule(BrowserDynamicTestingModule, {
      set: {
        entryComponents: [ProxyCreateDialogComponent],
      }
    });

    fixture = TestBed.createComponent(ProxiesComponent);
    component = fixture.componentInstance;
    proxyService = TestBed.inject(ToxiproxyService) as jasmine.SpyObj<ToxiproxyService>;
    dialog = TestBed.inject(MatDialog) as jasmine.SpyObj<MatDialog> ;

    const matDialogRef = <SpyObj<MatDialogRef<any, any>>>createSpyObj('MatDialogRef', ['afterClosed']);
    matDialogRef.afterClosed.and.returnValue(of({}));
    dialog.open.and.returnValue(matDialogRef);

    proxies = {};
    proxies['BarkerProxy'] = {
      'name': 'BarkerProxy',
      'listen': 'localhost:5002',
      'upstream': 'somedomain.com:5003',
      'enabled': false,
      'toxics': []
    };
    proxies['Bobs Burgers'] = {
      'name': 'Bobs Burgers',
      'listen': '127.0.0.1:7779',
      'upstream': 'somdomain.com:8000',
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

  it('renders table with no proxies', () => {
    proxyService.getProxies.and.returnValue(of(null));
    fixture.detectChanges();
    const page: HTMLElement = fixture.nativeElement;
    const table = page.querySelector('.proxy-table');
    const tableRows = table.querySelectorAll('.proxy-table tbody .mat-row');

    // Table header
    expect(table.querySelector('.mat-column-name').textContent).toMatch('Name');
    expect(table.querySelector('.mat-column-enabled').textContent).toMatch('Enabled');
    expect(table.querySelector('.mat-column-listen').textContent).toMatch('Listen');
    expect(table.querySelector('.mat-column-upstream').textContent).toMatch('Upstream');

    // Table rows
    expect(tableRows.length).toBe(0);
  });

  it('renders table with proxies', () => {
    proxyService.getProxies.and.returnValue(of(proxies));
    fixture.detectChanges();
    const page: HTMLElement = fixture.nativeElement;
    const table = page.querySelector('.proxy-table');
    const tableRows = table.querySelectorAll('.proxy-table tbody .mat-row');

    // Table header
    expect(table.querySelector('.mat-column-name').textContent).toMatch('Name');
    expect(table.querySelector('.mat-column-enabled').textContent).toMatch('Enabled');
    expect(table.querySelector('.mat-column-listen').textContent).toMatch('Listen');
    expect(table.querySelector('.mat-column-upstream').textContent).toMatch('Upstream');

    // Table rows
    expect(tableRows.length).toBe(2);

    const firstRow = tableRows[0];
    const secondRow = tableRows[1];

    expect(firstRow.querySelector('.mat-column-name').textContent).toMatch('BarkerProxy');
    expect(firstRow.querySelector('.mat-column-enabled').textContent).toMatch('false');
    expect(firstRow.querySelector('.mat-column-listen').textContent).toMatch('localhost:5002');
    expect(firstRow.querySelector('.mat-column-upstream').textContent).toMatch('somedomain.com:5003');

    expect(secondRow.querySelector('.mat-column-name').textContent).toMatch('Bobs Burgers');
    expect(secondRow.querySelector('.mat-column-enabled').textContent).toMatch('true');
    expect(secondRow.querySelector('.mat-column-listen').textContent).toMatch('127.0.0.1:7779');
    expect(secondRow.querySelector('.mat-column-upstream').textContent).toMatch('somdomain.com:8000');
  });

  it('renders table with filtered proxies', () => {
    proxyService.getProxies.and.returnValue(of(proxies));
    fixture.detectChanges();
    const page: HTMLElement = fixture.nativeElement;
    const table = page.querySelector('.proxy-table');
    const tableRows = table.querySelectorAll('.proxy-table tbody .mat-row');

    // Table header
    expect(table.querySelector('.mat-column-name').textContent).toMatch('Name');
    expect(table.querySelector('.mat-column-enabled').textContent).toMatch('Enabled');
    expect(table.querySelector('.mat-column-listen').textContent).toMatch('Listen');
    expect(table.querySelector('.mat-column-upstream').textContent).toMatch('Upstream');

    // Table rows
    expect(tableRows.length).toBe(2);

    const firstRow = tableRows[0];
    const secondRow = tableRows[1];

    expect(firstRow.querySelector('.mat-column-name').textContent).toMatch('BarkerProxy');
    expect(firstRow.querySelector('.mat-column-enabled').textContent).toMatch('false');
    expect(firstRow.querySelector('.mat-column-listen').textContent).toMatch('localhost:5002');
    expect(firstRow.querySelector('.mat-column-upstream').textContent).toMatch('somedomain.com:5003');

    expect(secondRow.querySelector('.mat-column-name').textContent).toMatch('Bobs Burgers');
    expect(secondRow.querySelector('.mat-column-enabled').textContent).toMatch('true');
    expect(secondRow.querySelector('.mat-column-listen').textContent).toMatch('127.0.0.1:7779');
    expect(secondRow.querySelector('.mat-column-upstream').textContent).toMatch('somdomain.com:8000');

    // Type something into the filter
    component.filter = 'Burgers';
    component.applyFilter();
    fixture.detectChanges();

    const filteredRows = table.querySelectorAll('.proxy-table tbody .mat-row');
    expect(filteredRows.length).toBe(1);

    const filteredRow = filteredRows[0];

    expect(filteredRow.querySelector('.mat-column-name').textContent).toMatch('Bobs Burgers');
    expect(filteredRow.querySelector('.mat-column-enabled').textContent).toMatch('true');
    expect(filteredRow.querySelector('.mat-column-listen').textContent).toMatch('127.0.0.1:7779');
    expect(filteredRow.querySelector('.mat-column-upstream').textContent).toMatch('somdomain.com:8000');
  });

  it('should load proxies with page size', () => {
    proxyService.getProxies.and.returnValue(of(proxies));
    const event = new PageEvent();
    event.pageSize = 1;
    event.pageIndex = 0;

    component.loadProxies(event);

    expect(component.proxies.data.length).toBe(1);
    expect(component.proxies.data.filter(proxy => {proxy.name === 'BarkerProxy';})).toBeTruthy();
    expect(component.pageSize).toBe(1);
  });

  it('should load proxies with page index', () => {
    proxyService.getProxies.and.returnValue(of(proxies));
    const event = new PageEvent();
    event.pageSize = 1;
    event.pageIndex = 1;

    component.loadProxies(event);

    expect(component.proxies.data.length).toBe(1);
    expect(component.proxies.data.filter(proxy => {proxy.name === 'Bobs Burgers';})).toBeTruthy();
    expect(component.pageSize).toBe(1);
  });

  it('should load proxies and apply sort', () => {
    proxyService.getProxies.and.returnValue(of(proxies));
    const sort = new MatSort();
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

  it('should apply when no proxies', () => {
    component.filter = 'Something';
    component.proxies = null;

    component.applyFilter();

    expect(component.proxies).toBe(null);
  });

  it('should open create proxy dialog', () => {
    const componentLoadProxySpy = spyOn(component, 'loadProxies');

    component.openProxyCreate();

    expect(dialog.open).toHaveBeenCalledWith(ProxyCreateDialogComponent, {width: '500px'});
    expect(componentLoadProxySpy).toHaveBeenCalledWith(null);
  });

  it('render should call create proxy', () => {
    spyOn(component, 'openProxyCreate');
    const createButton = fixture.debugElement.query(By.css('button.create-button'));
    createButton.triggerEventHandler('click', null);
    expect(component.openProxyCreate).toHaveBeenCalledTimes(1);
  });
});

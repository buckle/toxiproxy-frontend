import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ProxiesComponent} from './proxies.component';
import { MatDialog, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatPaginatorModule } from '@angular/material/paginator';
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
    expect(component.proxies.sort).toEqual(component.sort);
    expect(component.proxies.paginator).toEqual(component.paginator);
    expect(component.proxies.filter).toBeFalsy();
    expect(component.proxies.data.filter(proxy => {proxy.name === 'BarkerProxy';})).toBeTruthy();
    expect(component.proxies.data.filter(proxy => {proxy.name === 'Bobs Burgers';})).toBeTruthy();
  });

  it('renders table with no proxies', async () => {
    proxyService.getProxies.and.returnValue(of(null));
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();

    const page: HTMLElement = fixture.nativeElement;
    const table = page.querySelector('.proxy-table');
    const tableRows = table.querySelectorAll('tr.mat-mdc-row');

    // Table header
    expect(table.querySelector('.mat-column-name').textContent).toMatch('Name');
    expect(table.querySelector('.mat-column-enabled').textContent).toMatch('Enabled');
    expect(table.querySelector('.mat-column-listen').textContent).toMatch('Listen');
    expect(table.querySelector('.mat-column-upstream').textContent).toMatch('Upstream');

    // Table rows
    expect(tableRows.length).toBe(0);
  });

  it('renders table with proxies', async () => {
    proxyService.getProxies.and.returnValue(of(proxies));
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();

    const page: HTMLElement = fixture.nativeElement;
    const table = page.querySelector('.proxy-table');
    const tableRows = table.querySelectorAll('tr.mat-mdc-row');

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

  it('renders table with filtered proxies', async () => {
    proxyService.getProxies.and.returnValue(of(proxies));
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();

    const page: HTMLElement = fixture.nativeElement;
    const table = page.querySelector('.proxy-table');
    const tableRows = table.querySelectorAll('tr.mat-mdc-row');

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
    await fixture.whenStable();
    fixture.detectChanges();

    const filteredRows = table.querySelectorAll('tr.mat-mdc-row');
    expect(filteredRows.length).toBe(1);

    const filteredRow = filteredRows[0];

    expect(filteredRow.querySelector('.mat-column-name').textContent).toMatch('Bobs Burgers');
    expect(filteredRow.querySelector('.mat-column-enabled').textContent).toMatch('true');
    expect(filteredRow.querySelector('.mat-column-listen').textContent).toMatch('127.0.0.1:7779');
    expect(filteredRow.querySelector('.mat-column-upstream').textContent).toMatch('somdomain.com:8000');
  });

  it('should filter results even if they are not on the current page', async () => {
    const manyProxies = {};
    for (let i = 0; i < 30; i++) {
      const name = `Proxy ${i.toString().padStart(2, '0')}`;
      manyProxies[name] = {
        name: name,
        enabled: true,
        listen: `localhost:${5000 + i}`,
        upstream: `localhost:${6000 + i}`,
        toxics: []
      };
    }
    manyProxies['TargetProxy'] = {
      name: 'TargetProxy',
      enabled: true,
      listen: 'localhost:9999',
      upstream: 'localhost:9998',
      toxics: []
    };

    proxyService.getProxies.and.returnValue(of(manyProxies));
    component.pageSize = 10;

    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();

    const page: HTMLElement = fixture.nativeElement;
    expect(page.textContent).not.toContain('TargetProxy');

    component.filter = 'Target';
    component.applyFilter();
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();

    expect(page.textContent).toContain('TargetProxy');

    const tableRows = page.querySelectorAll('tr.mat-mdc-row');
    expect(tableRows.length).toBe(1);
    expect(tableRows[0].textContent).toContain('TargetProxy');
  });

  it('should reset to first page when applying filter', async () => {
    const manyProxies = {};
    for (let i = 0; i < 30; i++) {
      const name = `Proxy ${i.toString().padStart(2, '0')}`;
      manyProxies[name] = {
        name: name,
        enabled: true,
        listen: `localhost:${5000 + i}`,
        upstream: `localhost:${6000 + i}`,
        toxics: []
      };
    }

    proxyService.getProxies.and.returnValue(of(manyProxies));
    component.pageSize = 10;

    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();

    component.proxies.paginator.pageIndex = 2;
    fixture.detectChanges();

    expect(component.proxies.paginator.pageIndex).toBe(2);

    component.filter = 'Proxy 0';
    component.applyFilter();
    fixture.detectChanges();

    expect(component.proxies.paginator.pageIndex).toBe(0);
  });

  it('should load proxies', () => {
    proxyService.getProxies.and.returnValue(of(proxies));

    component.loadProxies();

    expect(component.proxies.data.length).toBe(2);
    expect(component.proxies.data.find(proxy => proxy.name === 'BarkerProxy')).toBeTruthy();
    expect(component.proxies.data.find(proxy => proxy.name === 'Bobs Burgers')).toBeTruthy();
  });

  it('should load proxies and apply sort', () => {
    proxyService.getProxies.and.returnValue(of(proxies));
    const sort = new MatSort();
    component.sort = sort;

    component.loadProxies();

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
    expect(componentLoadProxySpy).toHaveBeenCalledWith();
  });

  it('render should call create proxy', () => {
    spyOn(component, 'openProxyCreate');
    const createButton = fixture.debugElement.query(By.css('button.create-button'));
    createButton.triggerEventHandler('click', null);
    expect(component.openProxyCreate).toHaveBeenCalledTimes(1);
  });
});

import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ProxyDetailComponent} from './proxy-detail.component';
import {AppModule} from '../../app.module';
import {RouterTestingModule} from '@angular/router/testing';
import {ToxiproxyService} from '../../services/toxiproxy.service';
import {of, throwError} from 'rxjs';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {MatSnackBar} from '@angular/material/snack-bar';
import {ActivatedRoute, Router} from '@angular/router';
import {Proxy} from '../../services/proxy';
import {ProxyCreateDialogComponent} from '../proxy-create-dialog/proxy-create-dialog.component';
import {ToxicCreateDialogComponent} from './toxic-create-dialog/toxic-create-dialog.component';
import {Toxic} from '../../services/toxic';
import {ActivatedRouteStub} from '../../../testing/ActivatedRouteStub';
import createSpyObj = jasmine.createSpyObj;
import SpyObj = jasmine.SpyObj;

describe('ProxyDetailComponent', () => {
  let component: ProxyDetailComponent;
  let fixture: ComponentFixture<ProxyDetailComponent>;
  let proxyService: SpyObj<ToxiproxyService>;
  let snackBar: SpyObj<MatSnackBar>;
  let router: Router;
  let dialog: SpyObj<MatDialog>;
  let route: ActivatedRouteStub;
  let proxy: Proxy;
  let proxyWithoutToxic: Proxy;

  beforeEach(() => {
    const toxiProxySpy = createSpyObj('ToxiproxyService', ['getProxy', 'deleteProxy', 'deleteToxic']);
    const snackBarSpy = createSpyObj('MatSnackBar', ['open']);
    const dialogSpy = createSpyObj('MatDialog', ['afterClosed', 'open']);
    const activatedRouteStub = new ActivatedRouteStub();

    TestBed.configureTestingModule({
      imports: [
        AppModule,
        RouterTestingModule
      ],
      providers: [
        {provide: ToxiproxyService, useValue: toxiProxySpy},
        {provide: MatSnackBar, useValue: snackBarSpy},
        {provide: MatDialog, useValue: dialogSpy},
        {provide: ActivatedRoute, useValue: activatedRouteStub}
      ]
    });

    fixture = TestBed.createComponent(ProxyDetailComponent);
    component = fixture.componentInstance;
    proxyService = TestBed.inject(ToxiproxyService) as jasmine.SpyObj<ToxiproxyService>;
    snackBar = TestBed.inject(MatSnackBar) as jasmine.SpyObj<MatSnackBar>;
    router = TestBed.inject(Router);
    dialog = TestBed.inject(MatDialog) as jasmine.SpyObj<MatDialog>;
    route = TestBed.inject(ActivatedRoute) as unknown as ActivatedRouteStub;

    let matDialogRef = <SpyObj<MatDialogRef<any, any>>>createSpyObj('MatDialogRef', ['afterClosed']);
    matDialogRef.afterClosed.and.returnValue(of({}));
    dialog.open.and.returnValue(matDialogRef);

    proxy = {
      'name': 'BarkerProxy',
      'listen': 'localhost:5002',
      'upstream': 'somedomain.com:5003',
      'enabled': true,
      'toxics': [
        {
          'attributes': {
            'rate': 123
          },
          'name': 'Bandwidth Upstream',
          'type': 'bandwidth',
          'stream': 'upstream',
          'toxicity': 1
        }
      ]
    };

    proxyWithoutToxic = {
      'name': 'BarkerProxy',
      'listen': 'localhost:5002',
      'upstream': 'somedomain.com:5003',
      'enabled': true,
      'toxics': []
    };
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('renders details without toxics', () => {
    route.setParamMap({name: 'haha'});
    proxyService.getProxy.and.returnValue(of(proxyWithoutToxic));
    fixture.detectChanges();

    const page = fixture.nativeElement;
    const pageTitleText = page.querySelector('.mat-title').textContent;
    const detailSection = page.querySelector('.details');
    const detailSectionTitleText = detailSection.querySelector('.mat-title').textContent;
    const detailSectionValues = detailSection.querySelectorAll('mat-list-item');

    expect(pageTitleText).toMatch(proxy.name);
    expect(detailSectionTitleText).toMatch('Details');
    expect(detailSectionValues[0].textContent).toMatch('Listen: ' + proxy.listen);
    expect(detailSectionValues[1].textContent).toMatch('Upstream: ' + proxy.upstream);
    expect(detailSectionValues[2].textContent).toMatch('Enabled: ' + proxy.enabled);
  });

  it('renders toxics section without toxics', () => {
    route.setParamMap({name: 'haha'});
    proxyService.getProxy.and.returnValue(of(proxyWithoutToxic));
    fixture.detectChanges();

    const toxicsSection = fixture.nativeElement.querySelector('.toxics');
    const toxicsSectionTitleText = toxicsSection.querySelector('.mat-title').textContent;
    const toxicsSectionNoToxicsText = toxicsSection.querySelector('.mat-body').textContent;
    const toxicsTable = toxicsSection.querySelector('table');

    expect(toxicsSectionTitleText).toMatch('Toxics');
    expect(toxicsSectionNoToxicsText).toMatch('No toxics setup, use the menu to add a toxic.');
    expect(toxicsTable).toBeNull();
  });

  it('renders details with toxics', () => {
    route.setParamMap({name: 'haha'});
    proxyService.getProxy.and.returnValue(of(proxy));
    fixture.detectChanges();

    const page = fixture.nativeElement;
    const pageTitleText = page.querySelector('.mat-title').textContent;
    const detailSection = page.querySelector('.details');
    const detailSectionTitleText = detailSection.querySelector('.mat-title').textContent;
    const detailSectionValues = detailSection.querySelectorAll('mat-list-item');

    expect(pageTitleText).toMatch(proxy.name);
    expect(detailSectionTitleText).toMatch('Details');
    expect(detailSectionValues[0].textContent).toMatch('Listen: ' + proxy.listen);
    expect(detailSectionValues[1].textContent).toMatch('Upstream: ' + proxy.upstream);
    expect(detailSectionValues[2].textContent).toMatch('Enabled: ' + proxy.enabled);
  });

  it('renders toxics section with toxics', () => {
    route.setParamMap({name: 'haha'});
    proxyService.getProxy.and.returnValue(of(proxy));
    fixture.detectChanges();

    const toxicsSection = fixture.nativeElement.querySelector('.toxics');
    const toxicsSectionTitleText = toxicsSection.querySelector('.mat-title').textContent;
    const tableHeaderSection = toxicsSection.querySelectorAll('.mat-header-cell');
    const tableEntry = toxicsSection.querySelectorAll('.mat-row');
    const tableEntryValues = toxicsSection.querySelectorAll('.mat-cell');

    expect(toxicsSectionTitleText).toMatch('Toxics');

    expect(tableHeaderSection[0].textContent).toMatch('Name');
    expect(tableHeaderSection[1].textContent).toMatch('Type');
    expect(tableHeaderSection[2].textContent).toMatch('Stream');
    expect(tableHeaderSection[3].textContent).toMatch('Toxicity');
    expect(tableHeaderSection[4].textContent).toMatch('Attributes');
    expect(tableHeaderSection[5].textContent).toMatch(''); // Edit/delete column

    const proxyToxic = proxy.toxics[0];
    expect(tableEntry.length).toBe(1);
    expect(tableEntryValues[0].textContent).toMatch(proxyToxic.name);
    expect(tableEntryValues[1].textContent).toMatch(proxyToxic.type);
    expect(tableEntryValues[2].textContent).toMatch(proxyToxic.stream);
    expect(tableEntryValues[3].textContent).toMatch(String(proxyToxic.toxicity));
    expect(tableEntryValues[4].textContent).toMatch('rate: 123');
    expect(tableEntryValues[5].querySelectorAll('button')[0].textContent).toMatch('edit');
    expect(tableEntryValues[5].querySelectorAll('button')[1].textContent).toMatch('close');
  });

  it('should load proxy', () => {
    route.setParamMap({name: 'haha'});
    proxyService.getProxy.and.returnValue(of(proxy));
    fixture.detectChanges();

    expect(component.proxy).toBeTruthy();
    expect(component.proxy.name).toEqual(proxy.name);
    expect(component.toxicDataSource).toBeTruthy();
    expect(component.toxicDataSource.data).toContain(proxy.toxics[0]);
    expect(proxyService.getProxy).toHaveBeenCalledWith('haha');
  });

  it('should load proxy with no proxy parameter', () => {
    route.setParamMap({});
    fixture.detectChanges();

    expect(component.proxy).toBeUndefined();
    expect(component.toxicDataSource).toBeUndefined();
    expect(proxyService.getProxy).toHaveBeenCalledTimes(0);
  });

  it('should load proxy with error', () => {
    route.setParamMap({name: 'haha'});
    proxyService.getProxy.and.returnValue(throwError('not found'));
    fixture.detectChanges();

    expect(component.proxy).toBeUndefined();
    expect(component.toxicDataSource).toBeUndefined();
    expect(snackBar.open).toHaveBeenCalledWith('Unable to load proxy.', 'Close', {duration: 8000});
  });

  it('should delete proxy', () => {
    proxyService.deleteProxy.and.returnValue(of({}));
    component.proxy = new Proxy();
    let navigateSpy = spyOn(router, 'navigate');

    component.deleteProxy();

    expect(proxyService.deleteProxy).toHaveBeenCalledWith(component.proxy);
    expect(navigateSpy).toHaveBeenCalledWith(['/proxies']);
  });

  it('should delete proxy with error', () => {
    proxyService.deleteProxy.and.returnValue(throwError('bad delete'));
    component.proxy = proxy;

    component.deleteProxy();

    expect(proxyService.deleteProxy).toHaveBeenCalledWith(component.proxy);
    expect(snackBar.open).toHaveBeenCalledWith('Unable to delete proxy.', 'Close', {duration: 8000});
  });

  it('should open edit proxy dialog', () => {
    let componentLoadProxySpy = spyOn(component, 'loadProxy');
    component.proxy = proxy;

    component.editProxy();

    expect(dialog.open).toHaveBeenCalledWith(ProxyCreateDialogComponent, {width: '500px', data: component.proxy});
    expect(componentLoadProxySpy).toHaveBeenCalled();
  });

  it('should open create toxic dialog', () => {
    let componentLoadProxySpy = spyOn(component, 'loadProxy');
    component.proxy = proxy;

    component.openCreateToxicDialog();

    expect(dialog.open).toHaveBeenCalledWith(ToxicCreateDialogComponent, {width: '500px', data: {'proxy': component.proxy}});
    expect(componentLoadProxySpy).toHaveBeenCalled();
  });

  it('should open edit toxic dialog', () => {
    let componentLoadProxySpy = spyOn(component, 'loadProxy');
    component.proxy = proxy;
    let toxic = new Toxic();

    component.editToxicDialog(toxic);

    expect(dialog.open).toHaveBeenCalledWith(ToxicCreateDialogComponent, {width: '500px', data: {'proxy': component.proxy, 'toxic': toxic}});
    expect(componentLoadProxySpy).toHaveBeenCalled();
  });

  it('should delete toxic', () => {
    let componentLoadProxySpy = spyOn(component, 'loadProxy');
    proxyService.deleteToxic.and.returnValue(of({}));
    let toxic = new Toxic();
    component.proxy = proxy;

    component.deleteToxic(toxic);

    expect(proxyService.deleteToxic).toHaveBeenCalledWith(component.proxy, toxic);
    expect(componentLoadProxySpy).toHaveBeenCalled();
  });

  it('should delete toxic with error', () => {
    proxyService.deleteToxic.and.returnValue(throwError('unable to delete'));

    let toxic = new Toxic();
    component.proxy = proxy;

    component.deleteToxic(toxic);
    expect(proxyService.deleteToxic).toHaveBeenCalledWith(component.proxy, toxic);
    expect(snackBar.open).toHaveBeenCalledWith('Unable to delete toxic.', 'Close', {duration: 8000});
  });
});

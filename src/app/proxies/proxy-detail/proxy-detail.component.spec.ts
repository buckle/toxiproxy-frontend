import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ProxyDetailComponent} from './proxy-detail.component';
import {AppModule} from '../../app.module';
import {RouterTestingModule} from '@angular/router/testing';
import {ToxiproxyService} from '../../services/toxiproxy.service';
import {of, throwError} from 'rxjs';
import {MatDialog, MatDialogRef, MatSnackBar} from '@angular/material';
import {Router} from '@angular/router';
import {Proxy} from '../../services/proxy';
import createSpyObj = jasmine.createSpyObj;
import SpyObj = jasmine.SpyObj;
import {ProxyCreateDialogComponent} from '../proxy-create-dialog/proxy-create-dialog.component';
import {ToxicCreateDialogComponent} from './toxic-create-dialog/toxic-create-dialog.component';
import {Toxic} from '../../services/toxic';

describe('ProxyDetailComponent', () => {
  let component: ProxyDetailComponent;
  let fixture: ComponentFixture<ProxyDetailComponent>;
  let proxyService: SpyObj<ToxiproxyService>;
  let snackBar: SpyObj<MatSnackBar>;
  let router: Router;
  let dialog: MatDialog;
  let proxy: Proxy;

  beforeEach(async(() => {
    const toxiProxySpy = createSpyObj('ToxiproxyService', ['getProxy', 'deleteProxy']);
    const snackBarSpy = createSpyObj('MatSnackBar', ['open']);

    TestBed.configureTestingModule({
      imports: [
        AppModule,
        RouterTestingModule
      ],
      providers: [
        {provide: ToxiproxyService, useValue: toxiProxySpy},
        {provide: MatSnackBar, useValue: snackBarSpy},
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProxyDetailComponent);
    component = fixture.componentInstance;
    proxyService = TestBed.get(ToxiproxyService);
    snackBar = TestBed.get(MatSnackBar);
    router = TestBed.get(Router);
    dialog = TestBed.get(MatDialog);

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
          'name': 'bandwidth_upstream',
          'type': 'bandwidth',
          'stream': 'upstream',
          'toxicity': 1
        }
      ]
    };
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load proxy', () => {
    // TODO Test parameter from url
    proxyService.getProxy.and.returnValue(of(proxy));
    fixture.detectChanges();

    expect(component.proxy).toBeTruthy();
    expect(component.proxy.name).toEqual(proxy.name);
    expect(component.toxicDataSource).toBeTruthy();
    expect(component.toxicDataSource.data).toContain(proxy.toxics[0]);
  });

  it('should load proxy with error', () => {
    proxyService.getProxy.and.returnValue(throwError("not found"));
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
    proxyService.deleteProxy.and.returnValue(throwError("bad delete"));
    component.proxy = proxy;

    component.deleteProxy();

    expect(proxyService.deleteProxy).toHaveBeenCalledWith(component.proxy);
    expect(snackBar.open).toHaveBeenCalledWith('Unable to delete proxy.', 'Close', {duration: 8000});
  });

  it('should open edit proxy dialog', () => {
    let matDialogRef = <SpyObj<MatDialogRef<any, any>>>createSpyObj('MatDialogRef', ['afterClosed']);
    matDialogRef.afterClosed.and.returnValue(of({}));
    let dialogOpenSpy = spyOn(dialog, 'open');
    dialogOpenSpy.and.returnValue(matDialogRef);
    let componentLoadProxySpy = spyOn(component, 'loadProxy');
    component.proxy = proxy;

    component.editProxy();

    expect(dialogOpenSpy).toHaveBeenCalledWith(ProxyCreateDialogComponent, {width: '500px', data: component.proxy});
    expect(componentLoadProxySpy).toHaveBeenCalled();
  });

  it('should open create toxic dialog', () => {
    let matDialogRef = <SpyObj<MatDialogRef<any, any>>>createSpyObj('MatDialogRef', ['afterClosed']);
    matDialogRef.afterClosed.and.returnValue(of({}));
    let dialogOpenSpy = spyOn(dialog, 'open');
    dialogOpenSpy.and.returnValue(matDialogRef);
    let componentLoadProxySpy = spyOn(component, 'loadProxy');
    component.proxy = proxy;

    component.openCreateToxicDialog();

    expect(dialogOpenSpy).toHaveBeenCalledWith(ToxicCreateDialogComponent, {width: '500px', data: {'proxy': component.proxy}});
    expect(componentLoadProxySpy).toHaveBeenCalled();
  });

  it('should open edit toxic dialog', () => {
    let matDialogRef = <SpyObj<MatDialogRef<any, any>>>createSpyObj('MatDialogRef', ['afterClosed']);
    matDialogRef.afterClosed.and.returnValue(of({}));
    let dialogOpenSpy = spyOn(dialog, 'open');
    dialogOpenSpy.and.returnValue(matDialogRef);
    let componentLoadProxySpy = spyOn(component, 'loadProxy');
    component.proxy = proxy;
    let toxic = new Toxic();

    component.editToxicDialog(toxic);

    expect(dialogOpenSpy).toHaveBeenCalledWith(ToxicCreateDialogComponent, {width: '500px', data: {'proxy': component.proxy, 'toxic': toxic}});
    expect(componentLoadProxySpy).toHaveBeenCalled();
  });
});

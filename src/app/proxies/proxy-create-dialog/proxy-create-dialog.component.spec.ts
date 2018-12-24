import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ProxyCreateDialogComponent} from './proxy-create-dialog.component';
import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef,
  MatFormFieldModule,
  MatInputModule,
  MatProgressSpinnerModule,
  MatRadioModule, MatSnackBar,
  MatSnackBarModule
} from '@angular/material';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {ToxiproxyService} from '../../services/toxiproxy.service';
import {HttpClient, HttpHandler} from '@angular/common/http';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import {Proxy} from '../../services/proxy';
import {of, throwError} from 'rxjs';
import createSpyObj = jasmine.createSpyObj;
import SpyObj = jasmine.SpyObj;

describe('ProxyCreateDialogComponent', () => {
  let component: ProxyCreateDialogComponent;
  let fixture: ComponentFixture<ProxyCreateDialogComponent>;
  let dialog: SpyObj<MatDialogRef<ProxyCreateDialogComponent>>;
  let dialogData: Proxy;
  let proxyService: SpyObj<ToxiproxyService>;
  let snackBar: SpyObj<MatSnackBar>;

  beforeEach(async(() => {
    const spyDialog = createSpyObj('MatDialogRef', ['close']);
    const toxiProxySpy = createSpyObj('ToxiproxyService', ['updateProxy', 'createProxy']);
    const mockSnackBar = createSpyObj('MatSnackBar', ['open']);

    TestBed.configureTestingModule({
      declarations: [ProxyCreateDialogComponent],
      imports: [
        ReactiveFormsModule,
        MatFormFieldModule,
        MatRadioModule,
        MatDialogModule,
        MatProgressSpinnerModule,
        MatSnackBarModule,
        MatInputModule,
        NoopAnimationsModule
      ],
      providers: [
        {provide: ToxiproxyService, useValue: toxiProxySpy},
        HttpClient,
        HttpHandler,
        { provide: MatDialogRef, useValue: spyDialog },
        { provide: MAT_DIALOG_DATA, useValue: {} },
        { provide: MatSnackBar, useValue: mockSnackBar }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProxyCreateDialogComponent);
    component = fixture.componentInstance;
    dialog = TestBed.get(MatDialogRef);
    dialogData = TestBed.get(MAT_DIALOG_DATA);
    proxyService = TestBed.get(ToxiproxyService);
    snackBar = TestBed.get(MatSnackBar);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should init', () => {
    fixture.detectChanges();
    expect(component.form).toBeTruthy();
  });

  it('should init form with proxy', () => {
    let proxy = new Proxy();
    proxy.name = 'some name';
    proxy.listen = '127.0.0.1:7888';
    proxy.upstream = 'somewhere.bobbarker.com:7889';
    proxy.enabled = true;

    component.initForm(proxy);

    expect(component.isUpdate).toBe(true);
    expect(component.form.get('name').value).toBe(proxy.name);
    expect(component.form.get('name').validator).toBe(Validators.required);
    expect(component.form.get('name').disabled).toBe(true);
    expect(component.form.get('listen').value).toBe(proxy.listen);
    expect(component.form.get('listen').validator).toBe(Validators.required);
    expect(component.form.get('upstream').value).toBe(proxy.upstream);
    expect(component.form.get('upstream').validator).toBe(Validators.required);
    expect(component.form.get('enabled').value).toBe(proxy.enabled + '');
  });

  it('should init form w/o proxy', () => {
    component.initForm(null);

    expect(component.isUpdate).toBe(false);
    expect(component.form.get('name').value).toBe('');
    expect(component.form.get('name').validator).toBe(Validators.required);
    expect(component.form.get('listen').value).toBe('');
    expect(component.form.get('listen').validator).toBe(Validators.required);
    expect(component.form.get('upstream').value).toBe('');
    expect(component.form.get('upstream').validator).toBe(Validators.required);
    expect(component.form.get('enabled').value).toBe('true');
  });

  it('should send update service call for update', () => {
    proxyService.updateProxy.and.returnValue(of(null));
    let proxy = new Proxy();
    proxy.name = 'some name';
    proxy.listen = 'localhost:5002';
    proxy.upstream = 'somedomain.com:5003';
    proxy.enabled = true;

    component.form = new FormGroup({
      name: new FormControl(proxy.name),
      listen: new FormControl(proxy.listen),
      upstream: new FormControl(proxy.upstream),
      enabled: new FormControl(proxy.enabled + '')
    });
    component.isUpdate = true;

    component.onSubmit();

    expect(proxyService.updateProxy).toHaveBeenCalledWith(proxy);
    expect(component.inProgress).toBe(false);
  });

  it('should send update service call with error', () => {
    proxyService.updateProxy.and.returnValue(throwError('Testing error'));

    component.form = new FormGroup({
      name: new FormControl(''),
      listen: new FormControl(''),
      upstream: new FormControl(''),
      enabled: new FormControl('')
    });
    component.isUpdate = true;

    component.onSubmit();

    expect(proxyService.updateProxy).toHaveBeenCalled();
    expect(snackBar.open).toHaveBeenCalled();
    expect(component.inProgress).toBe(false);
  });

  it('should send create call for create', () => {
    proxyService.createProxy.and.returnValue(of(null));
    let proxy = new Proxy();
    proxy.name = 'some name';
    proxy.listen = 'localhost:5002';
    proxy.upstream = 'somedomain.com:5003';
    proxy.enabled = true;

    component.form = new FormGroup({
      name: new FormControl(proxy.name),
      listen: new FormControl(proxy.listen),
      upstream: new FormControl(proxy.upstream),
      enabled: new FormControl(proxy.enabled + '')
    });
    component.isUpdate = false;

    component.onSubmit();

    expect(proxyService.createProxy).toHaveBeenCalledWith(proxy);
    expect(component.inProgress).toBe(false);
  });

  it('should send create call with error', () => {
    proxyService.createProxy.and.returnValue(throwError('Testing error'));

    component.form = new FormGroup({
      name: new FormControl(''),
      listen: new FormControl(''),
      upstream: new FormControl(''),
      enabled: new FormControl('')
    });
    component.isUpdate = false;

    component.onSubmit();

    expect(proxyService.createProxy).toHaveBeenCalled();
    expect(snackBar.open).toHaveBeenCalled();
    expect(component.inProgress).toBe(false);
  });
});

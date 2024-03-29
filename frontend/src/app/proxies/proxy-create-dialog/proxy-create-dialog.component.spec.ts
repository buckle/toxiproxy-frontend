import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ProxyCreateDialogComponent} from './proxy-create-dialog.component';
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from '@angular/material/dialog';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {MatRadioModule} from '@angular/material/radio';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
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

  beforeEach(() => {
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
    });

    fixture = TestBed.createComponent(ProxyCreateDialogComponent);
    component = fixture.componentInstance;
    dialog = TestBed.inject(MatDialogRef) as jasmine.SpyObj<MatDialogRef<ProxyCreateDialogComponent>>;
    dialogData = TestBed.inject(MAT_DIALOG_DATA);
    proxyService = TestBed.inject(ToxiproxyService) as jasmine.SpyObj<ToxiproxyService>;
    snackBar = TestBed.inject(MatSnackBar) as jasmine.SpyObj<MatSnackBar>;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should init', () => {
    fixture.detectChanges();
    expect(component.form).toBeTruthy();
  });

  it('should init form with proxy', () => {
    const proxy = new Proxy();
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

  it('renders with correct inputs', () => {
    const popup = fixture.nativeElement;
    const titleText = popup.querySelector('.proxy-create-title').textContent;
    const inputs = popup.querySelectorAll('input');
    const radios = popup.querySelectorAll('mat-radio-button');

    expect(titleText).toMatch('Proxy');
    expect(inputs[0].getAttribute('placeholder')).toMatch('Name');
    expect(inputs[1].getAttribute('placeholder')).toMatch('Listen');
    expect(inputs[2].getAttribute('placeholder')).toMatch('Upstream');

    expect(radios[0].getAttribute('value')).toMatch('true');
    expect(radios[1].getAttribute('value')).toMatch('false');
  });

  it('should send update service call for update', () => {
    proxyService.updateProxy.and.returnValue(of(null));
    const proxy = new Proxy();
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
    const proxy = new Proxy();
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

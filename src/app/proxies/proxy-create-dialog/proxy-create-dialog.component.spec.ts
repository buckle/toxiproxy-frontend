import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ProxyCreateDialogComponent} from './proxy-create-dialog.component';
import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef,
  MatFormFieldModule,
  MatInputModule,
  MatProgressSpinnerModule,
  MatRadioModule,
  MatSnackBarModule
} from '@angular/material';
import {ReactiveFormsModule, Validators} from '@angular/forms';
import {ToxiproxyService} from '../../services/toxiproxy.service';
import {HttpClient, HttpHandler} from '@angular/common/http';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import {Proxy} from '../../services/proxy';
import createSpyObj = jasmine.createSpyObj;
import SpyObj = jasmine.SpyObj;

describe('ProxyCreateDialogComponent', () => {
  let component: ProxyCreateDialogComponent;
  let fixture: ComponentFixture<ProxyCreateDialogComponent>;
  let dialog: SpyObj<MatDialogRef<ProxyCreateDialogComponent>>;
  let dialogData: Proxy;

  beforeEach(async(() => {
    let spyDialog = createSpyObj('MatDialogRef', ['close']);

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
        ToxiproxyService,
        HttpClient,
        HttpHandler,
        { provide: MatDialogRef, useValue: spyDialog },
        { provide: MAT_DIALOG_DATA, useValue: {} }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProxyCreateDialogComponent);
    component = fixture.componentInstance;
    dialog = TestBed.get(MatDialogRef);
    dialogData = TestBed.get(MAT_DIALOG_DATA);
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
});

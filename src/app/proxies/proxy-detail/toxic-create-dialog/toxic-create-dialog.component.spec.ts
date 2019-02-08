import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ToxicCreateDialogComponent} from './toxic-create-dialog.component';
import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef,
  MatInputModule,
  MatProgressSpinnerModule,
  MatSelectModule,
  MatSnackBarModule,
  MatTooltipModule
} from '@angular/material';
import {ReactiveFormsModule, Validators} from '@angular/forms';
import {ToxiproxyService} from '../../../services/toxiproxy.service';
import {HttpClient, HttpHandler} from '@angular/common/http';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import {ToxicData} from './toxic-data';
import {Proxy} from '../../../services/proxy';
import {Toxic} from '../../../services/toxic';
import {ToxicTypeConstants} from './toxic-type-constants';

describe('ToxicCreateDialogComponent', () => {
  let component: ToxicCreateDialogComponent;
  let fixture: ComponentFixture<ToxicCreateDialogComponent>;
  let proxy: Proxy;
  let toxic: Toxic;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ToxicCreateDialogComponent],
      imports: [
        ReactiveFormsModule,
        MatSelectModule,
        MatTooltipModule,
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
        {provide: MatDialogRef, useValue: {}},
        {provide: MAT_DIALOG_DATA, useValue: {}}
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ToxicCreateDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    proxy = {
      'name': 'BarkerProxy',
      'listen': 'localhost:5002',
      'upstream': 'somedomain.com:5003',
      'enabled': true,
      'toxics': []
    };

    toxic = {
      'attributes': {
        'rate': 123
      },
      'name': 'bandwidth_upstream',
      'type': 'bandwidth',
      'stream': 'upstream',
      'toxicity': 1
    }
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should init with toxic', () => {
    let createFormForUpdateSpy = spyOn(component, 'createFormForUpdate');

    let toxicData = new ToxicData();
    toxicData.toxic = toxic;
    toxicData.proxy = proxy;
    component.data = toxicData;

    component.ngOnInit();

    expect(component.proxy).toEqual(proxy);
    expect(component.toxic).toEqual(toxic);
    expect(component.isUpdate).toBeTruthy();
    expect(createFormForUpdateSpy).toHaveBeenCalledTimes(1);
  });

  it('should init without toxic', () => {
    let createFormForNewSpy = spyOn(component, 'createFormForNew');

    let toxicData = new ToxicData();
    toxicData.proxy = proxy;
    component.data = toxicData;

    component.ngOnInit();

    expect(component.proxy).toEqual(proxy);
    expect(component.toxic).toBeUndefined();
    expect(component.isUpdate).toBeFalsy();
    expect(createFormForNewSpy).toHaveBeenCalledTimes(1);
  });

  it('should create form for update', () => {
    component.proxy = proxy;
    component.toxic = toxic;
    let typeSelectSpy = spyOn(component, 'typeSelect');

    component.createFormForUpdate();

    expect(component.toxicForm).toBeTruthy();

    let typeControl = component.toxicForm.controls['type'];
    expect(typeControl.value).toEqual(component.toxic.type);
    expect(typeControl.disabled).toBeTruthy();
    expect(typeControl.validator).toEqual(Validators.required);

    let streamControl = component.toxicForm.controls['stream'];
    expect(streamControl.value).toEqual(component.toxic.stream);
    expect(streamControl.disabled).toBeTruthy();
    expect(streamControl.validator).toEqual(Validators.required);

    let toxicityControl = component.toxicForm.controls['toxicity'];
    expect(toxicityControl.value).toEqual(component.toxic.toxicity + '');
    expect(toxicityControl.disabled).toBeFalsy();
    expect(toxicityControl.validator).toEqual(Validators.required);

    expect(typeSelectSpy).toHaveBeenCalledWith(component.toxic.type);
  });

  it('should create from for new', () => {
    component.proxy = proxy;

    component.createFormForNew();

    expect(component.toxicForm).toBeTruthy();

    let typeControl = component.toxicForm.controls['type'];
    expect(typeControl.value).toEqual('');
    expect(typeControl.disabled).toBeFalsy();
    expect(typeControl.validator).toEqual(Validators.required);

    let streamControl = component.toxicForm.controls['stream'];
    expect(streamControl.value).toEqual('');
    expect(streamControl.disabled).toBeFalsy();
    expect(streamControl.validator).toEqual(Validators.required);

    let toxicityControl = component.toxicForm.controls['toxicity'];
    expect(toxicityControl.value).toEqual('1.0');
    expect(toxicityControl.disabled).toBeFalsy();
    expect(toxicityControl.validator).toEqual(Validators.required);
  });

  it('should type select with no toxic', () => {
    component.typeSelect(ToxicTypeConstants.LATENCY.value);

    let attributesControl = component.toxicForm.controls['attributes'];

    let latency = attributesControl.get('latency');
    expect(latency.value).toEqual('');
    expect(latency.validator).toEqual(Validators.required);

    let jitter = attributesControl.get('jitter');
    expect(jitter.value).toEqual('');
    expect(jitter.validator).toEqual(Validators.required);
  });

  it('should type select with toxic', () => {
    let toxic = new Toxic();
    toxic.type = ToxicTypeConstants.LATENCY.name;
    toxic.attributes = {'latency':'100', 'jitter':'200'};
    component.toxic = toxic;

    component.typeSelect(ToxicTypeConstants.LATENCY.value);

    let attributesControl = component.toxicForm.controls['attributes'];

    let latency = attributesControl.get('latency');
    expect(latency.value).toEqual('100');
    expect(latency.validator).toEqual(Validators.required);

    let jitter = attributesControl.get('jitter');
    expect(jitter.value).toEqual('200');
    expect(jitter.validator).toEqual(Validators.required);
  });
  
});

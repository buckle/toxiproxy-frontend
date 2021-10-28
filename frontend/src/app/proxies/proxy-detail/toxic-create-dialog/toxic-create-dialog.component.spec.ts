import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ToxicCreateDialogComponent} from './toxic-create-dialog.component';
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from '@angular/material/dialog';
import {MatInputModule} from '@angular/material/input';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {MatSelectModule} from '@angular/material/select';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import {MatTooltipModule} from '@angular/material/tooltip';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {ToxiproxyService} from '../../../services/toxiproxy.service';
import {HttpClient, HttpHandler} from '@angular/common/http';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import {ToxicData} from './toxic-data';
import {Proxy} from '../../../services/proxy';
import {Toxic} from '../../../services/toxic';
import {ToxicTypeConstants} from './toxic-type/toxic-type-constants';
import {of, throwError} from 'rxjs';
import createSpyObj = jasmine.createSpyObj;
import SpyObj = jasmine.SpyObj;

describe('ToxicCreateDialogComponent', () => {
  let component: ToxicCreateDialogComponent;
  let fixture: ComponentFixture<ToxicCreateDialogComponent>;
  let proxyService: SpyObj<ToxiproxyService>;
  let dialog: SpyObj<MatDialogRef<any>>;
  let snackBar: SpyObj<MatSnackBar>;
  let proxy: Proxy;
  let toxic: Toxic;

  beforeEach(() => {
    const toxiProxySpy = createSpyObj('ToxiproxyService', ['updateToxic', 'addToxic']);
    const matDialogRef = <SpyObj<MatDialogRef<any>>>createSpyObj('MatDialogRef', ['close']);
    const snackBarSpy = createSpyObj('MatSnackBar', ['open']);

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
        FormBuilder,
        {provide: MatDialogRef, useValue: matDialogRef},
        {provide: MAT_DIALOG_DATA, useValue: {}},
        {provide: ToxiproxyService, useValue: toxiProxySpy},
        {provide: MatSnackBar, useValue: snackBarSpy}
      ]
    });

    fixture = TestBed.createComponent(ToxicCreateDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    proxyService = TestBed.inject(ToxiproxyService) as jasmine.SpyObj<ToxiproxyService>;
    dialog = TestBed.inject(MatDialogRef) as jasmine.SpyObj<MatDialogRef<any>>;
    snackBar = TestBed.inject(MatSnackBar) as jasmine.SpyObj<MatSnackBar>;

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
    };
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('renders the form', () => {
    const page: HTMLElement = fixture.nativeElement;
    const formTitle = page.querySelector('.toxic-create-title');
    const form = page.querySelector('.create-toxic-form');
    const typeSelection = form.querySelector('#type-selection');
    const streamValueSelection = form.querySelector('#stream-value-selection');

    expect(formTitle.textContent).toMatch('Toxic');
    expect(form).toBeDefined();

    expect(typeSelection).toBeDefined();
    expect(typeSelection.textContent).toMatch('Type');

    expect(streamValueSelection).toBeDefined();
    expect(streamValueSelection.textContent).toMatch('Stream');
  });

  it('should init with toxic', () => {
    const createFormForUpdateSpy = spyOn(component, 'createFormForUpdate');

    const toxicData = new ToxicData();
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
    const createFormForNewSpy = spyOn(component, 'createFormForNew');

    const toxicData = new ToxicData();
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
    const typeSelectSpy = spyOn(component, 'typeSelect');

    component.createFormForUpdate();

    expect(component.toxicForm).toBeTruthy();

    const nameControl = component.toxicForm.controls['name'];
    expect(nameControl.value).toEqual(component.toxic.name);
    expect(nameControl.disabled).toBeTruthy();
    expect(nameControl.validator).toEqual(Validators.required);

    const typeControl = component.toxicForm.controls['type'];
    expect(typeControl.value).toEqual(component.toxic.type);
    expect(typeControl.disabled).toBeTruthy();
    expect(typeControl.validator).toEqual(Validators.required);

    const streamControl = component.toxicForm.controls['stream'];
    expect(streamControl.value).toEqual(component.toxic.stream);
    expect(streamControl.disabled).toBeTruthy();
    expect(streamControl.validator).toEqual(Validators.required);

    const toxicityControl = component.toxicForm.controls['toxicity'];
    expect(toxicityControl.value).toEqual(component.toxic.toxicity + '');
    expect(toxicityControl.disabled).toBeFalsy();
    expect(toxicityControl.validator).toEqual(Validators.required);

    expect(typeSelectSpy).toHaveBeenCalledWith(component.toxic.type);
  });

  it('should create from for new', () => {
    component.proxy = proxy;

    component.createFormForNew();

    expect(component.toxicForm).toBeTruthy();

    const nameControl = component.toxicForm.controls['name'];
    expect(nameControl.value).toEqual('');
    expect(nameControl.disabled).toBeTruthy();

    const typeControl = component.toxicForm.controls['type'];
    expect(typeControl.value).toEqual('');
    expect(typeControl.disabled).toBeFalsy();
    expect(typeControl.validator).toEqual(Validators.required);

    const streamControl = component.toxicForm.controls['stream'];
    expect(streamControl.value).toEqual('');
    expect(streamControl.disabled).toBeFalsy();
    expect(streamControl.validator).toEqual(Validators.required);

    const toxicityControl = component.toxicForm.controls['toxicity'];
    expect(toxicityControl.value).toEqual('1.0');
    expect(toxicityControl.disabled).toBeFalsy();
    expect(toxicityControl.validator).toEqual(Validators.required);
  });

  it('should type select with no toxic', () => {
    const toxicFormRemoveControlSpy = spyOn(component.toxicForm, 'removeControl');

    component.typeSelect(ToxicTypeConstants.LATENCY.value);

    expect(toxicFormRemoveControlSpy).toHaveBeenCalledWith('attributes');
    const attributesControl = component.toxicForm.controls['attributes'];
    const latency = attributesControl.get('latency');
    expect(latency.value).toEqual('');
    expect(latency.validator).toEqual(Validators.required);

    const jitter = attributesControl.get('jitter');
    expect(jitter.value).toEqual('');
    expect(jitter.validator).toEqual(Validators.required);
  });

  it('should type select with toxic', () => {
    const toxic1 = new Toxic();
    toxic1.type = ToxicTypeConstants.LATENCY.name;
    toxic1.attributes = {'latency':'100', 'jitter':'200'};
    component.toxic = toxic1;
    const toxicFormRemoveControlSpy = spyOn(component.toxicForm, 'removeControl');

    component.typeSelect(ToxicTypeConstants.LATENCY.value);

    expect(toxicFormRemoveControlSpy).toHaveBeenCalledWith('attributes');
    const attributesControl = component.toxicForm.controls['attributes'];
    const latency = attributesControl.get('latency');
    expect(latency.value).toEqual('100');
    expect(latency.validator).toEqual(Validators.required);

    const jitter = attributesControl.get('jitter');
    expect(jitter.value).toEqual('200');
    expect(jitter.validator).toEqual(Validators.required);
  });

  it('should submit when updating toxic', () => {
    const createToxicFromFromDataSpy = spyOn(component, 'createToxicFromFormData');
    const updateToxicSpy = spyOn(component, 'updateToxic');
    component.isUpdate = true;
    expect(component.inProgress).toBeFalsy();

    component.onSubmit();

    expect(component.inProgress).toBeTruthy();
    expect(createToxicFromFromDataSpy).toHaveBeenCalledTimes(1);
    expect(updateToxicSpy).toHaveBeenCalledTimes(1);
  });

  it('should submit when creating toxic', () => {
    const createToxicFromFromDataSpy = spyOn(component, 'createToxicFromFormData');
    const createToxicSpy = spyOn(component, 'createToxic');
    component.isUpdate = false;
    expect(component.inProgress).toBeFalsy();

    component.onSubmit();

    expect(component.inProgress).toBeTruthy();
    expect(createToxicFromFromDataSpy).toHaveBeenCalledTimes(1);
    expect(createToxicSpy).toHaveBeenCalledTimes(1);
  });

  it('should create toxic from form data', () => {
    component.selectedType = ToxicTypeConstants.LATENCY;
    component.toxicForm.setValue({
      'name':'latency_upstream',
      'type':'latency',
      'stream':'upstream',
      'toxicity':'1'
    });
    component.toxicForm.addControl('attributes', new FormGroup({latency: new FormControl('100'), jitter: new FormControl('101')}));

    const newToxic = component.createToxicFromFormData();

    expect(newToxic).toBeTruthy();
    expect(newToxic.name).toEqual('latency_upstream');
    expect(newToxic.type).toEqual('latency');
    expect(newToxic.attributes).toEqual({'latency': 100, 'jitter': 101});
    expect(newToxic.stream).toEqual('upstream');
    expect(newToxic.toxicity).toEqual(1);
  });

  it('should update toxic', () => {
    component.proxy = proxy;
    component.inProgress = true;
    proxyService.updateToxic.and.returnValue(of(new Proxy()));

    component.updateToxic(toxic);

    expect(dialog.close).toHaveBeenCalledTimes(1);
    expect(component.inProgress).toBeFalsy();
    expect(proxyService.updateToxic).toHaveBeenCalledWith(proxy, toxic);
  });

  it('should update toxic with error', () => {
    component.proxy = proxy;
    component.inProgress = true;
    proxyService.updateToxic.and.returnValue(throwError('bad call'));

    component.updateToxic(toxic);

    expect(dialog.close).toHaveBeenCalledTimes(0);
    expect(component.inProgress).toBeFalsy();
    expect(proxyService.updateToxic).toHaveBeenCalledWith(proxy, toxic);
    expect(snackBar.open).toHaveBeenCalledWith('Unable to edit toxic.', 'Close', {duration: 5000});
  });

  it('should create toxic', () => {
    component.proxy = proxy;
    component.inProgress = true;
    proxyService.addToxic.and.returnValue(of(new Proxy()));

    component.createToxic(toxic);

    expect(dialog.close).toHaveBeenCalledTimes(1);
    expect(component.inProgress).toBeFalsy();
    expect(proxyService.addToxic).toHaveBeenCalledWith(proxy, toxic);
  });

  it('should create toxic with error', () => {
    component.proxy = proxy;
    component.inProgress = true;
    proxyService.addToxic.and.returnValue(throwError('bad call'));

    component.createToxic(toxic);

    expect(dialog.close).toHaveBeenCalledTimes(0);
    expect(component.inProgress).toBeFalsy();
    expect(proxyService.addToxic).toHaveBeenCalledWith(proxy, toxic);
    expect(snackBar.open).toHaveBeenCalledWith('Unable to add toxic to proxy.', 'Close', {duration: 5000});
  });
});

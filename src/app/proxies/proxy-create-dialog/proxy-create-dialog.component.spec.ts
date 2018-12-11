import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ProxyCreateDialogComponent} from './proxy-create-dialog.component';
import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef,
  MatFormFieldModule, MatInputModule,
  MatProgressSpinnerModule,
  MatRadioModule,
  MatSnackBarModule
} from '@angular/material';
import {ReactiveFormsModule} from '@angular/forms';
import {ToxiproxyService} from '../../services/toxiproxy.service';
import {HttpClient, HttpHandler} from '@angular/common/http';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';

describe('ProxyCreateDialogComponent', () => {
  let component: ProxyCreateDialogComponent;
  let fixture: ComponentFixture<ProxyCreateDialogComponent>;

  beforeEach(async(() => {
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
        { provide: MatDialogRef, useValue: {} },
        { provide: MAT_DIALOG_DATA, useValue: {} }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProxyCreateDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

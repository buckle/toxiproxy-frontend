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
import {ReactiveFormsModule} from '@angular/forms';
import {ToxiproxyService} from '../../../services/toxiproxy.service';
import {HttpClient, HttpHandler} from '@angular/common/http';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';

describe('ToxicCreateDialogComponent', () => {
  let component: ToxicCreateDialogComponent;
  let fixture: ComponentFixture<ToxicCreateDialogComponent>;

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
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

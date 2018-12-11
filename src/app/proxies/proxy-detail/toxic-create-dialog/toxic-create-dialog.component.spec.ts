import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ToxicCreateDialogComponent } from './toxic-create-dialog.component';
import {AppModule} from '../../../app.module';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';

describe('ToxicCreateDialogComponent', () => {
  let component: ToxicCreateDialogComponent;
  let fixture: ComponentFixture<ToxicCreateDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AppModule],
      providers: [
        { provide: MatDialogRef, useValue: {} },
        { provide: MAT_DIALOG_DATA, useValue: {} }
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

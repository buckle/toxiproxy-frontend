import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProxyCreateDialogComponent } from './proxy-create-dialog.component';
import {AppModule} from '../../app.module';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';

describe('ProxyCreateDialogComponent', () => {
  let component: ProxyCreateDialogComponent;
  let fixture: ComponentFixture<ProxyCreateDialogComponent>;

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
    fixture = TestBed.createComponent(ProxyCreateDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

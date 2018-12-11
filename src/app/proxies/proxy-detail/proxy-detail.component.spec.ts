import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ProxyDetailComponent} from './proxy-detail.component';
import {AppModule} from '../../app.module';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';

describe('ProxyDetailComponent', () => {
  let component: ProxyDetailComponent;
  let fixture: ComponentFixture<ProxyDetailComponent>;

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
    fixture = TestBed.createComponent(ProxyDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

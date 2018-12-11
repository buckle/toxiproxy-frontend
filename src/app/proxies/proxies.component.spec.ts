import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ProxiesComponent} from './proxies.component';
import {
  MatDialogModule,
  MatDividerModule,
  MatFormFieldModule,
  MatIconModule,
  MatInputModule,
  MatPaginatorModule,
  MatTableModule
} from '@angular/material';
import {RouterTestingModule} from '@angular/router/testing';
import {FormsModule} from '@angular/forms';
import {ToxiproxyService} from '../services/toxiproxy.service';
import {HttpClient, HttpHandler} from '@angular/common/http';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';

describe('ProxiesComponent', () => {
  let component: ProxiesComponent;
  let fixture: ComponentFixture<ProxiesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ProxiesComponent],
      imports: [
        MatDividerModule,
        MatIconModule,
        MatFormFieldModule,
        MatTableModule,
        MatPaginatorModule,
        RouterTestingModule,
        FormsModule,
        MatDialogModule,
        MatInputModule,
        NoopAnimationsModule
      ],
      providers: [
        ToxiproxyService,
        HttpClient,
        HttpHandler
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProxiesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

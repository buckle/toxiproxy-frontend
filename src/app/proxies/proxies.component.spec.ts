import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProxiesComponent } from './proxies.component';
import {AppModule} from '../app.module';

describe('ProxiesComponent', () => {
  let component: ProxiesComponent;
  let fixture: ComponentFixture<ProxiesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AppModule]
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

import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ProxyDetailComponent} from './proxy-detail.component';
import {AppModule} from '../../app.module';
import {RouterTestingModule} from '@angular/router/testing';
import {ToxiproxyService} from '../../services/toxiproxy.service';
import {of} from 'rxjs';
import createSpyObj = jasmine.createSpyObj;
import SpyObj = jasmine.SpyObj;

describe('ProxyDetailComponent', () => {
  let component: ProxyDetailComponent;
  let fixture: ComponentFixture<ProxyDetailComponent>;
  let proxyService: SpyObj<ToxiproxyService>;
  let proxy;

  beforeEach(async(() => {
    const toxiProxySpy = createSpyObj('ToxiproxyService', ['getProxy']);

    TestBed.configureTestingModule({
      imports: [
        AppModule,
        RouterTestingModule
      ],
      providers: [
        {provide: ToxiproxyService, useValue: toxiProxySpy}
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProxyDetailComponent);
    component = fixture.componentInstance;
    proxyService = TestBed.get(ToxiproxyService);

    proxy = {
      'name': 'BarkerProxy',
      'listen': 'localhost:5002',
      'upstream': 'somedomain.com:5003',
      'enabled': true,
      'toxics': [
        {
          'attributes': {
            'rate': 123
          },
          'name': 'bandwidth_upstream',
          'type': 'bandwidth',
          'stream': 'upstream',
          'toxicity': 1
        }
      ]
    };
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load proxy', () => {
    // TODO Test parameter from url
    proxyService.getProxy.and.returnValue(of(proxy));
    fixture.detectChanges();

    expect(component.proxy).toBeTruthy();
    expect(component.proxy.name).toEqual(proxy.name);
    expect(component.toxicDataSource).toBeTruthy();
    expect(component.toxicDataSource.data).toContain(proxy.toxics[0]);
  });
});

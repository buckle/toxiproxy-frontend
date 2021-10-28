import {ComponentFixture, TestBed} from '@angular/core/testing';

import {HeaderComponent} from './header.component';
import {MatToolbarModule} from '@angular/material/toolbar';
import {ToxiproxyService} from '../services/toxiproxy.service';
import {of, throwError} from 'rxjs';
import SpyObj = jasmine.SpyObj;

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let proxyService: SpyObj<ToxiproxyService>;
  let fixture: ComponentFixture<HeaderComponent>;

  beforeEach(() => {
    const toxiProxySpy = jasmine.createSpyObj('ToxiproxyService', ['getProxyVersion']);

    TestBed.configureTestingModule({
      imports: [MatToolbarModule],
      declarations: [HeaderComponent],
      providers: [HeaderComponent, {provide: ToxiproxyService, useValue: toxiProxySpy}]
    });

    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
    proxyService = TestBed.inject(ToxiproxyService) as jasmine.SpyObj<ToxiproxyService>;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('render correct version', () => {
    proxyService.getProxyVersion.and.returnValue(of('1.0.0'));
    fixture.detectChanges();

    let textContent = fixture.nativeElement.querySelector('.header-version').textContent;
    expect(textContent).toBe('Version: 1.0.0');
  });

  it('render version when version lookup fails', () => {
    proxyService.getProxyVersion.and.returnValue(throwError("Failed to retrieve version"));
    fixture.detectChanges();

    let textContent = fixture.nativeElement.querySelector('.header-version').textContent;
    expect(textContent).toBe('Version: ');
  });
});

import { TestBed } from '@angular/core/testing';

import { ToxiproxyService } from './toxiproxy.service';
import {HttpClient, HttpHandler} from '@angular/common/http';

describe('ToxiproxyService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [ToxiproxyService, HttpClient, HttpHandler]
  }));

  it('should be created', () => {
    const service: ToxiproxyService = TestBed.get(ToxiproxyService);
    expect(service).toBeTruthy();
  });
});

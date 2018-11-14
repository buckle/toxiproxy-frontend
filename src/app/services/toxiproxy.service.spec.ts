import { TestBed } from '@angular/core/testing';

import { ToxiproxyService } from './toxiproxy.service';

describe('ToxiproxyService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ToxiproxyService = TestBed.get(ToxiproxyService);
    expect(service).toBeTruthy();
  });
});

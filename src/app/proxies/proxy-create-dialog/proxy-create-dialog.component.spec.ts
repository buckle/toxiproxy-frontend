import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProxyCreateDialogComponent } from './proxy-create-dialog.component';

describe('ProxyCreateDialogComponent', () => {
  let component: ProxyCreateDialogComponent;
  let fixture: ComponentFixture<ProxyCreateDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProxyCreateDialogComponent ]
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

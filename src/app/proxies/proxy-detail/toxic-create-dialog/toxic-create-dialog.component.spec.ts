import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ToxicCreateDialogComponent } from './toxic-create-dialog.component';

describe('ToxicCreateDialogComponent', () => {
  let component: ToxicCreateDialogComponent;
  let fixture: ComponentFixture<ToxicCreateDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ToxicCreateDialogComponent ]
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

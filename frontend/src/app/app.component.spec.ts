import {TestBed} from '@angular/core/testing';
import {AppComponent} from './app.component';
import {AppModule} from './app.module';

describe('AppComponent', () => {

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AppModule],
    });
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  });
});

import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {HttpClientModule} from '@angular/common/http';

import {AppComponent} from './app.component';
import {HeaderComponent} from './header/header.component';
import {MatToolbarModule, MatListModule, MatIconModule} from '@angular/material';
import {ToxiproxyService} from './services/toxiproxy.service';
import {ProxiesComponent} from './proxies/proxies.component';


@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    ProxiesComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    MatToolbarModule,
    MatListModule,
    MatIconModule
  ],
  providers: [ToxiproxyService],
  bootstrap: [AppComponent]
})
export class AppModule {
}

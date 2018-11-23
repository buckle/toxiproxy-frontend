import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {HttpClientModule} from '@angular/common/http';
import {RouterModule, Routes} from '@angular/router';

import {AppComponent} from './app.component';
import {HeaderComponent} from './header/header.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {
  MatToolbarModule,
  MatListModule,
  MatIconModule,
  MatDialogModule,
  MatButtonModule,
  MatInputModule,
  MatFormFieldModule,
  MatRadioModule,
  MatProgressSpinnerModule,
  MatSnackBarModule,
  MatCardModule,
  MatSelectModule,
  MatOptionModule,
  MatTooltipModule,
  MatTableModule,
  MatSortModule,
  MatPaginatorModule,
  MatDividerModule
} from '@angular/material';
import {FlexLayoutModule} from '@angular/flex-layout';
import {ToxiproxyService} from './services/toxiproxy.service';
import {ProxiesComponent} from './proxies/proxies.component';
import {ProxyDetailComponent} from './proxies/proxy-detail/proxy-detail.component';
import {ProxyCreateDialogComponent} from './proxies/proxy-create-dialog/proxy-create-dialog.component';
import {ToxicCreateDialogComponent} from './proxies/proxy-detail/toxic-create-dialog/toxic-create-dialog.component';

const appRoutes: Routes = [
  {path: 'proxies', component: ProxiesComponent},
  {path: 'proxies/:name', component: ProxyDetailComponent},
  {
    path: '',
    redirectTo: '/proxies',
    pathMatch: 'full'
  }
];

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    ProxiesComponent,
    ProxyDetailComponent,
    ProxyCreateDialogComponent,
    ToxicCreateDialogComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(appRoutes),
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule,
    FlexLayoutModule,
    MatToolbarModule,
    MatListModule,
    MatIconModule,
    MatDialogModule,
    MatButtonModule,
    MatInputModule,
    MatFormFieldModule,
    MatRadioModule,
    BrowserAnimationsModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatCardModule,
    MatSelectModule,
    MatOptionModule,
    MatTooltipModule,
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    MatDividerModule
  ],
  entryComponents: [ProxyCreateDialogComponent, ToxicCreateDialogComponent],
  providers: [ToxiproxyService],
  bootstrap: [AppComponent]
})
export class AppModule {
}

import {Component, OnInit} from '@angular/core';
import {ToxiproxyService} from '../services/toxiproxy.service';
import {Proxy} from '../services/Proxy';
import {ProxyResponse} from '../services/proxy-response';
import {ProxyCreateDialogComponent} from './proxy-create-dialog/proxy-create-dialog.component';
import {MatDialog} from '@angular/material';

@Component({
  selector: 'app-proxies',
  templateUrl: './proxies.component.html',
  styleUrls: ['./proxies.component.scss']
})
export class ProxiesComponent implements OnInit {

  proxies: Proxy[];

  constructor(private proxyService: ToxiproxyService,
              private dialog: MatDialog) {
  }

  ngOnInit() {
    this.loadProxies();
  }

  loadProxies() {
    this.proxyService
      .getProxies()
      .subscribe(value => {
        this.proxies = new ProxyResponse(value).proxies;
      });
  }

  openProxyCreate(): void {
    const dialogRef = this.dialog.open(ProxyCreateDialogComponent, {
      width: '500px',
    });

    dialogRef.afterClosed().subscribe(() => {
      this.loadProxies();
    });
  }
}

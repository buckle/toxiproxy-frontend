import {Component, OnInit, ViewChild} from '@angular/core';
import {ToxiproxyService} from '../services/toxiproxy.service';
import {ProxyResponse} from '../services/proxy-response';
import {ProxyCreateDialogComponent} from './proxy-create-dialog/proxy-create-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { PageEvent } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import {Proxy} from '../services/proxy';

@Component({
  selector: 'app-proxies',
  templateUrl: './proxies.component.html',
  styleUrls: ['./proxies.component.scss']
})
export class ProxiesComponent implements OnInit {

  proxies: MatTableDataSource<Proxy>;
  filter: string;
  displayedColumns: string[] = ['name', 'enabled', 'listen', 'upstream'];
  @ViewChild(MatSort,  {static: true}) sort: MatSort;
  pageSize: number = 25;
  pageIndex: number = 0;
  totalItems: number = 0;

  constructor(private proxyService: ToxiproxyService,
              private dialog: MatDialog) {
  }

  ngOnInit() {
    this.loadProxies(null);
  }

  loadProxies(event: PageEvent) {
    if(event != null) {
      this.pageSize = event.pageSize;
      this.pageIndex = event.pageIndex;
    }

    this.proxyService
      .getProxies()
      .subscribe(value => {
        let serviceProxies = new ProxyResponse(value).proxies;
        if(serviceProxies) {
          this.totalItems = serviceProxies.length;

          let multiplier = this.pageIndex + 1;
          const beginIndex = (this.pageSize * multiplier) - this.pageSize;
          const endIndex = this.pageSize * multiplier;

          let filteredProxies = serviceProxies.slice(beginIndex, endIndex);
          this.proxies = new MatTableDataSource(filteredProxies);
          this.proxies.sort = this.sort;
        }
      });
  }

  applyFilter() {
    if(this.proxies) {
      this.proxies.filter = this.filter;
    }
  }

  openProxyCreate(): void {
    let createProxyDialog = this.dialog.open(ProxyCreateDialogComponent, {
      width: '500px',
    });

    createProxyDialog.afterClosed().subscribe(() => {
      this.loadProxies(null)
    });
  }
}

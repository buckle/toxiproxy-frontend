import {Component, OnInit, ViewChild} from '@angular/core';
import {ToxiproxyService} from '../services/toxiproxy.service';
import {ProxyResponse} from '../services/proxy-response';
import {ProxyCreateDialogComponent} from './proxy-create-dialog/proxy-create-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import {Proxy} from '../services/proxy';

@Component({
  selector: 'app-proxies',
  templateUrl: './proxies.component.html',
  styleUrls: ['./proxies.component.scss'],
  standalone: false
})
export class ProxiesComponent implements OnInit {

  proxies: MatTableDataSource<Proxy>;
  filter: string;
  displayedColumns: string[] = ['name', 'enabled', 'listen', 'upstream'];
  @ViewChild(MatSort,  {static: true}) sort: MatSort;
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  pageSize: number = 25;

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
        let serviceProxies = new ProxyResponse(value).proxies;
        if(serviceProxies) {
          this.proxies = new MatTableDataSource(serviceProxies);
          this.proxies.paginator = this.paginator;
          this.proxies.sort = this.sort;

          if(this.filter) {
            this.proxies.filter = this.filter;
          }
        }
      });
  }

  applyFilter() {
    if(this.proxies) {
      this.proxies.filter = this.filter;
      if (this.proxies.paginator) {
        this.proxies.paginator.firstPage();
      }
    }
  }

  openProxyCreate(): void {
    let createProxyDialog = this.dialog.open(ProxyCreateDialogComponent, {
      width: '500px',
    });

    createProxyDialog.afterClosed().subscribe(() => {
      this.loadProxies()
    });
  }
}

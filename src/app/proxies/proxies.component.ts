import {Component, OnInit, ViewChild} from '@angular/core';
import {ToxiproxyService} from '../services/toxiproxy.service';
import {ProxyResponse} from '../services/proxy-response';
import {ProxyCreateDialogComponent} from './proxy-create-dialog/proxy-create-dialog.component';
import {MatDialog, MatSort, MatTableDataSource, PageEvent} from '@angular/material';
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
  @ViewChild(MatSort) sort: MatSort;
  pageSize: number;
  pageIndex: number;
  totalItems: number;

  constructor(private proxyService: ToxiproxyService,
              private dialog: MatDialog) {
  }

  ngOnInit() {
    this.pageSize = 25;
    this.pageIndex = 0;
    this.totalItems = 0;
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
        this.totalItems = serviceProxies.length;

        let multiplier = this.pageIndex + 1;
        const beginIndex = (this.pageSize * multiplier) - this.pageSize;
        const endIndex = this.pageSize * multiplier;

        let filteredProxies = serviceProxies.slice(beginIndex, endIndex);
        this.proxies = new MatTableDataSource(filteredProxies);
        this.proxies.sort = this.sort;
      });
  }

  applyFilter() {
    if(this.proxies) {
      this.proxies.filter = this.filter;
    }
  }

  openProxyCreate(): void {
    const dialogRef = this.dialog.open(ProxyCreateDialogComponent, {
      width: '500px',
    });

    dialogRef.afterClosed().subscribe(() => {
      this.loadProxies(null);
    });
  }
}

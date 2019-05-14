import {Component, OnInit} from '@angular/core';
import {ToxiproxyService} from '../../services/toxiproxy.service';
import {Proxy} from '../../services/proxy';
import {ActivatedRoute, Router} from '@angular/router';
import {MatDialog, MatSnackBar, MatTableDataSource} from '@angular/material';
import {ToxicCreateDialogComponent} from './toxic-create-dialog/toxic-create-dialog.component';
import {Toxic} from '../../services/toxic';
import {ProxyCreateDialogComponent} from '../proxy-create-dialog/proxy-create-dialog.component';

@Component({
  selector: 'app-proxy-detail',
  templateUrl: './proxy-detail.component.html',
  styleUrls: ['./proxy-detail.component.scss']
})
export class ProxyDetailComponent implements OnInit {

  proxy: Proxy;
  toxicDataSource: MatTableDataSource<Toxic>;
  objectEntries = Object.entries;
  displayedColumns: string[] = ['name', 'type', 'stream', 'toxicity', 'attributes', 'delete'];

  constructor(private proxyService: ToxiproxyService,
              private route: ActivatedRoute,
              private router: Router,
              private snackBar: MatSnackBar,
              private dialog: MatDialog) {
  }

  ngOnInit() {
    this.loadProxy();
  }

  loadProxy() {
    this.route.paramMap.subscribe(params => {
      let proxyName = params.get('name');
      if(proxyName) {
        this.proxyService
          .getProxy(proxyName)
          .subscribe(
            value => {
              this.proxy = value;
              this.toxicDataSource = new MatTableDataSource(value.toxics);
            },
            () => {
              this.snackBar.open(
                'Unable to load proxy.',
                'Close',
                {duration: 8000});
            }
          );
      }
    });
  }

  deleteProxy() {
    this.proxyService
      .deleteProxy(this.proxy)
      .subscribe(
        () => {
          this.router.navigate(['/proxies']);
        },
        () => {
          this.snackBar.open(
            'Unable to delete proxy.',
            'Close',
            {duration: 8000});
        }
      );
  }

  editProxy() {
    const dialogRef = this.dialog.open(ProxyCreateDialogComponent, {
      width: '500px',
      data: this.proxy
    });

    dialogRef.afterClosed().subscribe(() => {
      this.loadProxy();
    });
  }

  openCreateToxicDialog() {
    const dialogRef = this.dialog.open(ToxicCreateDialogComponent, {
      width: '500px',
      data: {'proxy': this.proxy}
    });

    dialogRef.afterClosed().subscribe(() => {
      this.loadProxy();
    });
  }

  editToxicDialog(toxic: Toxic) {
    const dialogRef = this.dialog.open(ToxicCreateDialogComponent, {
      width: '500px',
      data: {'proxy': this.proxy, 'toxic': toxic}
    });

    dialogRef.afterClosed().subscribe(() => {
      this.loadProxy();
    });
  }

  deleteToxic(toxic: Toxic) {
    this.proxyService
      .deleteToxic(this.proxy, toxic)
      .subscribe(
        () => {
          this.loadProxy();
        },
        () => {
          this.snackBar.open(
            'Unable to delete toxic.',
            'Close',
            {duration: 8000});
        }
      );
  }
}

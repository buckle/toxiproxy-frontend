import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import {ToxiproxyService} from '../services/toxiproxy.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  standalone: false
})
export class HeaderComponent implements OnInit {

  version: string;

  constructor(private proxyService: ToxiproxyService, private changeDetector: ChangeDetectorRef) { }

  ngOnInit() {
    this.proxyService.getProxyVersion().subscribe(value => {
      this.version = value;
      this.changeDetector.detectChanges();
    });
  }

}

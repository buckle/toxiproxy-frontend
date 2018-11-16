import { Component, OnInit } from '@angular/core';
import {ToxiproxyService} from '../services/toxiproxy.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  version: string;

  constructor(private proxyService: ToxiproxyService) { }

  ngOnInit() {
    this.proxyService.getProxyVersion().subscribe(value => this.version = value);
  }

}

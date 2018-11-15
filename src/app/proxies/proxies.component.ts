import {Component, OnInit} from '@angular/core';
import {ToxiproxyService} from '../services/toxiproxy.service';
import {Proxy} from '../services/Proxy';
import {ProxyResponse} from '../services/ProxyResponse';

@Component({
  selector: 'app-proxies',
  templateUrl: './proxies.component.html',
  styleUrls: ['./proxies.component.css']
})
export class ProxiesComponent implements OnInit {

  proxies: Proxy[];

  constructor(private proxyService: ToxiproxyService) {
  }

  ngOnInit() {
    this.proxyService
      .getProxies()
      .subscribe(value => {
        this.proxies = new ProxyResponse(value).proxies;
      });
  }
}

import {Component, OnInit} from '@angular/core';
import {ToxiproxyService} from '../../services/toxiproxy.service';
import {Proxy} from '../../services/proxy';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-proxy-detail',
  templateUrl: './proxy-detail.component.html',
  styleUrls: ['./proxy-detail.component.scss']
})
export class ProxyDetailComponent implements OnInit {

  proxy: Proxy;

  constructor(private proxyService: ToxiproxyService,
              private route: ActivatedRoute) {

  }

  ngOnInit() {
    const name = this.route.snapshot.paramMap.get('name');
    this.proxyService.getProxy(name).subscribe(value => this.proxy = value);
  }

}

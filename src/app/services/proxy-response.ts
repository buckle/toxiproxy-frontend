import {Proxy} from './proxy';

export class ProxyResponse {

  proxies: Proxy[];

  constructor(response: object) {
    if(response) {
      // @ts-ignore
      this.proxies = Object.values(response);
    }
  }
}

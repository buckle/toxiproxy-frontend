import {Proxy} from './Proxy';

export class ProxyResponse {

  proxies: Proxy[];

  constructor(response: object) {
    if(response) {
      // @ts-ignore
      this.proxies = Object.values(response);
    }
  }
}

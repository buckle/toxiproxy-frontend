import {Proxy} from './Proxy';

export class ProxyResponse {

  private readonly _proxies: Proxy[];

  constructor(response: object) {
    if(response) {
      // @ts-ignore
      this._proxies = Object.values(response);
    }
  }

  get proxies(): Proxy[] {
    return this._proxies;
  }
}

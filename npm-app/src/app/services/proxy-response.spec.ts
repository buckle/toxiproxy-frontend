import {ProxyResponse} from './proxy-response';

describe('ProxyResponse', () => {

  it('should construct', () => {
    let proxiesKeyed = {
      "Propane Master": {
        "name": "Propane Master",
        "listen": "host:8010",
        "upstream": "host:8011",
        "enabled": false,
        "toxics": []
      },
      "Terminator": {
        "name": "Terminator",
        "listen": "127.0.0.1:8001",
        "upstream": "host:8002",
        "enabled": true,
        "toxics": []
      }
    };

    let proxyResponse = new ProxyResponse(proxiesKeyed);

    expect(proxyResponse.proxies).toBeTruthy();
    expect(proxyResponse.proxies[0].name).toEqual('Propane Master');
    expect(proxyResponse.proxies[1].name).toEqual('Terminator');
  });

  it('should construct with empty data', () => {
    let proxyResponse = new ProxyResponse(null);

    expect(proxyResponse.proxies).toBeUndefined();
  });
});

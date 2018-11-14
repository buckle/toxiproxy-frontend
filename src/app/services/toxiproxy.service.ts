import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class ToxiproxyService {

  constructor(private http: HttpClient) {
  }
}

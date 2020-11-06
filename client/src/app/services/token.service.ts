import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { config } from '../../config/local';
import * as Cookies from 'js-cookie';


@Injectable()
export class TokenService {

  url = `${config.authUrl}/oauth/token`;

  constructor(private httpClient: HttpClient) {
  }


  obtainAccessToken(email: string, password: string) {
    let body: HttpParams = new HttpParams();
    body = body.append('username', email);
    body = body.append('password', password);
    body = body.append('scope', 'read write');
    body = body.append('grant_type', 'password');
    body = body.append('client_id', config.clientId);


    return this.httpClient.post(this.url, body, {
      headers: {
        'Content-type': 'application/x-www-form-urlencoded; charset=utf-8',
        Authorization: 'Basic '
          + btoa(`${config.clientId}:${config.clientSecret}`)
      }
    });


  }

  obtainAccessTokenWithRefreshToken(refreshToken: string) {
    let body: HttpParams = new HttpParams();
    body = body.append('refresh_token', refreshToken);
    body = body.append('grant_type', 'refresh_token');
    return this.httpClient.post(this.url, body, {
      headers: {
        'Content-type': 'application/x-www-form-urlencoded; charset=utf-8',
        Authorization: 'Basic '
          + btoa(`${config.clientId}:${config.clientSecret}`)
      }
    });

  }

  saveToken(token): void {
    Cookies.set('usr', JSON.stringify(token), { expires: 365 });
  }


  removeToken() {
    Cookies.remove('usr');
  }

  getToken() {
    const token = Cookies.get('usr');
    if (!token) {
      return '';
    }
    return JSON.parse(token).access_token;
  }

  getRefreshToken() {
    const token = Cookies.get('usr');
    if (!token) {
      return '';
    }
    return JSON.parse(token).refresh_token;
  }

  checkIfTokenExists() {
    const token = Cookies.get('usr');
    if (!token) {
      return false;
    }
    return JSON.parse(token).access_token && JSON.parse(token).access_token.length;
  }
}

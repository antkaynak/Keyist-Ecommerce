import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";


@Injectable()
export class TokenService {


  url: string = 'backendurl/oauth/token';

  constructor(private httpClient: HttpClient) {
  }


  obtainAccessToken(email: string, password: string) {
    let body: HttpParams = new HttpParams();
    body = body.append('username', email);
    body = body.append('password', password);
    body = body.append('scope', 'read write');
    body = body.append('grant_type', 'password');
    body = body.append('client_id', 'client_id');


    return this.httpClient.post(this.url, body, {
      headers: {
        'Content-type': 'application/x-www-form-urlencoded; charset=utf-8',
        'Authorization': 'Basic '
        + btoa("client_id" + ':' + "client_password")
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
        'Authorization': 'Basic '
        + btoa("client_id" + ':' + "client_password")
      }
    });

  }

  saveToken(token): void {
    localStorage.setItem('usr', JSON.stringify(token));
  }


  removeToken() {
    localStorage.removeItem('usr');
  }

  getToken() {
    const storageToken = localStorage.getItem('usr');
    let token = '';
    if (storageToken != null || storageToken != undefined) {
      token = JSON.parse(storageToken).access_token;
    }
    return token;
  }

  getRefreshToken() {
    const storageRefreshToken = localStorage.getItem('usr');
    let refreshToken = '';
    if (storageRefreshToken != null || storageRefreshToken != undefined) {
      refreshToken = JSON.parse(storageRefreshToken).refresh_token;
    }
    return refreshToken;
  }

  checkIfTokenExists() {
    return localStorage.getItem('usr') != null || localStorage.getItem('usr') != undefined;
  }
}

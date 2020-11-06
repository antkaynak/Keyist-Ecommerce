import { User } from './../store/model';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { config } from '../../config/local';


@Injectable()
export class AccountService {

  publicUrl = `${config.apiUrl}/api/public/account`;
  url = `${config.apiUrl}/api/account`;

  constructor(private httpClient: HttpClient) {
  }

  createAccount(email: string, password: string, passwordRepeat: string) {
    return this.httpClient.post(this.publicUrl + '/registration', {
      email,
      password,
      passwordRepeat
    }, { headers: { 'Content-type': 'application/json; charset=utf-8' } });
  }

  getUser() {
    return this.httpClient.get<User>(this.url);
  }

  updateUser(user) {
    return this.httpClient.put(this.url, user);
  }

  updateUserAddress(user) {
    return this.httpClient.put(`${this.url}/address`, user);
  }

  verifyEmail(verificationToken) {
    return this.httpClient.post(this.publicUrl + '/registration/validate', {
      token: verificationToken
    });
  }

  forgotPasswordRequest(email) {
    return this.httpClient.post(this.publicUrl + '/password/forgot', {
      email
    });
  }

  forgotPasswordConfirm(passwordForgotToken) {
    return this.httpClient.post(this.publicUrl + '/password/forgot/confirm', {
      token: passwordForgotToken
    });
  }

  forgotPasswordReset(passwordForgotToken, newPassword, newPasswordConfirm) {
    return this.httpClient.post(this.publicUrl + '/password/forgot/validate', {
      token: passwordForgotToken,
      newPassword,
      newPasswordConfirm
    });
  }


  resetPassword(oldPassword, newPassword, newPasswordConfirm) {
    return this.httpClient.post(this.url + '/password/reset', {
      oldPassword,
      newPassword,
      newPasswordConfirm
    });
  }

  getVerificationStatus() {
    return this.httpClient.get(this.url + '/status');
  }

}

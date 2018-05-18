import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {User} from "../store/auth/auth.reducer";


@Injectable()
export class AccountService {

  url: string = 'backendurl/api/account';
  securedUrl: string = 'backendurl/api/secured/account';

  constructor(private httpClient: HttpClient) {
  }

  createAccount(email: string, password: string, passwordRepeat: string) {
    return this.httpClient.post(this.url + "/registration", {
      email: email,
      password: password,
      passwordRepeat: passwordRepeat
    }, {headers: {'Content-type': 'application/json; charset=utf-8'}});
  }

  getUser() {
    return this.httpClient.get<User>(this.securedUrl);
  }

  saveUser(user) {
    return this.httpClient.put(this.securedUrl, user);
  }

  verifyEmail(verificationToken) {
    return this.httpClient.get(this.url + '/registration', {
      params: new HttpParams().set('token', verificationToken)
    });
  }

  resetEmail(newEmail, newEmailConfirm, password) {
    return this.httpClient.post(this.securedUrl + '/email/reset', {
      newEmail: newEmail,
      newEmailConfirm: newEmailConfirm,
      password: password
    });
  }

  resetEmailConfirm(emailResetToken) {
    return this.httpClient.get(this.url + '/email/reset', {
      params: new HttpParams().set('token', emailResetToken)
    });
  }

  forgotPasswordRequest(email) {
    return this.httpClient.post(this.url + '/password/forgot', {
      email: email
    });
  }

  forgotPasswordConfirm(passwordForgotToken) {
    return this.httpClient.get(this.url + '/password/forgot', {
      params: new HttpParams().set('token', passwordForgotToken)
    });
  }

  forgotPasswordReset(passwordForgotToken, newPassword, newPasswordConfirm) {
    return this.httpClient.put(this.url + '/password/forgot', {
      token: passwordForgotToken,
      newPassword: newPassword,
      newPasswordConfirm: newPasswordConfirm
    });
  }


  resetPassword(oldPassword, newPassword, newPasswordConfirm) {
    return this.httpClient.post(this.securedUrl + '/password/reset', {
      oldPassword: oldPassword,
      newPassword: newPassword,
      newPasswordConfirm: newPasswordConfirm
    });
  }

  getVerificationStatus() {
    return this.httpClient.get(this.securedUrl + '/status');
  }


}
